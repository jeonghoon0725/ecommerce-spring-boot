package com.home.java_02.domain.purchase.service;

import com.home.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.home.java_02.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseCancelService {

  @Transactional
  public void cancel(User user, List<PurchaseProductRequest> purchaseItems) {
    //!@#$
  }
}
