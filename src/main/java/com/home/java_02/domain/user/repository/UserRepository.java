package com.home.java_02.domain.user.repository;

import com.home.java_02.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findFirstByNameAndEmail(String name, String email);
  //find는 있을수도 없을수도 -> 옵셔널로

  //jpql  -> queryDSL
  @Query("SELECT u FROM User u WHERE u.name = :name AND u.email = :email")
  Optional<User> findUser(String name, String email);

  Optional<User> findUserByStatus(String status);

  @Query("SELECT u FROM User u JOIN FETCH u.purchases")
  List<User> findAllWithPurchases();

}
