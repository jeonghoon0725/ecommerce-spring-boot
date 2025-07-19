package com.home.java_02.domain.purchase.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseDto {

  private Long id;       // purchase_id
  private Long userId;           // user_id
  private BigDecimal totalPrice; // total_price
  private String status;         // status
  private LocalDateTime createdAt; // created_at
}