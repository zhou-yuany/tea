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
 * 商品配料表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Batching对象", description="商品配料表")
public class Batching extends BaseBean implements Serializable {

    @ApiModelProperty(value = "配料名称")
    private String name;

    @ApiModelProperty(value = "其他设备id")
    private Integer deviceId;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "配方自己的品牌")
    private String brand;

    @ApiModelProperty(value = "出料口")
    private String outlet;

    @ApiModelProperty(value = "设备占位 01奶茶，02果茶")
    private String machineNo;

    @ApiModelProperty(value = "收购价")
    private BigDecimal price;


}
