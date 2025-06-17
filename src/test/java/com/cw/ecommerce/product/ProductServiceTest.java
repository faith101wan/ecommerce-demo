package com.cw.ecommerce.product;

import com.cw.ecommerce.config.BusinessException;
import com.cw.ecommerce.model.Product;
import com.cw.ecommerce.model.dto.ProductStockDTO;
import com.cw.ecommerce.repositories.ProductRepository;
import com.cw.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // Test data builders
    private ProductStockDTO createStockDTO(String sku, String merchantId, Integer quantity, String name, BigDecimal price) {
        ProductStockDTO dto = new ProductStockDTO();
        dto.setSku(sku);
        dto.setMerchantId(merchantId);
        dto.setQuantity(quantity);
        dto.setName(name);
        dto.setPrice(price);
        return dto;
    }

    private Product createProduct(Long id, String sku, String merchantId, String name, BigDecimal price, Integer stock) {
        Product product = new Product();
        product.setId(id);
        product.setSku(sku);
        product.setMerchantId(merchantId);
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        return product;
    }

    @Test
    @Transactional
    void deductStock_ShouldSucceed_WhenStockAvailable() {
        // Arrange
        Long productId = 1L;
        int quantity = 5;
        when(productRepository.deductStock(productId, quantity)).thenReturn(1);

        // Act
        productService.deductStock(productId, quantity);

        // Assert
        verify(productRepository, times(1)).deductStock(productId, quantity);
    }

    @Test
    @Transactional
    void deductStock_ShouldThrowException_WhenStockInsufficient() {
        // Arrange
        Long productId = 1L;
        int quantity = 100;
        when(productRepository.deductStock(productId, quantity)).thenReturn(0);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            productService.deductStock(productId, quantity);
        }, "库存不足或商品不存在");
        verify(productRepository, times(1)).deductStock(productId, quantity);
    }

    @Test
    @Transactional
    void addStock_ShouldUpdateExistingProduct_WhenProductExists() {
        // Arrange
        ProductStockDTO dto = createStockDTO("SKU123", "MERCHANT1", 10, "Existing Product", BigDecimal.valueOf(99.99));
        Product existingProduct = createProduct(1L, "SKU123", "MERCHANT1", "Existing Product", BigDecimal.valueOf(99.99), 5);
        
        when(productRepository.selectBySkuAndMerchantId("SKU123", "MERCHANT1")).thenReturn(existingProduct);
        doNothing().when(productRepository).addStock(1L, 10);

        // Act
        productService.addStock(dto);

        // Assert
        verify(productRepository, times(1)).selectBySkuAndMerchantId("SKU123", "MERCHANT1");
        verify(productRepository, times(1)).addStock(1L, 10);

    }

    @Test
    @Transactional
    void addStock_ShouldCreateNewProduct_WhenProductNotExistsWithValidData() {
        // Arrange
        ProductStockDTO dto = createStockDTO("NEWSKU", "MERCHANT1", 20, "New Product", BigDecimal.valueOf(49.99));
        when(productRepository.selectBySkuAndMerchantId("NEWSKU", "MERCHANT1")).thenReturn(null);
        when(productRepository.insert(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(2L);
            return 1;
        });

        // Act
        productService.addStock(dto);

        // Assert
        verify(productRepository, times(1)).selectBySkuAndMerchantId("NEWSKU", "MERCHANT1");
        verify(productRepository, never()).addStock(anyLong(), anyInt());

    }

    @Test
    @Transactional
    void addStock_ShouldThrowException_WhenNewProductMissingName() {
        // Arrange
        ProductStockDTO dto = createStockDTO("NEWSKU", "MERCHANT1", 20, null, BigDecimal.valueOf(49.99));
        when(productRepository.selectBySkuAndMerchantId("NEWSKU", "MERCHANT1")).thenReturn(null);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            productService.addStock(dto);
        }, "新增商品需要明确单价,名称");
        
        verify(productRepository, never()).addStock(anyLong(), anyInt());

    }

    @Test
    @Transactional
    void addStock_ShouldThrowException_WhenNewProductMissingPrice() {
        // Arrange
        ProductStockDTO dto = createStockDTO("NEWSKU", "MERCHANT1", 20, "New Product", null);
        when(productRepository.selectBySkuAndMerchantId("NEWSKU", "MERCHANT1")).thenReturn(null);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            productService.addStock(dto);
        }, "新增商品需要明确单价,名称");
        
        verify(productRepository, never()).addStock(anyLong(), anyInt());

    }

    @Test
    @Transactional
    void addStock_ShouldHandleZeroQuantity_ForExistingProduct() {
        // Arrange
        ProductStockDTO dto = createStockDTO("SKU123", "MERCHANT1", 0, "Existing Product", BigDecimal.valueOf(99.99));
        Product existingProduct = createProduct(1L, "SKU123", "MERCHANT1", "Existing Product", BigDecimal.valueOf(99.99), 5);
        
        when(productRepository.selectBySkuAndMerchantId("SKU123", "MERCHANT1")).thenReturn(existingProduct);

        // Act
        productService.addStock(dto);

        // Assert
        verify(productRepository, times(1)).selectBySkuAndMerchantId("SKU123", "MERCHANT1");
        verify(productRepository, times(1)).addStock(1L, 0);
    }
}