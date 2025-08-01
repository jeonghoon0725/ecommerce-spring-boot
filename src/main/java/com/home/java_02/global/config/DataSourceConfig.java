package com.home.java_02.global.config;

import com.zaxxer.hikari.HikariDataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataSourceConfig {

  public static final String MASTER_DATASOURCE = "masterDataSource";
  public static final String SLAVE_DATASOURCE = "slaveDataSource";

  @Bean(MASTER_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
  public DataSource masterDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(SLAVE_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
  public DataSource slaveDataSource() {
    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  @Primary // 다른 컴포넌트(jpa 등)들이 기본적으로 제공받는 데이터소스가 되도록
  @DependsOn({MASTER_DATASOURCE, SLAVE_DATASOURCE}) // 안에 객체가 먼저 Bean 생성된 후 Bean 생성
  public DataSource routingDataSource(
      @Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
      @Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource
  ) {
    RoutingDataSource routingDataSource = new RoutingDataSource();

    Map<Object, Object> dataSourceMap = new HashMap<>();
    dataSourceMap.put("master", masterDataSource);
    dataSourceMap.put("slave", slaveDataSource);

    routingDataSource.setTargetDataSources(dataSourceMap);
    routingDataSource.setDefaultTargetDataSource(masterDataSource);
    routingDataSource.afterPropertiesSet(); // 설정이 변경되었을 수 있으니 초기화

    return routingDataSource;
  }

  @Bean
  public DataSourceRoutingAspect dataSourceRoutingAspect() {
    return new DataSourceRoutingAspect();
  }

  @Aspect
  @Component
  @Slf4j
  @Order(1)
  public static class DataSourceRoutingAspect {

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void setDataSourceKey(JoinPoint joinPoint) {
      MethodSignature signature = (MethodSignature) joinPoint.getSignature();
      Method method = signature.getMethod();

      Transactional transactional = method.getAnnotation(Transactional.class);
      if (transactional != null && transactional.readOnly()) {
        DataSourceContextHolder.setDataSourceKey("slave");
      } else {
        DataSourceContextHolder.setDataSourceKey("master");
      }
    }

    @After("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void clearDataSourceKey() {
      DataSourceContextHolder.clearDataSource();
    }
  }
}
