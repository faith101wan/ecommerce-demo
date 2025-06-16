package com.cw.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 交易流水表（审计用）
 * transaction_log
 */
@Data
public class TransactionLog implements Serializable {
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商家id
     */
    private String merchantId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易状态
     */
    private String status;

    private Date createdAt;

    /**
     * 逻辑删除位 0，正常 ， 1 删除
     */
    private Integer deleted;

    private static final long serialVersionUID = 1L;
}