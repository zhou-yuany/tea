package com.tea.server.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配方库存
 */
@Data
public class OutletVo {

    @ApiModelProperty(value = "当前规格的单位消耗数量")
    private Integer useNumber;

    @ApiModelProperty(value = "出料口")
    private String outlet;

    @ApiModelProperty(value = "名称")
    private String name;

}
