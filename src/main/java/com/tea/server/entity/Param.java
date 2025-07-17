package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品规格表
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Param对象", description="商品规格表")
public class Param extends BaseBean implements Serializable {
    @ApiModelProperty(value = "售价")
    @TableField(exist = false)
    private BigDecimal price;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "规格分类id")
    private Long paramCateId;

    @ApiModelProperty(value = "是否可调整 1：可调整，2：不可调整")
    private Integer isAdjust;

    @ApiModelProperty(value = "是否推荐 1：推荐 2：不推荐")
    private Integer isRecommend;

    @ApiModelProperty(value = "类型 1：规格，2：糖度，3：温度，4：小料")
    private Integer type;

    @ApiModelProperty(value = "商品基础上加价")
    private BigDecimal addPrice;

    @ApiModelProperty(value = "基本用量")
    private Integer useNumber;



}
