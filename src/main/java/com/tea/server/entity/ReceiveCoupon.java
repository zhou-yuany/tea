package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户领取优惠券表
 * </p>
 *
 * @author testjava
 * @since 2023-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReceiveCoupon对象", description="用户领取优惠券表")
public class ReceiveCoupon extends BaseBean implements Serializable {
    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "用户openid")
    private String openid;

    @ApiModelProperty(value = "是否使用 1：使用过，2：未使用")
    private Integer isUsed;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "平台类型")
    private Integer platType;

    @ApiModelProperty(value = "优惠券类型 1：满减，2：会员，3：完善信息(仅选择通用可展示)")
    private Integer type;

    @ApiModelProperty(value = "优惠券面值")
    private BigDecimal parValue;

    @ApiModelProperty(value = "优惠券使用限制 满减")
    private BigDecimal limits;

    @ApiModelProperty(value = "商品范围 多个商品id英文逗号隔开")
    private String goodRange;

    @ApiModelProperty(value = "有效时长 （天数）")
    private Integer days;

    @ApiModelProperty(value = "有效时长 （月数）")
    private Integer months;

    @ApiModelProperty(value = "活动开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "活动结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "历史优惠券类型 1：过期，2：已使用")
    @TableField(exist = false)
    private Integer expireType;

    @ApiModelProperty(value = "商品名称")
    @TableField(exist = false)
    private String goodsName;

    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @TableField(exist = false)
    private LocalDateTime overdueTime;


}
