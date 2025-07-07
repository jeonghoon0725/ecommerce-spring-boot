package com.home.java_02.domain.auth.controller;

import com.home.java_02.common.exception.ServiceException;
import com.home.java_02.common.exception.ServiceExceptionCode;
import com.home.java_02.common.response.ApiResponse;
import com.home.java_02.domain.auth.dto.LoginRequest;
import com.home.java_02.domain.auth.dto.LoginResponse;
import com.home.java_02.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ApiResponse<LoginResponse> login(HttpSession httpSession,
      @Valid @RequestBody LoginRequest loginRequest) {
    LoginResponse loginResponse = authService.login(loginRequest);

    httpSession.setAttribute("userId", loginResponse.getUserId());
    httpSession.setAttribute("name", loginResponse.getName());
    httpSession.setAttribute("email", loginResponse.getEmail());

    log.info("session id : {}", httpSession.getId());

    return ApiResponse.success(loginResponse);
  }

  @GetMapping("/status")
  public ApiResponse<LoginResponse> checkStatus(HttpSession httpSession) {
    Long userId = (Long) httpSession.getAttribute("userId");
    String email = (String) httpSession.getAttribute("email");

    if (ObjectUtils.isEmpty(userId) && ObjectUtils.isEmpty(email)) {
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_USER);
    }

    return ApiResponse.success(authService.getLoginResponse(userId, email));
  }

  @GetMapping("/logout")
  public ApiResponse<Void> logout(HttpSession httpSession) {
    httpSession.invalidate();
    return ApiResponse.success();
  }
}