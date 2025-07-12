package com.home.java_02.domain.auth.service;

import com.home.java_02.common.exception.ServiceException;
import com.home.java_02.common.exception.ServiceExceptionCode;
import com.home.java_02.domain.auth.dto.LoginRequest;
import com.home.java_02.domain.auth.dto.LoginResponse;
import com.home.java_02.domain.user.entity.User;
import com.home.java_02.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public LoginResponse login(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_USER);
    }

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);//레디스 키 삽입

    return LoginResponse.builder()
        .userId(user.getId())
        .email(loginRequest.getEmail())
        .build();
  }

  public LoginResponse getLoginResponse(Long userId, String email) {
    return LoginResponse.builder()
        .userId(userId)
        .email(email)
        .build();
  }

  public void logout() {
    SecurityContextHolder.clearContext();
  }
}