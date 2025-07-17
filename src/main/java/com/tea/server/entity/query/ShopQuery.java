package com.tea.server.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopQuery {
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;
}
