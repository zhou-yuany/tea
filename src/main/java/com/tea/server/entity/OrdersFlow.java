package com.tea.server.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单流水表
 * </p>
 *
 * @author zyy
 * @since 2024-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrdersFlow对象", description="订单流水表")
public class OrdersFlow extends BaseBean implements Serializable {

    @ApiModelProperty(value = "账单类型 1：入账， 2：出账")
    private Integer type;

    @ApiModelProperty(value = "入账/出账 订单金额")
    private BigDecimal price;

    @ApiModelProperty(value = "流水号 和订单里保持一致")
    private String serialNum;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "店家自己的商品id")
    private Long goodsId;

    @ApiModelProperty(value = "支付平台 0：茶饮小程序， 1：饿了么，2：美团, 3:线下 4: 充值")
    private Integer payPlatform;

    @ApiModelProperty(value = "支付类型 1：小程序支付， 2：扫码支付，3：现金支付")
    private Integer payType;

    @ApiModelProperty(value = "收银员id")
    private Long adminId;

    @ApiModelProperty(value = "杯数")
    private Integer number;

    @ApiModelProperty(value = "代理分销金额")
    private BigDecimal distributionPrice;

    @ApiModelProperty(value = "商户分销金额")
    private BigDecimal shopDistributionPrice;

    @ApiModelProperty(value = "总平台分销金额")
    private BigDecimal platDistributionPrice;

    @ApiModelProperty(value = "代理id")
    private Long agentId;

    @ApiModelProperty(value = "支付状态 0：未支付 1：已支付")
    private Integer payStatus;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;


}
