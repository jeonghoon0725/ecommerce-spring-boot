package com.home.java_02.domain.purchase.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlySalesDto {

  
  String salesMonth;
  Double totalSales;
}
