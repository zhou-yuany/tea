package com.tea.server.entity.vo;

import com.tea.server.entity.GoodsToBatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品成本卡
 */
@Data
public class CostCardVo extends GoodsToBatch {

    @ApiModelProperty(value = "名称 后台分类显示")
    private String realName;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "规格名称")
    private String paramName;

    // @ApiModelProperty(value = "单位")
    // private String unit;

}
