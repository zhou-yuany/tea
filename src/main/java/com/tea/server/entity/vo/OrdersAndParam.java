package com.tea.server.entity.vo;

import com.tea.server.entity.Param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 生成订单数据
 */
@Data
public class OrdersAndParam {

    @ApiModelProperty(value = "商品Id 商家自己")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal price;

    @ApiModelProperty(value = "商品数量")
    private Integer selectCount;

    @ApiModelProperty(value = "商品规格属性")
    private List<Param> selectParams;

}
