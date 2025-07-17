package com.tea.server.entity;

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
 * 物料消耗-订单表
 * </p>
 *
 * @author zyy
 * @since 2024-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BatchOrderUse对象", description="物料消耗-订单表")
public class BatchOrderUse extends BaseBean implements Serializable {

    @ApiModelProperty(value = "配料名称")
    @TableField(exist = false)
    private String batchName;

    @ApiModelProperty(value = "商家名称")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "配料id")
    private Long batchId;

    @ApiModelProperty(value = "当前店铺配方销售数量")
    private Integer useCount;

    @ApiModelProperty(value = "出料口")
    private String outlet;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNum;


}
