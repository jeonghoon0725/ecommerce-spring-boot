package com.home.java_02.global.external.client;

import com.home.java_02.global.config.OpenFeignConfig;
import com.home.java_02.global.external.dto.ExternalProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "external-shop",
    url = "${external.external-shop.url}",
    configuration = OpenFeignConfig.class
)
public interface ExternalShopClient {

  // http://localhost:3001/products?page=1&size=10
  @GetMapping("/products")
  ExternalProductResponse getProducts(@RequestParam("page") Integer page,
      @RequestParam("size") Integer size);
}