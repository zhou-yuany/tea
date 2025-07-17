package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品规格分类表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ParamCate对象", description="商品规格分类表")
public class ParamCate extends BaseBean implements Serializable {

    @ApiModelProperty(value = "商品规格下的属性")
    @TableField(exist = false)
    private List<Param> paramList;

    @ApiModelProperty(value = "名称")
    private String name;


}
