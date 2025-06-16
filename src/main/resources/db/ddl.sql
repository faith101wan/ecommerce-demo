-- 用户表（user）
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `user_id` VARCHAR(100) NOT NULL COMMENT '用户id',
                        `name` VARCHAR(100) NOT NULL COMMENT '用户名',
                        `balance` DECIMAL(15,2) NOT NULL DEFAULT '0.00' COMMENT '预存账户余额（单位：元）',
                        `status` int NOT NULL default 0 COMMENT '状态 0，正常 ， 1 停用',
                        `currency` VARCHAR(50) NOT NULL default '' COMMENT '货币种类',
                        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`),
                        INDEX `idx_user_name` (`name`)
) ENGINE=InnoDB COMMENT='用户账户表';

-- 商家表（merchant）
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                            `merchant_id` VARCHAR(100) NOT NULL COMMENT '商家id',
                            `name` VARCHAR(100) NOT NULL COMMENT '商家名称',
                            `balance` DECIMAL(15,2) NOT NULL DEFAULT '0.00' COMMENT '商家账户余额（单位：元）',
                            `status` int NOT NULL default 0 COMMENT '状态 0，正常 ， 1 停用',
                            `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='商家账户表';

-- 商品表（product）
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                           `merchant_id` VARCHAR(100) NOT NULL COMMENT '所属商家ID',
                           `sku` VARCHAR(50) NOT NULL COMMENT '商品唯一编码',
                           `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
                           `price` DECIMAL(10,2) NOT NULL COMMENT '单价（单位：元）',
                           `stock` INT NOT NULL DEFAULT '0' COMMENT '库存数量',
                           `sold_current_day` INT NOT NULL DEFAULT '0' COMMENT '当前卖出的量',
                           `deleted` int NOT NULL default 0 COMMENT '逻辑删除位 0，正常 ， 1 删除',
                           `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_product_merchant_sku` (`merchant_id`, `sku`)

) ENGINE=InnoDB COMMENT='商品库存表';

-- 交易流水表（transaction_log，可选）
DROP TABLE IF EXISTS `transaction_log`;
CREATE TABLE `transaction_log` (
                                   `id` BIGINT NOT NULL AUTO_INCREMENT,
                                   `user_id` VARCHAR(100) NOT NULL default '' COMMENT '用户id',
                                   `product_id` BIGINT NOT NULL COMMENT '商品ID',
                                   `merchant_id` VARCHAR(100) NOT NULL default '' COMMENT '商家id',
                                   `quantity` INT NOT NULL COMMENT '购买数量',
                                   `amount` DECIMAL(15,2) NOT NULL COMMENT '交易金额',
                                   `status` VARCHAR(20) NOT NULL DEFAULT 'SUCCESS' COMMENT '交易状态',
                                   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `deleted` int NOT NULL COMMENT '逻辑删除位 0，正常 ， 1 删除',
                                   PRIMARY KEY (`id`),
                                   INDEX `idx_transaction_user` (`user_id`),
                                   INDEX `idx_transaction_merchant` (`merchant_id`),
                                   INDEX `idx_transaction_product` (`product_id`),
                                   INDEX `idx_transaction_time` (`created_at`)
) ENGINE=InnoDB COMMENT='交易流水表（审计用）';



DROP TABLE IF EXISTS `merchant_settlement`;
CREATE TABLE `merchant_settlement` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '结算记录ID',
                                       `merchant_id` varchar(100) NOT NULL COMMENT '商家ID（与transaction_log一致）',
                                       `settlement_date` date NOT NULL COMMENT '结算日期（如2023-10-01）',
                                       `expected_amount` decimal(15,2) NOT NULL COMMENT '理论应收金额（从交易流水汇总）',
                                       `actual_amount` decimal(15,2) NOT NULL COMMENT '实际到账金额（从账户系统获取）',
                                       `product_count` int NOT NULL DEFAULT '0' COMMENT '涉及商品种类数',
                                       `transaction_count` int NOT NULL DEFAULT '0' COMMENT '交易订单数',
                                       `status` varchar(20) NOT NULL COMMENT '结算状态：SUCCESS/FAILED/PENDING',
                                       `discrepancy_reason` varchar(255) DEFAULT NULL COMMENT '差异原因（如金额或库存不符）',
                                       `version` int NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
                                       `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_merchant_date` (`merchant_id`, `settlement_date`) COMMENT '商家+日期唯一约束',
                                       KEY `idx_status` (`status`),
                                       KEY `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家每日结算记录表';