package com.cw.ecommerce.repositories;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cw.ecommerce.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends BaseMapper<User> {


    @Update("UPDATE user SET balance = balance + #{totalPrice} " +
            "WHERE user_id = #{userId} AND balance +  #{totalPrice} >= 0 ")
    int updateBalance(@Param("userId") String userId, @Param("totalPrice") BigDecimal totalPrice);

}
