package com.tea.server.entity.vo;

import com.tea.server.entity.ParamAllBatch;
import com.tea.server.entity.ParamBatch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ParamAllBatchVo extends ParamAllBatch {
    @ApiModelProperty(value = "温度规格名称")
    private String heatName;

    @ApiModelProperty(value = "糖度规格名称")
    private String sugarName;

    @ApiModelProperty(value = "杯型规格名称")
    private String sizeName;
}
