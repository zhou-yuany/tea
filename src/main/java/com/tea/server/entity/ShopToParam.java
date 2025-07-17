package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
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
 * 店铺与规格消耗对应关系表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ShopToParam对象", description="店铺与规格消耗对应关系表")
public class ShopToParam extends BaseBean implements Serializable {

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "规格id")
    private Long paramId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "当前规格的总数量")
    private Integer totalCount;

    @ApiModelProperty(value = "当前规格的销售数量")
    private Integer useCount;

    @ApiModelProperty(value = "商品基础上加价")
    private BigDecimal addPrice;

    @ApiModelProperty(value = "基本用量")
    private Integer useNumber;

    @ApiModelProperty(value = "类型 1：规格，2：糖度，3：温度，4：小料")
    private Integer type;

}
