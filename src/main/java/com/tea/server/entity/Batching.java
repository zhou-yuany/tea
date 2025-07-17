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
 * @author testjava
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Batching对象", description="商品配料表")
public class Batching extends BaseBean implements Serializable {

    @ApiModelProperty(value = "配料名称")
    private String name;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "配料自己的品牌")
    private String brand;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "收购价")
    private BigDecimal price;


}
