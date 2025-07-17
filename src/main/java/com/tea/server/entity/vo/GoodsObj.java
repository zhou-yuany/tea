package com.tea.server.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoodsObj {
    @ApiModelProperty(value = "商品id")
    private List<Long> ids;
}
