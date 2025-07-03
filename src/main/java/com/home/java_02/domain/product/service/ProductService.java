package com.home.java_02.domain.product.service;

import com.home.java_02.domain.product.dto.ProductResponse;
import com.home.java_02.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<ProductResponse> getAllProducts() {
    return productRepository.findAll().stream()
        .map(product -> ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .stock(product.getStock())
            .build()).toList();
  }
}
