package com.home.java_02.domain.purchase.service;

import com.home.java_02.common.enums.TaskType;
import com.home.java_02.common.exception.ServiceException;
import com.home.java_02.common.exception.ServiceExceptionCode;
import com.home.java_02.domain.product.entity.Product;
import com.home.java_02.domain.product.repository.ProductRepository;
import com.home.java_02.domain.purchase.dto.MonthlySalesDto;
import com.home.java_02.domain.purchase.dto.PurchaseCancelRequest;
import com.home.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.home.java_02.domain.purchase.dto.PurchaseRequest;
import com.home.java_02.domain.purchase.dto.PurchaseSearchCondition;
import com.home.java_02.domain.purchase.entity.Purchase;
import com.home.java_02.domain.purchase.entity.PurchaseProduct;
import com.home.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.home.java_02.domain.purchase.repository.PurchaseRepository;
import com.home.java_02.domain.purchase.repository.PurchaseSqlMapper;
import com.home.java_02.domain.task.entity.TaskQueue;
import com.home.java_02.domain.task.service.TaskQueueService;
import com.home.java_02.domain.user.entity.User;
import com.home.java_02.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final PurchaseRepository purchaseRepository;
  private final ProductRepository productRepository;
  private final PurchaseProductRepository purchaseProductRepository;
  private final UserRepository userRepository;
  private final PurchaseSqlMapper purchaseSqlMapper;

  private final PurchaseProcessService purchaseProcessService;
  private final PurchaseCancelService purchaseCancelService;
  private final TaskQueueService taskQueueService;

  public void findTopSpendingUser() {
    // 1번 고객의 'COMPLETED' 상태 구매 내역 조회
    PurchaseSearchCondition condition = PurchaseSearchCondition.builder()
        .userId(1L)
        .status("COMPLETED")
        .limit(10)
        .offset(0)
        .build();

    List<Purchase> purchases = purchaseSqlMapper.findPurchasesWithPaging(condition);
  }

  public void getMonthlySalesStats() {
    Map<String, Object> dateRange = new HashMap<>();
    dateRange.put("startDate", "2025-01-01 00:00:00");
    dateRange.put("endDate", "2025-12-31 23:59:59");

    List<MonthlySalesDto> monthlySales = purchaseSqlMapper.getMonthlySalesStats(dateRange);

    System.out.println("📅 2025년 월별 매출 통계");
    monthlySales.forEach(stat ->
        System.out.println(stat.getSalesMonth() + ": " + stat.getTotalSales() + "원")
    );
  }

  @Transactional
  public void purchaseRequest(PurchaseRequest request) {
    TaskQueue taskQueue = taskQueueService.requestQueue(TaskType.PURCHASE);

    purchaseProcess(taskQueue.getId(), request);
  }

  public void purchaseProcess(Long taskQueueId, PurchaseRequest request) {
    taskQueueService.processQueueById(taskQueueId, (taskQueue) -> {
      User user = userRepository.findById(request.getUserId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

      Purchase purchase = purchaseProcessService.savePurchase(user);

      taskQueue.setEventId(purchase.getId());

      List<PurchaseProduct> purchaseProducts = purchaseProcessService.createPurchaseProducts(
          request.getProducts(), purchase);

      BigDecimal totalPrice = purchaseProcessService.calculateTotalPrice(purchaseProducts);
      purchase.setTotalPrice(totalPrice);
    });
  }

  //리팩토링 후 (단일 책임 원칙, SRP 적용 + 퍼사드 패턴)
  @Transactional
  public void processPurchase(@Valid PurchaseRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    purchaseProcessService.process(user, request.getProducts());
  }

  @Transactional
  public void cancelPurchase(@Valid PurchaseCancelRequest request) {
    purchaseCancelService.cancel(request.getPurchaseId(), request.getUserId());
  }

  //리팩토링 전
  //하나의 메서드에서 처리, 역할 별로 분리하여
  @Transactional
  public void createPurchase(PurchaseRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    Purchase purchase = purchaseRepository.save(Purchase.builder()
        .user(user)
        .build());

    BigDecimal totalPrice = BigDecimal.ZERO;
    List<PurchaseProduct> purchaseProducts = new ArrayList<>();

    for (PurchaseProductRequest productRequest : request.getProducts()) {
      Product product = productRepository.findById(productRequest.getProductId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

      if (productRequest.getQuantity() > product.getStock()) {
        throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
      }

      product.reduceStock(productRequest.getQuantity());

      PurchaseProduct purchaseProduct = PurchaseProduct.builder()
          .product(product)
          .purchase(purchase)
          .quantity(productRequest.getQuantity())
          .price(product.getPrice())
          .build();

      purchaseProducts.add(purchaseProduct);
      totalPrice = totalPrice.add(
          product.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity()))
      );
    }

    purchase.setTotalPrice(totalPrice);
    purchaseProductRepository.saveAll(purchaseProducts);
  }


}
