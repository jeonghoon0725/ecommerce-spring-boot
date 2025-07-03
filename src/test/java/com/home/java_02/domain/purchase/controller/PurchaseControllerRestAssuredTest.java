package com.home.java_02.domain.purchase.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PurchaseControllerRestAssuredTest {

  @LocalServerPort
  private int port;

  @BeforeEach
  void setup() {
    RestAssured.port = port;

  }

  @Test
  void 주문_성공() {
    // given
    String requestBody = """
        {
            "userId": 1,
            "products": [
                {
                    "productId": 1,
                    "quantity": 50
                }
            ]
        }
        """;

    // when & then
    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/api/purchases")
        .then().log().all()
        .statusCode(200)
        //.body("result", IsEqual.equalTo(true));
        .body("error.errorCode", IsEqual.equalTo("OUT_OF_STOCK_PRODUCT"));


  }
}
