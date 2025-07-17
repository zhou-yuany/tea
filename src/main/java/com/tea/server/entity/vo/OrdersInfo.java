package com.tea.server.entity.vo;

import com.tea.server.entity.OrderParam;
import com.tea.server.entity.Orders;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情
 */
@Data
public class OrdersInfo extends Orders {
    @ApiModelProperty(value = "优惠券面值")
    private BigDecimal parValue;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "省份")
    private String shopProvince;

    @ApiModelProperty(value = "城市")
    private String shopCity;

    @ApiModelProperty(value = "地区")
    private String shopArea;

    @ApiModelProperty(value = "详细地址")
    private String shopAddress;

    @ApiModelProperty(value = "联系电话")
    private String shopPhone;

    @ApiModelProperty(value = "店铺地址纬度")
    private String lat;

    @ApiModelProperty(value = "店铺地址经度")
    private String lng;

    private List<OrderParam> params;
}
