package com.home.java_02.domain.purchase.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseRequest {

  Long id;

  Long productId;

  int quantity;

  BigDecimal totalPrice;

  LocalDateTime createdAt;
}
