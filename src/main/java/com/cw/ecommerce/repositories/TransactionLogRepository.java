package com.cw.ecommerce.repositories;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cw.ecommerce.model.TransactionLog;
import com.cw.ecommerce.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TransactionLogRepository extends BaseMapper<TransactionLog> {

    @Select("SELECT IFNULL(SUM(amount), 0) FROM transaction_log tl " +
            "JOIN product p ON tl.product_id = p.id " +
            "WHERE p.merchant_id = #{merchantId} " +
            "AND tl.status = 'SUCCESS' " +
            "AND tl.created_at BETWEEN #{start} AND #{end} " +
            "AND tl.deleted = 0")
    BigDecimal sumSalesByMerchant(@Param("merchantId") String merchantId,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);

    @Select("SELECT product_id, SUM(quantity) as total " +
            "FROM transaction_log " +
            "WHERE merchant_id = #{merchantId} " +
            "AND status = 'SUCCESS' " +
            "AND created_at BETWEEN #{start} AND #{end} " +
            "AND deleted = 0 " +
            "GROUP BY product_id")
    List<Map<String, Object>> sumProductSales(@Param("merchantId") String merchantId,
                                              @Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

}
