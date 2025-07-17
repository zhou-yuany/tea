package com.tea.server.entity.vo;

import com.tea.server.entity.OrderParam;
import com.tea.server.entity.Orders;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单详情
 */
@Data
public class OrdersInfo extends Orders {
    @ApiModelProperty(value = "收银")
    private String adminName;


    @ApiModelProperty(value = "规格参数")
    private List<OrderParam> params;




}
