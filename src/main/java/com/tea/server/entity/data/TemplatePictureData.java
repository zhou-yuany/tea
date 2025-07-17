package com.tea.server.entity.data;

import com.tea.server.entity.TemplatePicture;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TemplatePictureData extends TemplatePicture {

    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "模板颜色")
    private String colorName;
}
