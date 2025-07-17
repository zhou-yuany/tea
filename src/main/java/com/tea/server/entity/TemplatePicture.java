package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 模板配图
 * </p>
 *
 * @author testjava
 * @since 2024-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TemplatePicture对象", description="模板配图")
public class TemplatePicture extends BaseBean implements Serializable {

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "logo")
    private String logo;

    @ApiModelProperty(value = "轮播图 多个以英文逗号分隔")
    private String banner;

    @ApiModelProperty(value = "到店自取")
    private String order1;

    @ApiModelProperty(value = "外卖点单")
    private String order2;

    @ApiModelProperty(value = "广告图上")
    private String ad;

    @ApiModelProperty(value = "广告图2")
    private String ad2;

    @ApiModelProperty(value = "优惠券广告")
    @TableField("adBg")
    private String adBg;

    @ApiModelProperty(value = "外卖")
    private String switch1;

    @ApiModelProperty(value = "到店")
    private String switch2;

    @ApiModelProperty(value = "活动图")
    private String activity;

    @ApiModelProperty(value = "画报")
    private String canvas;

    @ApiModelProperty(value = "购物车")
    private String cart;

    @ApiModelProperty(value = "成功")
    private String success;

    @ApiModelProperty(value = "失败")
    private String warning;

    @ApiModelProperty(value = "头像")
    private String header;

    @ApiModelProperty(value = "编辑")
    private String edit;

    @ApiModelProperty(value = "确认订单自提")
    private String selfPickup;

    @ApiModelProperty(value = "确认订单外卖")
    private String takeaway;

    @ApiModelProperty(value = "购物车")
    private String checked;

}
