package com.home.java_02.domain.product.controller;

import com.home.java_02.common.response.ApiResponse;
import com.home.java_02.domain.product.dto.ProductResponse;
import com.home.java_02.domain.product.service.ProductExternalService;
import com.home.java_02.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final ProductExternalService productExternalService;

  @GetMapping
  public ApiResponse<List<ProductResponse>> getProducts() {
    //productService.getAllProducts();
    return ApiResponse.success(productService.getAllProducts());
  }

  @GetMapping("/externalProduct")
  public ApiResponse<Void> getProductsForExternal() {
    productExternalService.saveAllExternalProducts();
    return ApiResponse.success();
  }
}
