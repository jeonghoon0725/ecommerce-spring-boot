package com.home.java_02.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

  String name;

  String email;

  String password;

  /**
   * { "name" : "홍길동", "email" : "test@naver.com", ...}
   */


}
