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
 * 商户充值记录
 * </p>
 *
 * @author zyy
 * @since 2024-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RechargeRecord对象", description="商户充值记录")
public class RechargeRecord extends BaseBean implements Serializable {

    @ApiModelProperty(value = "账单类型 1：充值， 2：入账")
    private Integer type;

    @ApiModelProperty(value = "充值/出账 金额")
    private BigDecimal price;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "支付状态 0：未支付 1：已支付")
    private Integer payStatus;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;


}
