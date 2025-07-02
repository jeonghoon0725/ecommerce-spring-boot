package com.home.java_02.domain.purchase.dto;

import com.home.java_02.common.enums.PurchaseStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseCancelResponse {

  Long purchaseId;

  Long userId;

  PurchaseStatus status;

  LocalDateTime canceledAt;

  String message;

  List<PurchaseProductResponse> canceledProducts;
}
