package com.home.java_02.domain.purchase.service;

import com.home.java_02.common.enums.PurchaseStatus;
import com.home.java_02.common.exception.ServiceException;
import com.home.java_02.common.exception.ServiceExceptionCode;
import com.home.java_02.domain.product.entity.Product;
import com.home.java_02.domain.product.repository.ProductRepository;
import com.home.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.home.java_02.domain.purchase.entity.Purchase;
import com.home.java_02.domain.purchase.entity.PurchaseProduct;
import com.home.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.home.java_02.domain.purchase.repository.PurchaseRepository;
import com.home.java_02.domain.user.entity.User;
import com.home.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseProcessService {

  private final PurchaseRepository purchaseRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final PurchaseProductRepository purchaseProductRepository;

  @Transactional
  public void process(User user, List<PurchaseProductRequest> purchaseItems) {
    Purchase purchase = savePurchase(user);
    List<PurchaseProduct> purchaseProducts = createPurchaseProducts(purchaseItems, purchase);

    BigDecimal totalPrice = calculateTotalPrice(purchaseProducts);

    purchase.setTotalPrice(totalPrice);
  }

  //내부 동작들
  public Purchase savePurchase(User user) {
    return purchaseRepository.save(Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.ZERO)
        .status(PurchaseStatus.PENDING)
        .build());
  }

  public List<PurchaseProduct> createPurchaseProducts(
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

  public BigDecimal calculateTotalPrice(List<PurchaseProduct> purchaseProducts) {
    return purchaseProducts.stream()
        .map(purchaseProduct -> purchaseProduct.getPrice()
            .multiply(BigDecimal.valueOf(purchaseProduct.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}