package com.home.java_02.global.external.client;

import com.home.java_02.domain.product.service.ProductExternalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ExternalShopClientTest {

  @Autowired
  private ProductExternalService productExternalService;

  @Test
  void save() {
    productExternalService.save();
  }
}