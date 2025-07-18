package com.home.java_02.domain.purchase.dto;

public class MonthlySales {

  private String salesMonth;
  private Double totalSales;

  public String getSalesMonth() {
    return salesMonth;
  }

  public void setSalesMonth(String salesMonth) {
    this.salesMonth = salesMonth;
  }

  public Double getTotalSales() {
    return totalSales;
  }

  public void setTotalSales(Double totalSales) {
    this.totalSales = totalSales;
  }
}
