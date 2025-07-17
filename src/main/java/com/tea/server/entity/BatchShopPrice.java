package com.tea.server.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 店家签约配方价格表
 * </p>
 *
 * @author zyy
 * @since 2024-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BatchShopPrice对象", description="店家签约配方价格表")
public class BatchShopPrice extends BaseBean implements Serializable {
    @ApiModelProperty(value = "配料名称")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "配料id")
    private Long batchId;

    @ApiModelProperty(value = "签约价格")
    private BigDecimal price;


}
