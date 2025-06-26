package com.home.java_02.domain.user.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

  // mapstruct는 반드시 필드명이 서로 일치해야 매핑되니 주의해라
  Long id;

  String name;

  String email;

  LocalDateTime createdAt;


}