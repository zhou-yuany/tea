package com.tea.server.entity.vo;

import com.tea.server.entity.BatchUse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配方库存
 */
@Data
public class BatchingVo extends BatchUse {

    @ApiModelProperty(value = "配料名称")
    private String name;

    @ApiModelProperty(value = "配料自己的品牌名称")
    private String brandName;

    @ApiModelProperty(value = "单位")
    private String unit;

}
