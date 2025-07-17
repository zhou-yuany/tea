package com.tea.server.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品成本卡 配方
 */
@Data
public class CostCardInfoVo {

    @ApiModelProperty(value = "配料名称")
    private String batchName;

    @ApiModelProperty(value = "配料配比数量")
    private Integer batchUseCount;

    @ApiModelProperty(value = "配料单位")
    private String batchUnit;


}
