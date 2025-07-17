package com.tea.server.entity.vo;

import com.tea.server.entity.ParamCate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品规格及属性
 */
@Data
public class ParamCateAndParam {
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "介绍")
    private String introduce;

    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    @ApiModelProperty(value = "商品图片")
    private String url;

    @ApiModelProperty(value = "商品规格及属性")
    private List<ParamCate> paramCateList;
}
