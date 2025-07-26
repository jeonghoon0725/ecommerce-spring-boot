package com.home.java_02.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum TaskStatus {
  PENDING, // 작업 대기, 미처리 상태
  PROCESSING, // 작업 중, 다른 트랜잭션이 접근하지 못 하게
  COMPLETED, // 작업 완료
  ;
}