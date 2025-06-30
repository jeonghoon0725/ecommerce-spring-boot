package com.home.java_02.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

  @Pointcut("execution(* com.home.java_02.domain..*(..))")
  private void allServiceMethods() {

  }

  @Around("allServiceMethods()")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    Object result = joinPoint.proceed();//실제 타깃

    long endTime = System.currentTimeMillis();
    long executionTime = endTime - startTime;
    log.info("'{}' execution time: {} ms", joinPoint.getSignature().toShortString(), executionTime);

    return result;
  }
}
