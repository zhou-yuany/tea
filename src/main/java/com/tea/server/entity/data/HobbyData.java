package com.tea.server.entity.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class HobbyData {
    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "hobby")
    private List<HobbySelect> hobby;
}
