package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 配料出/入库记录表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="InventoryRecord对象", description="配料出/入库记录表")
public class InventoryRecord extends BaseBean implements Serializable {

    @ApiModelProperty(value = "记录类型 1:入库 2：出库")
    private Integer type;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "配料id")
    private Long batchId;

    @ApiModelProperty(value = "配料名称")
    private String batchName;

    @ApiModelProperty(value = "配料品牌名称")
    private String brand;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "规格 如500 ")
    private Integer useNumber;

    @ApiModelProperty(value = "配料单位")
    private String unit;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
