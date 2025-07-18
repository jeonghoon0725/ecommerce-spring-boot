package com.home.java_02.domain.purchase.dto;

public class PurchaseSearchCondition {

  private Long customerId; // 검색할 고객 ID
  private String status;   // 검색할 구매 상태
  private int limit;       // 페이지 당 데이터 수
  private int offset;      // 건너뛸 데이터 수

  // Getters and Setters

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }
}