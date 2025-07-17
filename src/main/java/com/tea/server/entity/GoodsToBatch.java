package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 商品与配料对应关系表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GoodsToBatch对象", description="商品与配料对应关系表")
public class GoodsToBatch extends BaseBean implements Serializable {
    @ApiModelProperty(value = "设备id")
    @TableField(exist = false)
    private String batchName;

    @ApiModelProperty(value = "单位")
    @TableField(exist = false)
    private String unit;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商户配方id")
    private Long batchId;

    @ApiModelProperty(value = "当前规格的单位消耗数量")
    private Integer useNumber;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "杯型规格参数id  paramId type=1")
    private Long sizeId;

    @ApiModelProperty(value = "糖度规格参数id  paramId type=2")
    private Long sugarId;

    @ApiModelProperty(value = "温度规格参数id  paramId type=3")
    private Long temperatureId;

    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    @ApiModelProperty(value = "成本")
    private BigDecimal cost;

}
