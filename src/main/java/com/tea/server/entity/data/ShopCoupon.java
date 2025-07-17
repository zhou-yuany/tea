package com.tea.server.entity.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopCoupon {
    private Long id;

    private String goodRange;

    @ApiModelProperty(value = "优惠券面值")
    private BigDecimal parValue;

}
