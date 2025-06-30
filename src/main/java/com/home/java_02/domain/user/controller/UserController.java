package com.home.java_02.domain.user.controller;

import com.home.java_02.common.annotation.Loggable;
import com.home.java_02.common.response.ApiResponse;
import com.home.java_02.domain.user.dto.UserCreateRequest;
import com.home.java_02.domain.user.dto.UserRequest;
import com.home.java_02.domain.user.dto.UserResponse;
import com.home.java_02.domain.user.dto.UserSearchResponse;
import com.home.java_02.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor//final 붙은 게 있으면 생성자 만들어줌
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @Loggable
  @GetMapping
  public ApiResponse<List<UserSearchResponse>> findAll() {
    // GET /api/users/12?email="test@naver.com"

    //return ResponseEntity.status(200).build();

    //return ResponseEntity.status(200).body(new HashMap<String, String>() {{
    //put("name", "홍길동");
    //}});

    //return ResponseEntity.status(200).body(UserSearchResponse.builder().build());

    //return ResponseEntity.ok().body(UserSearchResponse.builder().build());//200

    return ApiResponse.success(userService.searchUser());

  }

  @GetMapping("/{userId}")
  public ApiResponse<UserResponse> findById(@PathVariable Long userId) {
    return ApiResponse.success(userService.getUserById(userId));
  }
  
  /*@GetMapping("/{userId}")
  public ApiResponse<UserSearchResponse> findById(
      @RequestParam(required = false) String email,
      @PathVariable Long userId
  ) {
    // GET /api/users/12?email="test@naver.com"

    //return ResponseEntity.status(200).build();

    //return ResponseEntity.status(200).body(new HashMap<String, String>() {{
    //put("name", "홍길동");
    //}});

    //return ResponseEntity.status(200).body(UserSearchResponse.builder().build());

    //return ResponseEntity.ok().body(UserSearchResponse.builder().build());//200

    return ApiResponse.success(userService.searchUser());

  }*/

  @PostMapping
  public ApiResponse<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
    userService.save();

    return ApiResponse.success(userService.createUser(request));
  }

  @PutMapping("{userId}")
  public ResponseEntity<Void> update(@PathVariable Long userId, @RequestBody UserRequest request) {
    return ResponseEntity.ok().build();
  }

  @PatchMapping("{userId}")
  public ResponseEntity<Void> updateStatus(@PathVariable Long userId) {
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<Void> delete(@PathVariable Long userId) {
    userService.delete(userId);
    return ResponseEntity.ok().build();
  }

  

/*
  private UserService userService;

  field 주입
  private UserService userService;

  setter 주입
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  //final로 안전하게
  private final UserService userService;

  //생성자 주입
  //@Autowired 이제 이것도 안 써도 됨;; UserService 여기서 해줌
  public UserController(UserService userService) {
    this.userService = userService;
  }

  public void save() {
    userService.save();
  }
*/
}
