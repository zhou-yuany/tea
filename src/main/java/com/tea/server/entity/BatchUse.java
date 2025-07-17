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
 * 店家商品与配方消耗对应关系表
 * </p>
 *
 * @author testjava
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BatchUse对象", description="店家商品与配方消耗对应关系表")
public class BatchUse extends BaseBean implements Serializable {


    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "商品与配料对应关系表id")
    private Long batchId;

    @ApiModelProperty(value = "当前店铺配方总数量")
    private Integer totalCount;

    @ApiModelProperty(value = "当前店铺配方销售数量")
    private Integer useCount;

    @ApiModelProperty(value = "出料口")
    private String outlet;

    @ApiModelProperty(value = "设备占位 01奶茶，02果茶")
    private String machineNo;
}
