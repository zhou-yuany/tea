package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Categorize对象", description="商品分类表")
public class Categorize extends BaseBean implements Serializable {
    @ApiModelProperty(value = "店铺token信息")
    @TableField(exist = false)
    private String accessToken;

    @ApiModelProperty(value = "店铺token信息")
    @TableField(exist = false)
    private String tokenType;

    @ApiModelProperty(value = "店铺token信息")
    @TableField(exist = false)
    private Long expiresIn;

    @ApiModelProperty(value = "店铺token信息")
    @TableField(exist = false)
    private String refreshToken;

    @ApiModelProperty(value = "名称 小程序左侧导航栏显示")
    private String name;

    @ApiModelProperty(value = "实际分类名称 后台查看")
    private String realName;

    @ApiModelProperty(value = "设备占位 01 奶茶，02果茶")
    private String machineNo;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "总分类id")
    private Long cateAllId;

    @ApiModelProperty(value = "平台 0：茶饮小程序，1：饿了么，2：美团")
    private Integer type;

    @ApiModelProperty(value = "饿了么上的商品分类Id")
    private Long eleId;

    @ApiModelProperty(value = "美团上的商品分类Id")
    private Long mtId;

    @ApiModelProperty(value = "京东上的商品分类Id")
    private Long jdId;

    @ApiModelProperty(value = "描述")
    private String description;



}
