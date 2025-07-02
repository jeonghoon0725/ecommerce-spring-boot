package com.home.java_02.common.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Constants {

  public static String PURCHASE_CANCEL_MESSAGE = "구매가 정상적으로 취소되었습니다.";


}