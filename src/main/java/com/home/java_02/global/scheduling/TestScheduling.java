package com.home.java_02.global.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestScheduling {

  @Scheduled(fixedRate = 5000) //시작시간 기준
  @Scheduled(fixedDelay = 5000) //종료시간 기준
  @Scheduled(cron = "0 30 9 ? * MON-FRI") //초 분 시 일 월 요일
  public void batchUser() {

  }
}
