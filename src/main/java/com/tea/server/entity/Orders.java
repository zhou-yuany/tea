package com.tea.server.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单记录表
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Orders对象", description="订单记录表")
public class Orders extends BaseBean implements Serializable {

    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "流水号")
    private String serialNum;

    @ApiModelProperty(value = "取餐码")
    private Integer pickupCode;

    @ApiModelProperty(value = "平台类型 1:茶饮小程序 2：饿了么 3：美团")
    private Integer platformType;

    @ApiModelProperty(value = "订单类型 1:外卖单 2：自提单")
    private Integer orderType;

    @ApiModelProperty(value = "订单状态 0：未开始制作，1:制作中 2：已完成")
    private Integer orderStatus;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "订单备注(客户备注)")
    private String notes;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "总支付金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "用户openid")
    private String openid;

    @ApiModelProperty(value = "总数量")
    private Integer totalCount;

    @ApiModelProperty(value = "客户预留电话")
    private String phone;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "支付类型 1：小程序支付， 2：扫码支付，3：现金支付")
    private Integer payType;

    @ApiModelProperty(value = "支付状态 1：待支付， 2：已支付")
    private Integer payStatus;

    @ApiModelProperty(value = "收银员id")
    private Long adminId;

    @ApiModelProperty(value = "是否取走 1：未取走 2：已取走")
    private Integer isTake;

    @ApiModelProperty(value = "是否叫号 1：未叫号 2：已叫号")
    private Integer isCall;

    @ApiModelProperty(value = "类型 1:手动 外卖单 2：自动 自助单")
    private Integer equipmentType;

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "收货地址")
    private String address;

    @ApiModelProperty(value = "收货人性别 1：男，2：女")
    private Integer sex;

    @ApiModelProperty(value = "是否匿名 1：是 2：否")
    private Integer isAnonymous;

    @ApiModelProperty(value = "赠予者")
    private String giver;

    @ApiModelProperty(value = "赠予备注")
    private String giverNotes;

    @ApiModelProperty(value = "预计送达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String deliveryDate;


}
