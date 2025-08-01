package com.home.java_02.domain.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerTest {

  @Autowired//테스트케이스에선 심플하게 필드 주입으로
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void 주문_생성() throws Exception {
    //given
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 10));

    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(1L,
        purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(purchaseRequestTest);

    //when & then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON.toString())
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true));
  }

  @Test
  void 유저_없음_체크() throws Exception {
    //given
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 10));

    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(1000000l,
        purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(purchaseRequestTest);

    //when & then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON.toString())
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error.errorCode").value("NOT_FOUND_USER"));
  }

  @Test
  void 수량_체크() throws Exception {
    //given
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 50));

    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(1l,
        purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(purchaseRequestTest);

    //when & then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.error.errorCode").value("OUT_OF_STOCK_PRODUCT"));
  }
}