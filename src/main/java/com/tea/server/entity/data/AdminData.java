package com.tea.server.entity.data;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tea.server.entity.Admins;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AdminData extends Admins {
    @ApiModelProperty(value = "商户id数组")
    private List<Long> shopList;

    @ApiModelProperty(value = "商户id")
    private Long shopId;
}
