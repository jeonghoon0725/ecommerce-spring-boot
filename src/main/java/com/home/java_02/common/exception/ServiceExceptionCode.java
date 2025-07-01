package com.home.java_02.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ServiceExceptionCode {

  NOT_FOUND_DATA("데이터를 찾을수 없습니다."),
  NOT_FOUND_USER("유저를 찾을 수 없습니다."),
  OUT_OF_STOCK_PRODUCT("주문 가능 수량을 초과하였습니다."),
  INSUFFICIENT_STOCK("재고가 부족합니다."),
  NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다."),
  NOT_FOUND_PURCHASE("구매내역을 찾을수 없습니다."),
  CANNOT_CANCEL("취소 불가능합니다.");

  final String message;
}