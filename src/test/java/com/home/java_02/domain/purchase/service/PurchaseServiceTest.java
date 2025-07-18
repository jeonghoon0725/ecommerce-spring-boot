package com.home.java_02.domain.purchase.service;

import com.home.java_02.domain.purchase.dto.PurchaseSearchCondition;
import com.home.java_02.domain.purchase.repository.PurchaseSqlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PurchaseServiceTest {

  @Autowired
  private PurchaseSqlMapper purchaseSqlMapper;

  @Test
  void findTopSpendingCustomers() {
    PurchaseSearchCondition condition = new PurchaseSearchCondition();

  }
}