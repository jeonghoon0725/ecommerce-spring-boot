package com.home.java_02.domain.product.service;

import com.home.java_02.domain.product.entity.Product;
import com.home.java_02.domain.product.repository.ProductRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootTest
class ProductLockServiceTest {

  @Autowired
  private ProductLockService productLockService;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  TransactionTemplate transactionTemplate;

  @Test
  public void updateStockWithPessimisticLock() throws InterruptedException {
    //given
    Long productId = 1L;
    int threadCount = 2;

    Product firstProduct = productRepository.findById(productId).orElseThrow();

    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    //when
    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          productLockService.updateStockWithPessimisticLock(productId, 1);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await(); // 모든 스레드가 작업을 마칠 때까지 대기

    //then
    Product product = productRepository.findById(productId).orElseThrow();
    Assertions.assertThat(product.getStock()).isEqualTo(firstProduct.getStock() - 2);
  }

  @Test
  public void updateStockWithOptimisticLock() throws InterruptedException {
    //given
    Long productId = 1L;
    int threadCount = 2;

    // 테스트 시작 전의 초기 상태를 별도로 저장
    Product initialProduct = productRepository.findById(productId).orElseThrow();
    int initialStock = initialProduct.getStock(); // 예: 16

    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          productLockService.updateStockWithOptimisticLock(productId, 1);
        } catch (Exception e) {
          // OptimisticLockException 또는 다른 예외가 발생하여 하나의 스레드는 실패할 것으로 예상
          System.out.println("예상된 충돌 발생: " + e.getMessage());
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();

    // 새로운 트랜잭션에서 최종 결과를 조회
    Integer finalStock = transactionTemplate.execute(status -> {
      Product productAfterUpdate = productRepository.findById(productId).orElseThrow();
      return productAfterUpdate.getStock();
    });

    // then
    // 하나의 트랜잭션만 성공했으므로 재고는 1만 감소해야 함
    Assertions.assertThat(finalStock).isEqualTo(initialStock - 1);
  }
}