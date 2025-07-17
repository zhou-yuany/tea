package com.tea.server.entity.vo;

import com.tea.server.entity.GoodsAllBatch;
import com.tea.server.entity.GoodsToBatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品配方列表数据
 */
@Data
public class GoodsAllBatchList extends GoodsAllBatch {
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "规格名称")
    private String paramName;

    @ApiModelProperty(value = "规格名称")
    private String sugarName;

    @ApiModelProperty(value = "规格名称")
    private String temperatureName;

    @ApiModelProperty(value = "配料名称")
    private String batchName;

    @ApiModelProperty(value = "数量单位")
    private String numberUnit;
}
