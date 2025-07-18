package com.home.java_02.domain.user.repository;

import com.home.java_02.domain.user.dto.UserRankDto;
import com.home.java_02.domain.user.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserSqlMapper {

  void getUserById(Long id);

  void insertUser(User user);

  void insertUserList(@Param("users") List<User> users);

  void updateUser(User user);

  void deleteUserById(Long id);

  List<UserRankDto> findTopSpendingUser(int topN);
}
