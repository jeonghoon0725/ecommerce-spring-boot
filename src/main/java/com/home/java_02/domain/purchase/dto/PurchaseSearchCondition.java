package com.home.java_02.domain.purchase.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseSearchCondition {

  Long customerId; // 검색할 고객 ID
  String status;   // 검색할 구매 상태
  int limit;       // 페이지 당 데이터 수
  int offset;      // 건너뛸 데이터 수

  // Getters and Setters
}