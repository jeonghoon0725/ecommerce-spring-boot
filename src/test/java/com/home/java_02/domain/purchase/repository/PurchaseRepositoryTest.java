package com.home.java_02.domain.purchase.repository;

import com.home.java_02.common.enums.PurchaseStatus;
import com.home.java_02.domain.purchase.entity.Purchase;
import com.home.java_02.domain.user.entity.User;
import com.home.java_02.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
class PurchaseRepositoryTest {

  @Autowired
  private PurchaseRepository purchaseRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void 추가() {
    //User user = userRepository.findById(1L).get();
    User user = userRepository.save(User.builder()
        .name("d")
        .email("d")
        .passwordHash("d")
        .build());

    Purchase purchase = Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase = purchaseRepository.save(purchase);

    //List<Purchase> purchases = new ArrayList<>();
    //purchaseRepository.saveAll(purchases);
  }

  @Test
  void 수정() {
    User user = userRepository.save(User.builder()
        .name("d1")
        .email("d1")
        .passwordHash("d1")
        .build());

    Purchase purchase = Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase = purchaseRepository.save(purchase);

    savePurchase.setStatus(PurchaseStatus.COMPLETED);
    purchaseRepository.save(savePurchase);
  }

  @Test
  void 삭제() {
    User user = userRepository.save(User.builder()
        .name("d3")
        .email("d3")
        .passwordHash("d3")
        .build());

    Purchase purchase = Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase = purchaseRepository.save(purchase);

    purchaseRepository.delete(savePurchase);

    List<Purchase> purchases = new ArrayList<>();
    purchaseRepository.deleteAll(purchases);
  }

  @Test
  void 조회() {
    List<Purchase> purchases = purchaseRepository.findAll();

    System.out.println("결과: " + purchases.get(0).getId());

    Purchase purchase = purchaseRepository.findById(1l)
        .orElseThrow(() -> new RuntimeException("주문내역이 없음"));

    System.out.println("결과: " + purchase.getTotalPrice());
  }

  @Test
  void 조회2() {
    userRepository.findAll();
  }

  @Test
  void 조회3() {
    userRepository.findAllWithPurchases();
  }

  @Test
  void 조회4() {
    List<Purchase> purchases = purchaseRepository.findAll();

    
  }
}