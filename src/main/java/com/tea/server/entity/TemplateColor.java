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
 * 模板配色
 * </p>
 *
 * @author testjava
 * @since 2024-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TemplateColor对象", description="模板配色")
public class TemplateColor extends BaseBean implements Serializable {

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "分类文字颜色")
    private String activity;

    @ApiModelProperty(value = "定位文字颜色")
    private String positioning;


}
