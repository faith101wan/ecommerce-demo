package com.cw.ecommerce.service;

import com.cw.ecommerce.config.BusinessException;
import com.cw.ecommerce.model.Product;
import com.cw.ecommerce.model.dto.ProductStockDTO;
import com.cw.ecommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * 扣减库存（原子操作）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deductStock(Long productId, int quantity) {
        if (productRepository.deductStock(productId, quantity) <= 0) {
            throw new BusinessException("库存不足或商品不存在");
        }
    }

    /**
     * 增加库存
     */
    @Transactional
    public void addStock(ProductStockDTO productStockDTO) {
        Product product = productRepository.selectBySkuAndMerchantId(productStockDTO.getSku(), productStockDTO.getMerchantId());
        if (product != null) {
            productRepository.addStock(product.getId(), productStockDTO.getQuantity());
            return;
        }
        // 添加新商品
        if (product == null &&
                (Objects.isNull(productStockDTO.getPrice())
                        || Objects.isNull(productStockDTO.getName()))) {
            throw new BusinessException("新增商品需要明确单价,名称");
        }

        Product target = new Product();
        target.setName(productStockDTO.getName());
        target.setSku(productStockDTO.getSku());
        target.setPrice(productStockDTO.getPrice());
        target.setMerchantId(productStockDTO.getMerchantId());
        target.setStock(productStockDTO.getQuantity());
        productRepository.insert(target);

        return;

    }
}