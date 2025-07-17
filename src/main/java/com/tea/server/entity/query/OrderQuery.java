package com.tea.server.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单列表筛选条件
 */
@Data
public class OrderQuery {
    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "平台类型 1:茶饮小程序 2：饿了么 3：美团 4：竖屏下单")
    private Integer platformType;

    @ApiModelProperty(value = "商户id")
    private Long shopId;

    @ApiModelProperty(value = "支付状态 1：待支付， 2：已支付")
    private Integer payStatus;

    private String begin;

    private String end;
}
