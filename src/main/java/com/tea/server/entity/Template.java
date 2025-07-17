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
 * 模板名称表
 * </p>
 *
 * @author testjava
 * @since 2024-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Template对象", description="模板名称表")
public class Template extends BaseBean implements Serializable {

    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "模板颜色")
    private String colorName;

    @ApiModelProperty(value = "图集")
    private String pics;

    @ApiModelProperty(value = "颜色样式")
    private String color;


}
