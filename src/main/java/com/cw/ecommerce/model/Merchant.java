package com.cw.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 商家账户表
 * merchant
 */
@Data
public class Merchant implements Serializable {
    private Long id;

    /**
     * 商家id
     */
    private String merchantId;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家账户余额（单位：元）
     */
    private BigDecimal balance;

    /**
     * 状态 0，正常 ， 1 停用
     */
    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}