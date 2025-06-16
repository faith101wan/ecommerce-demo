package com.cw.ecommerce.web;

import com.cw.ecommerce.config.ApiResponse;
import com.cw.ecommerce.config.BusinessException;
import com.cw.ecommerce.service.ProductService;
import com.cw.ecommerce.model.dto.ProductStockDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/merchants")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * 增加新商品 或 增加库存
     * @param merchantId
     * @param dto
     * @return
     */
    @PostMapping("/{merchantId}/products/add-stock")
    public ResponseEntity<ApiResponse> addStock(
            @PathVariable String merchantId,
            @RequestBody ProductStockDTO dto) {
        // 参数校验
        if (Objects.equals(merchantId, dto.getMerchantId())) {
            throw new BusinessException("路径商家ID与请求体不匹配");
        }
        productService.addStock(dto);
        return ResponseEntity.ok(ApiResponse.success("库存更新成功"));
    }
}