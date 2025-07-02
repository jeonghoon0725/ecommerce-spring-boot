package com.home.java_02.domain.purchase.service;

import com.home.java_02.common.exception.ServiceException;
import com.home.java_02.common.exception.ServiceExceptionCode;
import com.home.java_02.domain.product.entity.Product;
import com.home.java_02.domain.product.repository.ProductRepository;
import com.home.java_02.domain.purchase.dto.PurchaseCancelRequest;
import com.home.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.home.java_02.domain.purchase.dto.PurchaseRequest;
import com.home.java_02.domain.purchase.entity.Purchase;
import com.home.java_02.domain.purchase.entity.PurchaseProduct;
import com.home.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.home.java_02.domain.purchase.repository.PurchaseRepository;
import com.home.java_02.domain.user.entity.User;
import com.home.java_02.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
  private final PurchaseProcessService purchaseProcessService;
  private final PurchaseCancelService purchaseCancelService;


  //리팩토링 후 (단일 책임 원칙, SRP 적용 + 퍼사드 패턴)
  @Transactional
  public void processPurchase(@Valid PurchaseRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    purchaseProcessService.process(user, request.getPurchaseProducts());
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

    for (PurchaseProductRequest productRequest : request.getPurchaseProducts()) {
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
