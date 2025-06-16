package com.cw.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商品库存表
 * product
 */
@Data
public class Product implements Serializable {
    private Long id;

    /**
     * 所属商家ID
     */
    private String merchantId;

    /**
     * 商品唯一编码
     */
    private String sku;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 单价（单位：元）
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 当前卖出的量
     */
    private Integer soldCurrentDay;

    /**
     * 逻辑删除位 0，正常 ， 1 删除
     */
    private Integer deleted;

    private Date createdAt;

    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}