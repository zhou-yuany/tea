package com.tea.server.entity.vo;

import com.tea.server.entity.Goods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GoodsMachine extends Goods {
    @ApiModelProperty(value = "设备占位 01 奶茶，02果茶")
    private String machineNo;
}
