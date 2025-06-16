package com.cw.ecommerce.web;

import com.cw.ecommerce.config.ApiResponse;
import com.cw.ecommerce.model.User;
import com.cw.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户充值
     * @param userId
     * @param amount
     * @return
     */
    @PostMapping("/{userId}/balance")
    public ResponseEntity<ApiResponse<String>> balance(
            @PathVariable String userId,
            @RequestParam("amount")  BigDecimal amount) {
        userService.balance(userId, amount);
        return ResponseEntity.ok(ApiResponse.success("充值成功"));
    }

    // 查询用户信息
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable("userId") String userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
}