package com.home.java_02.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${spring.data.redis.password:}")//데이터가 없을수도 있을 때 ':'
  private String redisPassword;

  @Bean
  public Jedis jedis() {
    Jedis jedis = new Jedis(redisHost, redisPort);

    if (!ObjectUtils.isEmpty(redisPassword)) {
      jedis.auth(redisPassword);
    }

    return jedis;
  }
}
