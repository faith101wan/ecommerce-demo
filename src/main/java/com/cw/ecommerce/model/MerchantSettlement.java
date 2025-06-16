package com.cw.ecommerce.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家每日结算记录表
 * merchant_settlement
 */
@Data
public class MerchantSettlement implements Serializable {
    /**
     * 结算记录ID
     */
    private Long id;

    /**
     * 商家ID（与transaction_log一致）
     */
    private String merchantId;

    /**
     * 结算日期（如2023-10-01）
     */
    private Date settlementDate;

    /**
     * 理论应收金额（从交易流水汇总）
     */
    private BigDecimal expectedAmount;

    /**
     * 实际到账金额（从账户系统获取）
     */
    private BigDecimal actualAmount;

    /**
     * 涉及商品种类数
     */
    private Integer productCount;

    /**
     * 交易订单数
     */
    private Integer transactionCount;

    /**
     * 结算状态：SUCCESS/FAILED/PENDING
     */
    private String status;

    /**
     * 差异原因（如金额或库存不符）
     */
    private String discrepancyReason;

    /**
     * 乐观锁版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}