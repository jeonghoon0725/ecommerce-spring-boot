package com.home.java_02.domain.purchase.service;

import com.home.java_02.domain.purchase.dto.MonthlySalesDto;
import com.home.java_02.domain.purchase.dto.PurchaseSearchCondition;
import com.home.java_02.domain.purchase.entity.Purchase;
import com.home.java_02.domain.purchase.repository.PurchaseSqlMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PurchaseServiceTest {

  @Autowired
  private PurchaseSqlMapper purchaseSqlMapper;

  @Test
  void findTopSpendingCustomers() {
    PurchaseSearchCondition condition = new PurchaseSearchCondition(1L, "COMPLETED", 10, 0);
    List<Purchase> purchases = purchaseSqlMapper.findPurchasesWithPaging(condition);
    for (Purchase purchase : purchases) {
      System.out.println(purchase);
    }
  }

  @Test
  void getMonthlySalesStats() {
    Map<String, Object> dateRange = new HashMap<>();
    dateRange.put("startDate", "2025-01-01 00:00:00");
    dateRange.put("endDate", "2025-12-31 23:59:59");

    List<MonthlySalesDto> monthlySales = purchaseSqlMapper.getMonthlySalesStats(dateRange);

    System.out.println("📅 2025년 월별 매출 통계");
    monthlySales.forEach(stat ->
        System.out.println(stat.getSalesMonth() + ": " + stat.getTotalSales() + "원")
    );
  }
}