package com.tea.server.entity.vo;

import com.tea.server.entity.GoodsToBatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 配方库存
 */
@Data
public class GoodsToBatchVo {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商家自己商品名称")
    private String goodsName;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "规格属性id（杯型）")
    private Long sizeId;

    @ApiModelProperty(value = "规格（杯型）")
    private String sizeName;

    @ApiModelProperty(value = "糖度规格参数id  paramId type=2")
    private Long sugarId;

    @ApiModelProperty(value = "温度规格参数id  paramId type=3")
    private Long temperatureId;

    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    @ApiModelProperty(value = "成本")
    private BigDecimal cost;

    @ApiModelProperty(value = "配料自己的品牌名称")
    private List<GoodsToBatch> goodsToBatchList;

}
