package com.home.java_02.domain.category.controller;

import com.home.java_02.common.response.ApiResponse;
import com.home.java_02.domain.category.dto.CategoryRequest;
import com.home.java_02.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  public ApiResponse<Void> save(@Valid @RequestBody CategoryRequest request) {
    return ApiResponse.success();
  }

}