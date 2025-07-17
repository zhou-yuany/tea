package com.tea.server.entity.vo;

import com.tea.server.entity.OrderParam;
import com.tea.server.entity.Orders;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 生成订单数据
 */
@Data
public class OrdersCall extends Orders {

    @ApiModelProperty(value = "商品规格属性")
    private List<OrderParam> orderParams;

}
