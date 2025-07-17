package com.tea.server.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券表
 * </p>
 *
 * @author zyy
 * @since 2023-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Coupon对象", description="优惠券表")
public class Coupon extends BaseBean implements Serializable {

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "平台类型 1：通用，2：指定店铺")
    private Integer platType;

    @ApiModelProperty(value = "优惠券类型 1：满减，2：新用户，3：完善信息(仅选择通用可展示)")
    private Integer type;

    @ApiModelProperty(value = "优惠券面值")
    private BigDecimal parValue;

    @ApiModelProperty(value = "优惠券使用限制 满减")
    private BigDecimal limits;

    @ApiModelProperty(value = "数量")
    private Integer count;

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

    @ApiModelProperty(value = "店铺名称")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty(value = "商品名称 多个以逗号分割")
    @TableField(exist = false)
    private String goodsName;

    @ApiModelProperty(value = "商品id数组")
    @TableField(exist = false)
    private List<Long> goodRangeList;

}
