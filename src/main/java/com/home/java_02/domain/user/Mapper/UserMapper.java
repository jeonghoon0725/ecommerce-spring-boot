package com.home.java_02.domain.user.Mapper;


import com.home.java_02.domain.user.dto.UserCreateRequest;
import com.home.java_02.domain.user.dto.UserResponse;
import com.home.java_02.domain.user.dto.UserSearchResponse;
import com.home.java_02.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") //spring에 빈 주입
public interface UserMapper {

  //source : 파라미터의 User 대상
  //target : return 되는 타입쪽 객체
  //역할: 필드명이 서로다를때 매핑시켜주기위함
  //@Mapping(target = "userEmail", source = "email")
  UserResponse toResponse(User user);

  UserSearchResponse toSearch(User user);

  User toEntity(UserCreateRequest request);
}