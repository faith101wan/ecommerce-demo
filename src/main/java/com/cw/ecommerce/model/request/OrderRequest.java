package com.cw.ecommerce.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "下单请求参数")
public class OrderRequest {

    @NotBlank(message = "SKU不能为空")
    @Schema(description = "商品SKU编码", required = true, example = "IPHONE_13_RED")
    private String sku;

    @NotNull(message = "操作数量不能为空")
    @Min(value = 1, message = "操作数量必须大于0")
    @Schema(description = "操作数量（必须>0）", required = true, example = "10")
    private Integer quantity;

    @NotNull(message = "下单用户ID")
    @Schema(description = "下单用户id", required = true, example = "cheng.wan")
    private String userId ;

}
