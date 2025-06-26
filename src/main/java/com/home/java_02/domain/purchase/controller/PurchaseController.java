package com.home.java_02.domain.purchase.controller;

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

  @PostMapping
  public ApiResponse<Void> placePurchase(@Valid @RequestBody PurchaseRequest request) {
    purchaseService.placePurchase(request);
    return ApiResponse.success();
  }
}