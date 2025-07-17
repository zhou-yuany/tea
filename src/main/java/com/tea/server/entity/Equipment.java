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
 * 店家设备表
 * </p>
 *
 * @author testjava
 * @since 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Equipment对象", description="店家设备表")
public class Equipment extends BaseBean implements Serializable {

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "设备别名")
    private String name;

    @ApiModelProperty(value = "是否禁用 1;已禁用 2：未禁用")
    private Integer isUse;

    @ApiModelProperty(value = "类型 1:手动 2：自动")
    private Integer type;



}
