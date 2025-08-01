package com.home.java_02.domain.product.service;

import com.home.java_02.common.exception.CustomCheckedException;
import com.home.java_02.common.exception.ServiceException;
import com.home.java_02.common.exception.ServiceExceptionCode;
import com.home.java_02.domain.product.dto.ProductResponse;
import com.home.java_02.domain.product.entity.Product;
import com.home.java_02.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional(readOnly = true)
  public List<ProductResponse> getAllProducts() {
    return productRepository.findAll().stream()
        .map(product -> ProductResponse.builder()
            .id(product.getId())
            .categoryId(product.getCategory().getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .createdAt(product.getCreatedAt())
            .build())
        .toList();
  }

  /**
   * 체크 예외(CustomCheckedException)가 발생하면, rollbackFor에 의해 트랜잭션 롤백 가능 / Exception으로 쓰는 것보다 조금 더
   * 명시적으로
   */
  @Transactional(rollbackFor = CustomCheckedException.class)
  public void updateProductStock(Long productId, Integer stock) throws CustomCheckedException {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    log.info("상품 재고를 {}에서 {}로 변경 시도.", product.getPrice(), stock);
    product.setStock(stock);
    productRepository.save(product); // 변경 사항을 우선 DB에 반영

    // 예외 발생 조건: 음수 가격은 허용하지 않음 (체크 예외)
    if (stock < 0) {
      throw new CustomCheckedException("재고는 음수가 될 수 없습니다.");
    }

    //catch문으로 작성 시 그냥 롤백되지 않음. catch 블록에서 예외를 다시 던지거나 TransactionAspectSupport ... rollback 작성
  }

  /**
   * 언체크 예외(IllegalArgumentException)가 발생해도 noRollbackFor 설정 때문에 트랜잭션 롤백 안 함
   */
  @Transactional(noRollbackFor = IllegalArgumentException.class)
  public void reduceProductStockNoRollback(Long productId, int quantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    // 예외 발생 전 다른 DB 작업을 수행했다고 가정
    // ex) logRepository.save 재고 차감 시도 ...

    // 재고 부족 시 IllegalArgumentException 발생 (언체크 예외)
    if (product.getStock() < quantity) {
      throw new IllegalArgumentException("재고가 부족합니다.");
    }

    // 위의 throw 로 인해 재고 차감 x
    product.reduceStock(quantity);
    productRepository.save(product);
  }
}
