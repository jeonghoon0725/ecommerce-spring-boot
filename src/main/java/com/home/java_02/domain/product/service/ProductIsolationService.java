package com.home.java_02.domain.product.service;

import com.home.java_02.domain.category.entity.Category;
import com.home.java_02.domain.category.repository.CategoryRepository;
import com.home.java_02.domain.product.entity.Product;
import com.home.java_02.domain.product.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
@AllArgsConstructor
public class ProductIsolationService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  private final EntityManager entityManager;

  @Transactional
  public void updateStockAndForceRollback(Long productId, int newStock) {

    Product product = productRepository.findById(productId).orElseThrow();
    log.info("Thread A: 재고를 " + product.getStock() + "에서 " + newStock + "으로 변경 시도");
    product.setStock(newStock);

    productRepository.saveAndFlush(product); // 영속성 뿐만 아니라 DB 세션에도 반영하기 위해 (트랜잭션 중)

    try {
      Thread.sleep(5000); // 다른 트랜잭션이 변경 자료에 접근할 시간 대기
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    log.info("Thread A: 작업을 롤백합니다.");
    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
  }

  // Dirty Read를 허용하는 메서드 (격리 수준: READ_UNCOMMITTED)
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public int getStockWithDirtyRead(Long productId) {
    log.info("Thread B: READ_UNCOMMITTED 트랜잭션에서 재고 조회 시도");
    Product product = productRepository.findById(productId).orElseThrow();
    return product.getStock();
  }

  // Dirty Read를 방지하는 메서드 (격리 수준: READ_COMMITTED)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public int getStockWithReadCommitted(Long productId) {
    log.info("Thread B: READ_COMMITTED 트랜잭션에서 재고 조회 시도");
    Product product = productRepository.findById(productId).orElseThrow();
    return product.getStock();
  }

  /**
   * Non-Repeatable Read를 재현하는 메서드 (격리 수준: READ_COMMITTED)
   */
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public void demonstrateNonRepeatableRead(Long productId) {
    // 1. 첫 번째 데이터 조회
    Product product1 = productRepository.findById(productId).orElseThrow();
    log.info("Thread A - First Read: Stock = {}", product1.getStock());

    // 2. 다른 트랜잭션이 데이터를 변경할 시간을 줌
    try {
      Thread.sleep(4000);
      entityManager.clear();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // 3. 동일 트랜잭션 내에서 데이터 다시 조회
    Product product2 = productRepository.findById(productId).orElseThrow();
    log.info("Thread A - Second Read: Stock = {}", product2.getStock());

    // 4. 두 조회 결과가 다른지 확인
    if (product1.getStock() != product2.getStock()) {
      log.info(">>>> Non-Repeatable Read가 발생했습니다!");
    }
  }

  /**
   * Non-Repeatable Read를 방지하는 메서드 (격리 수준: REPEATABLE_READ)
   */
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void demonstrateRepeatableRead(Long productId) {
    // 위와 로직은 동일하지만, 격리 수준만 다름
    Product product1 = productRepository.findById(productId).orElseThrow();
    log.info("Thread A - First Read: Stock = {}", product1.getStock());

    try {
      Thread.sleep(4000);
      entityManager.clear();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    Product product2 = productRepository.findById(productId).orElseThrow();
    log.info("Thread A - Second Read: Stock = {}", product2.getStock());

    if (Objects.equals(product1.getStock(), product2.getStock())) {
      log.info(">>>> Repeatable Read가 보장되었습니다!");
    }
  }

  @Transactional
  public void updateStock(Long productId, int newStock) {
    Product product = productRepository.findById(productId).orElseThrow();
    log.info("Thread B: 재고를 {} 으로 변경하고 커밋합니다.", newStock);
    product.setStock(newStock);
  }// 메서드가 종료되면서 변경 사항이 COMMIT됨

  /**
   * Phantom Read를 재현하는 메서드 (격리 수준: REPEATABLE_READ)
   */
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public void demonstratePhantomRead() {
    // 1. 첫 번째 범위 조회
    List<Product> products1 = productRepository.findAllByStockGreaterThan(5);
    log.info("Thread A - First Read: " + products1.size() + " products found.");

    // 2. 다른 트랜잭션이 데이터를 INSERT할 시간을 줌
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // 3. 동일 트랜잭션 내에서 다시 범위 조회
    List<Product> products2 = productRepository.findAllByStockGreaterThan(5);
    log.info("Thread A - Second Read: " + products2.size() + " products found.");

    if (products1.size() != products2.size()) {
      log.info(">>>> Phantom Read가 발생했습니다!");
    }
  }

  /**
   * Phantom Read를 방지하는 메서드 (격리 수준: SERIALIZABLE)
   */
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public void demonstrateSerializable() {
    // 위와 로직은 동일하지만, 격리 수준만 다름
    List<Product> products1 = productRepository.findAllByStockGreaterThan(5);
    log.info("Thread A - First Read: {} products found.", products1.size());

    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    List<Product> products2 = productRepository.findAllByStockGreaterThan(5);
    log.info("Thread A - Second Read: " + products2.size() + " products found.");

    if (products1.size() == products2.size()) {
      log.info(">>>> Phantom Read가 방지되었습니다!");
    }
  }

  /**
   * 조건에 맞는 신상품을 추가하고 즉시 커밋하는 메서드
   */
  @Transactional
  public void insertNewProduct(String name, int stock) {
    log.info("Thread B: 재고(" + stock + ")를 가진 신상품 추가 시도");

    Category category = categoryRepository.findById(1L).orElseThrow();

    Product product = Product.builder()
        .category(category)
        .name(name)
        .price(BigDecimal.valueOf(1000))
        .stock(stock)
        .build();
    productRepository.save(product);

    log.info("Thread B: 신상품 추가 및 커밋 완료");
  }
  
}
