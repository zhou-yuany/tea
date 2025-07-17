package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 店家与商品对应关系表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ShopToGoods对象", description="店家与商品对应关系表")
public class ShopToGoods extends BaseBean implements Serializable {
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

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "当前店铺的商品总数量")
    private Integer totalCount;

    @ApiModelProperty(value = "当前店铺的商品销售数量")
    private Integer useCount;

    @ApiModelProperty(value = "是否上架 1：上架，2：未上架")
    private Integer isGrounding;

    @ApiModelProperty(value = "是否添加过配方 1：已添加配方，2：未添加配方")
    private Integer addBatch;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "介绍")
    private String introduce;

    @ApiModelProperty(value = "分类id")
    private Long cateId;

    @ApiModelProperty(value = "售价")
    private BigDecimal price;

    @ApiModelProperty(value = "成本")
    private BigDecimal cost;

    @ApiModelProperty(value = "商品图片")
    private String url;

    @ApiModelProperty(value = "小贴士")
    private String tips;

    @ApiModelProperty(value = "饿了么上的商品Id")
    private Long eleId;

    @ApiModelProperty(value = "美团上的商品Id")
    private Long mtId;

    @ApiModelProperty(value = "哈希值")
    private String imageHash;

    @ApiModelProperty(value = "授权设备型号 单个")
    private String equipmentId;

    @ApiModelProperty(value = "详情图 多个图片以英文逗号隔开")
    private String detailsUrl;

    @ApiModelProperty(value = "京东上的商品Id(outSpuId)")
    private String jdId;

    @ApiModelProperty(value = "京东上的商品outSkuId")
    private String outSkuId;


}
