package com.home.java_02.domain.product.service;

import com.home.java_02.common.exception.ServiceException;
import com.home.java_02.common.exception.ServiceExceptionCode;
import com.home.java_02.domain.product.entity.Product;
import com.home.java_02.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductTransactionService {

  private final PlatformTransactionManager transactionManager;
  private final ProductRepository productRepository;

  public void updateProductStock(Long productId, int quantity) {
    TransactionStatus status = transactionManager.getTransaction(
        new DefaultTransactionDefinition());

    try {
      Product product = productRepository.findById(productId)
          .orElseThrow(() -> new RuntimeException("Product not found"));

      if (product.getStock() < quantity) {
        throw new IllegalArgumentException("Insufficient stock");
      }

      product.reduceStock(quantity);
      productRepository.save(product);

      log.info("isTransaction : {}", TransactionSynchronizationManager.isActualTransactionActive());

      transactionManager.commit(status);

    } catch (Exception ex) {
      transactionManager.rollback(status);
      throw ex;
    }
  }

  @Transactional(readOnly = true)//읽기 전용, read DB write DB 이중화 되어있을 때 베리
  public Product getProduct(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
  }

  @Transactional
  public void updateProductStockTransactional(Long productId, int quantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    if (product.getStock() < quantity) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }

    product.reduceStock(quantity);

    log.info("isTransaction : {}", TransactionSynchronizationManager.isActualTransactionActive());
  }
}