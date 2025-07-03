package com.home.java_02.domain.purchase.controller;


import java.util.List;

class PurchaseRequestTest {

  Long userId;
  List<PurchaseProductRequestTest> products;

  public PurchaseRequestTest(Long userId, List<PurchaseProductRequestTest> products) {
    this.userId = userId;
    this.products = products;
  }

  public Long getUserId() {
    return userId;
  }

  public List<PurchaseProductRequestTest> getProducts() {
    return products;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setPurchaseProducts(
      List<PurchaseProductRequestTest> products) {
    this.products = products;
  }
}