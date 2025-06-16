package com.cw.ecommerce.web;

import com.cw.ecommerce.config.ApiResponse;
import com.cw.ecommerce.model.request.OrderRequest;
import com.cw.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 用户下单
    @PostMapping("purchase")
    public ResponseEntity<ApiResponse> placeOrder(
            @RequestBody OrderRequest request) {
        orderService.placeOrder(request);
        return ResponseEntity.ok(ApiResponse.success("下单成功"));
    }
}