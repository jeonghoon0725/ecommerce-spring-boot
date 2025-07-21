package com.home.java_02.domain.category.controller;

import com.home.java_02.common.response.ApiResponse;
import com.home.java_02.domain.category.dto.CategoryRequest;
import com.home.java_02.domain.category.service.CategoryJdbcService;
import com.home.java_02.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryJdbcService categoryJdbcService;

  @PostMapping
  public ApiResponse<Void> save(@Valid @RequestBody CategoryRequest request) {
    return ApiResponse.success();
  }

  @PatchMapping("/{id}/name")
  public ApiResponse<JSONObject> updateByName(@RequestParam Long id,
      @RequestBody CategoryRequest request) throws SQLException {
    categoryJdbcService.updateCategory(id, request.getName());
    return ApiResponse.success(new JSONObject());
  }

}