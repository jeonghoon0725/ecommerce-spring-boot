package com.home.java_02.domain.user.service;

import com.home.java_02.common.exception.ServiceException;
import com.home.java_02.common.exception.ServiceExceptionCode;
import com.home.java_02.domain.user.Mapper.UserMapper;
import com.home.java_02.domain.user.dto.UserCreateRequest;
import com.home.java_02.domain.user.dto.UserResponse;
import com.home.java_02.domain.user.dto.UserSearchResponse;
import com.home.java_02.domain.user.dto.UserUpdateRequest;
import com.home.java_02.domain.user.entity.User;
import com.home.java_02.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper; //mapstruct 사용

  public void save() {

  }

  @Transactional
  public UserResponse createUser(UserCreateRequest request) {
    return userMapper.toResponse(
        userRepository.save(User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .build())
    );
  }

  public List<UserSearchResponse> searchUser() {
    //    return userRepository.findAll().stream()
//        .map((user) -> UserSearchResponse.builder()
//            .id(user.getId())
//            .name(user.getName())
//            .email(user.getEmail())
//            .createdAt(user.getCreatedAt())
//            .build())
//        .toList();
    //위와 같이 일일이 매칭할필요없이 아래 mapstruct 활요하여 간략히 하고 비즈니스에 집중할수있음
    return userRepository.findAll().stream()
        // 자바 람다식문법으로 userMapper.toSearch() 동일, 단, 파라미터가 단일일때만 사용가능, 멀티면 불가능
        .map(userMapper::toSearch)
        .toList();
  }

  @Transactional
  public UserResponse getUserById(Long userId) {
//    User user = getUser(userId);
//
//    return UserResponse.builder()
//        .id(user.getId())
//        .name(user.getName())
//        .email(user.getEmail())
//        .createdAt(user.getCreatedAt())
//        .build();

    //위와 같이 일일이 매칭할필요없이 아래 mapstruct 활요하여 간략히 하고 비즈니스에 집중할수있음
    return userMapper.toResponse(getUser(userId));
  }

  @Transactional
  public void create(UserCreateRequest request) {
    //필드가 적으면 모를까 여러개일경우 일일이 매핑하면 시간오래걸려 mapstructs 활용하면 좋음
//    userRepository.save(User.builder()
//        .name(request.getName())
//        .email(request.getEmail())
//        .passwordHash(request.getPassword())
//        .build());

    //위와 같이 일일이 매칭할필요없이 아래 mapstruct 활요하여 간략히 하고 비즈니스에 집중할수있음
    userRepository.save(userMapper.toEntity(request));
  }

  @Transactional
  public void update(Long userId, UserUpdateRequest request) {
    User user = getUser(userId);

    user.setName(request.getName());
    user.setEmail(request.getEmail());

    userRepository.save(user);

  }

  @Transactional
  public void delete(Long userId) {
    userRepository.delete(getUser(userId));
  }

  // 변경수행 전 대상조회하는 로직을 공통함수화
  private User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
  }
}
