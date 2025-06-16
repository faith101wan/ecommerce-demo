package com.cw.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 用户账户表
 * user
 */
@Data
public class User implements Serializable {
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 预存账户余额（单位：元）
     */
    private BigDecimal balance;

    /**
     * 状态 0，正常 ， 1 停用
     */
    private Integer status;

    /**
     * 货币种类
     */
    private String currency;

    private Date createdAt;

    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}