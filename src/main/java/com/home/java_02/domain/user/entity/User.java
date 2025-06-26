package com.home.java_02.domain.user.entity;

import com.home.java_02.domain.purchase.entity.Purchase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

@Table
@Entity
@Getter
@DynamicInsert//null이 아닌 데이터 컬럼만
@DynamicUpdate//변경된 데이터 컬럼만
@NoArgsConstructor//jpa는 빈 생성자가 필요
//@AllArgsConstructor//모든 ~ 생성자
//@RequiredArgsConstructor//private final 객체만 생성자로
@FieldDefaults(level = AccessLevel.PRIVATE)//필드 자동 private
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;

  @Setter
  @Column(nullable = false, length = 50)
  String name;

  @Setter
  @Column
  String email;

  @Column
  String passwordHash;

  @Column(length = 10)
  String status;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  LocalDateTime createdAt;

  @Column
  @UpdateTimestamp
  LocalDateTime updatedAt;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  List<Purchase> purchases = new ArrayList<>();

  @Builder
  public User(String name, String email, String passwordHash) {
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
  }


}
