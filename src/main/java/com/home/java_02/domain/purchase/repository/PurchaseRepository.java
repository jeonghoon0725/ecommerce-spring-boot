package com.home.java_02.domain.purchase.repository;

import com.home.java_02.domain.purchase.entity.Purchase;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

  Optional<Purchase> findByIdAndUser_Id(Long id, Long userId);//_ 써도 되고 안 써도 되고

  //Long user(User user);//확인필요
}