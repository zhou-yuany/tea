package com.tea.server.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tea.server.entity.ParamCate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情及规格
 */
@Data
public class GoodsInfoAndParam {
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "介绍")
    private String introduce;

    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    @ApiModelProperty(value = "商品图片")
    private String url;

    @ApiModelProperty(value = "小贴士")
    private String tips;

    @ApiModelProperty(value = "规格分类名称")
    private List<ParamCate> paramCateList;




}
