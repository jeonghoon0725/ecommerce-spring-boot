package com.home.java_02.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

  Long id;

  String name;
  String description;
  Long categoryId;

  BigDecimal price;
  Integer stock;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
  LocalDateTime createdAt;
}
