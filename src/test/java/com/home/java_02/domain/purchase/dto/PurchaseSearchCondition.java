package com.home.java_02.domain.purchase.dto;

public class PurchaseSearchCondition {

  private Long userId; // 검색할 고객 ID
  private String status;   // 검색할 구매 상태
  private int limit;       // 페이지 당 데이터 수
  private int offset;      // 건너뛸 데이터 수

  // Getters and Setters
  public PurchaseSearchCondition(Long userId, String status, int limit, int offset) {
    this.userId = userId;
    this.status = status;
    this.limit = limit;
    this.offset = offset;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
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