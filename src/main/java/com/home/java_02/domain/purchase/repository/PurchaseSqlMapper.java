package com.home.java_02.domain.purchase.repository;

import com.home.java_02.domain.purchase.dto.MonthlySalesDto;
import com.home.java_02.domain.purchase.dto.PurchaseSearchCondition;
import com.home.java_02.domain.purchase.entity.Purchase;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PurchaseSqlMapper {

  List<MonthlySalesDto> getMonthlySalesStats(Map<String, Object> map);

  List<Purchase> findPurchasesWithPaging(PurchaseSearchCondition condition);
}
