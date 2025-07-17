package com.tea.server.entity.vo;

import com.tea.server.entity.Goods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GoodsAndMachine extends Goods {
    @ApiModelProperty(value = "设备占位 01奶茶，02果茶")
    private String machineNo;

    @ApiModelProperty(value = "是否添加过配方 1：已添加，2：未添加")
    private Integer addBatch;

}
