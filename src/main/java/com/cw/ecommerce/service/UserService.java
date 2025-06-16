package com.cw.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cw.ecommerce.common.enumerate.Status;
import com.cw.ecommerce.model.User;
import com.cw.ecommerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public void balance(String userId, BigDecimal amount) {
        userRepository.updateBalance(userId, amount.abs());
    }

    public User getUserById(String userId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("status", Status.ACTIVE.getCode());
        User user = userRepository.selectOne(wrapper);
        return user;


    }


}