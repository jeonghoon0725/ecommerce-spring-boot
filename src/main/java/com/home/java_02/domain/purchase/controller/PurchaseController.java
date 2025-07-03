package com.home.java_02.domain.purchase.controller;

import com.home.java_02.common.annotation.Loggable;
import com.home.java_02.common.response.ApiResponse;
import com.home.java_02.domain.purchase.dto.PurchaseRequest;
import com.home.java_02.domain.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

  private final PurchaseService purchaseService;

  public ApiResponse<Void> create(@Valid @RequestBody PurchaseRequest request) {
    purchaseService.createPurchase(request);
    return ApiResponse.success();
  }
  
  @Loggable
  @PostMapping
  public ApiResponse<Void> savePurchase(@Valid @RequestBody PurchaseRequest request) {
    purchaseService.processPurchase(request);
    return ApiResponse.success();
  }
}