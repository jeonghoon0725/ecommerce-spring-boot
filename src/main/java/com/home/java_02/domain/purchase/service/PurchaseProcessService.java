package com.home.java_02.domain.purchase.service;

import com.home.java_02.common.enums.PurchaseStatus;
import com.home.java_02.common.exception.ServiceException;
import com.home.java_02.common.exception.ServiceExceptionCode;
import com.home.java_02.domain.product.entity.Product;
import com.home.java_02.domain.product.repository.ProductRepository;
import com.home.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.home.java_02.domain.purchase.dto.PurchaseRequest;
import com.home.java_02.domain.purchase.entity.Purchase;
import com.home.java_02.domain.purchase.entity.PurchaseProduct;
import com.home.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.home.java_02.domain.purchase.repository.PurchaseRepository;
import com.home.java_02.domain.task.service.TaskQueueService;
import com.home.java_02.domain.user.entity.User;
import com.home.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseProcessService {

  private final PurchaseRepository purchaseRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final PurchaseProductRepository purchaseProductRepository;
  private final TaskQueueService taskQueueService;

  @Async
  //@Transactional(propagation = Propagation.REQUIRES_NEW) // 항상 자신만의 새로운 트랜잭션을 시작, 부모와 별도 롤백, 혼자 롤백 가능
  @Transactional(propagation = Propagation.NESTED) //  기존 트랜잭션 안에서 서브 트랜잭션, 부모와 함께 롤백, 혼자 롤백 가능
  public void process(Long taskQueueId, PurchaseRequest request, User user) {
    taskQueueService.processQueueById(taskQueueId, (taskQueue) -> {
      Purchase purchase = savePurchase(user);

      taskQueue.setEventId(purchase.getId());

      List<PurchaseProduct> purchaseProducts = createPurchaseProducts(
          request.getProducts(),
          purchase);

      BigDecimal totalPrice = calculateTotalPrice(purchaseProducts);
      purchase.setTotalPrice(totalPrice);
    });
  }

  // REQUIRED: 항상 트랜잭션을 보장
  @Transactional(propagation = Propagation.REQUIRED)
  public void process(User user, List<PurchaseProductRequest> purchaseItems) {
    Purchase purchase = savePurchase(user);
    List<PurchaseProduct> purchaseProducts = createPurchaseProducts(purchaseItems, purchase);

    BigDecimal totalPrice = calculateTotalPrice(purchaseProducts);

    purchase.setTotalPrice(totalPrice);
  }

  //내부 동작들
  private Purchase savePurchase(User user) {
    return purchaseRepository.save(Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.ZERO)
        .status(PurchaseStatus.PENDING)
        .build());
  }

  private List<PurchaseProduct> createPurchaseProducts(
      List<PurchaseProductRequest> productRequests,
      Purchase purchase
  ) {
    List<PurchaseProduct> purchaseProducts = new ArrayList<>();

    for (PurchaseProductRequest productRequest : productRequests) {
      Product product = productRepository.findById(productRequest.getProductId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

      //재고수량 체크
      validateStock(product, productRequest.getQuantity());

      product.reduceStock(productRequest.getQuantity());

      PurchaseProduct purchaseProduct = PurchaseProduct.builder()
          .product(product)
          .purchase(purchase)
          .quantity(productRequest.getQuantity())
          .price(product.getPrice())
          .build();

      purchaseProducts.add(purchaseProduct);
    }

    purchaseProductRepository.saveAll(purchaseProducts);

    return purchaseProducts;
  }

  private void validateStock(Product product, int requestedQuantity) {
    if (requestedQuantity > product.getStock()) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }
  }

  private BigDecimal calculateTotalPrice(List<PurchaseProduct> purchaseProducts) {
    return purchaseProducts.stream()
        .map(purchaseProduct -> purchaseProduct.getPrice()
            .multiply(BigDecimal.valueOf(purchaseProduct.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}