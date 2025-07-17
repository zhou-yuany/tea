package com.tea.server.entity.vo;

import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单参数记录数据
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Data
public class ParamOrders {
    @ApiModelProperty(value = "类型 1:手动 2：自动")
    private Integer equipmentType;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "规格参数")
    private String curSelect;

    @ApiModelProperty(value = "数量")
    private String goodsCount;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品图片")
    private String url;

    @ApiModelProperty(value = "客户备注")
    private String remark;

    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "总价")
    private String totalPrice;

    @ApiModelProperty(value = "总数量")
    private Integer totalCount;

    @ApiModelProperty(value = "客户预留电话")
    private String phone;

    @ApiModelProperty(value = "规格参数id")
    private List<Long> paramIds;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "订单类型 1:外卖单 2：自提单")
    private Integer orderType;

    @ApiModelProperty(value = "是否匿名 1：是 2：否")
    private Integer isAnonymous;

    @ApiModelProperty(value = "赠予者")
    private String giver;

    @ApiModelProperty(value = "赠予备注")
    private String giverNotes;

    private String deliveryDate;

    @ApiModelProperty(value = "收货地址")
    private String address;

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "收货人性别 1：男，2：女")
    private Integer sex;



}
