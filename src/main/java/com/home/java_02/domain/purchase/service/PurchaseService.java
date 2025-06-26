package com.home.java_02.domain.purchase.service;

import com.home.java_02.domain.product.repository.ProductRepository;
import com.home.java_02.domain.purchase.dto.PurchaseRequest;
import com.home.java_02.domain.purchase.repository.PurchaseRepository;
import com.home.java_02.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  private final PurchaseRepository purchaseRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;


  public void placePurchase(@Valid PurchaseRequest request) {
    
  }
}
