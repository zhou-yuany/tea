package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 总规格参数配方表
 * </p>
 *
 * @author zyy
 * @since 2024-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ParamAllBatch对象", description="总规格参数配方表")
public class ParamAllBatch extends BaseBean implements Serializable {

    @ApiModelProperty(value = "温度规格参数id")
    private Long heatParamId;

    @ApiModelProperty(value = "温度规格参数消耗数量")
    private Integer heatUseNumber;

    @ApiModelProperty(value = "配方名称")
    private String name;

    @ApiModelProperty(value = "温度规格参数单位")
    private String heatUnit;

    @ApiModelProperty(value = "糖度规格参数id")
    private Long sugarParamId;

    @ApiModelProperty(value = "糖度规格参数消耗数量")
    private Integer sugarUseNumber;

    @ApiModelProperty(value = "糖度规格参数单位")
    private String sugarUnit;

}
