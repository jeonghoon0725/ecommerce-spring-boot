package com.home.java_02;

import com.home.java_02.domain.user.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 10) // Redis를 세션 저장소로 사용하도록 활성화 (10분)
public class Java02Application {

  public static void main(String[] args) {
    SpringApplication.run(Java02Application.class, args);

    //User user = new User("name1", "email1", "password1");

    //save(user); // insert 쿼리 실행

    //user.setName("name2");
    //save(user); // update 쿼리 실행

    //insert 생성자로 만듦
    //update Setter 로 값 변경

    User user1 = User.builder()
        .name("name3")
        .email("email3")
        .build();
  }

}
