package com.cw.ecommerce.repositories;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cw.ecommerce.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseMapper<Product> {

    /**
     * 原子扣减库存
     * @param productId 商品ID
     * @param quantity 扣减数量（必须>0）
     * @return 受影响的行数（0=库存不足或商品不存在，1=操作成功）
     */
    @Update("UPDATE product SET stock = stock - #{quantity} " +
            "WHERE id = #{productId} AND stock >= #{quantity}")
    int deductStock(@Param("productId") Long productId,
                    @Param("quantity") int quantity);

    /**
     * 根据SKU和商家ID查询商品（防商家越权）
     * @param sku 商品SKU
     * @param merchantId 商家ID
     * @return 商品实体（不存在时返回null）
     */
    @Select("SELECT * FROM product " +
            "WHERE sku = #{sku} AND merchant_id = #{merchantId} " +
            "LIMIT 1")
    Product selectBySkuAndMerchantId(@Param("sku") String sku,
                                     @Param("merchantId") String merchantId);

    /**
     * 增加库存
     */
    @Update("UPDATE product SET stock = stock + #{quantity} " +
            "WHERE id = #{productId}")
    int addStock(@Param("productId") Long productId,
                 @Param("quantity") int quantity);

    /**
     * 根据SKU查询商品
     */
    @Select("SELECT * FROM product WHERE sku = #{sku} LIMIT 1")
    Product selectBySku(@Param("sku") String sku);
}
