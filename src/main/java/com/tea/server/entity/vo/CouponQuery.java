package com.tea.server.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponQuery {
    // @ApiModelProperty(value = "优惠券名称")
    // private String name;

    @ApiModelProperty(value = "平台类型 1：通用，2：指定店铺")
    private Integer platType;

    @ApiModelProperty(value = "优惠券类型 1：满减，2：会员")
    private Integer type;

    @ApiModelProperty(value = "查询开始时间", example = "2019-01-01 10:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
    private String end;
}
