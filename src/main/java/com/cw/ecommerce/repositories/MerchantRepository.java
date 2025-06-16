package com.cw.ecommerce.repositories;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cw.ecommerce.model.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface MerchantRepository extends BaseMapper<Merchant> {


    @Update("UPDATE merchant SET balance = balance + #{totalPrice} " +
            "WHERE merchant_id = #{merchantId} and balance + + #{totalPrice} >= 0")
    int updateBalance(@Param("merchantId") String merchantId, @Param("totalPrice") BigDecimal totalPrice);


}
