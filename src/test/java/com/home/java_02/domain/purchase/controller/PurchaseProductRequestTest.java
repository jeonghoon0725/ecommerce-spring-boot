package com.home.java_02.domain.purchase.controller;

class PurchaseProductRequestTest {

  Long productId;

  Integer quantity;

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public PurchaseProductRequestTest(Long productId, Integer quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }
}