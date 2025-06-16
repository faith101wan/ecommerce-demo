package com.cw.ecommerce.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 商品库存操作DTO
 */
@Data
@Schema(description = "商品库存操作请求参数")
public class ProductStockDTO {
    @NotBlank(message = "SKU不能为空")
    @Schema(description = "商品SKU编码", required = true, example = "IPHONE_13_RED")
    private String sku;

    @NotNull(message = "商家ID不能为空")
    @Schema(description = "商家ID", required = true, example = "123")
    private String merchantId;

    @NotNull(message = "操作数量不能为空")
    @Min(value = 1, message = "操作数量必须大于0")
    @Schema(description = "操作数量（必须>0）", required = true, example = "10")
    private Integer quantity;

    @Schema(description = "商品单价", required = false, example = "0.7")
    private BigDecimal price;

    @Schema(description = "商品名称", required = false, example = "iphone")
    private String name;

    @Schema(description = "操作类型（INCREASE-增加/DEDUCT-扣减）", example = "INCREASE")
    private String operationType; // 可选：INCREASE | DEDUCT

    @Schema(description = "操作原因备注", example = "手动补货")
    private String remark;
}