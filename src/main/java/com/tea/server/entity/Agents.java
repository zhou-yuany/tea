package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
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
 * 代理商户表
 * </p>
 *
 * @author zyy
 * @since 2024-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Agents对象", description="代理商户表")
public class Agents extends BaseBean implements Serializable {
    @ApiModelProperty(value = "姓名")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty(value = "商品id数组")
    @TableField(exist = false)
    private List<Long> shopRangeList;

    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "登录名")
    private String username;

    @ApiModelProperty(value = "密码   md5加密")
    private String password;

    @ApiModelProperty(value = "创建id")
    private Long createdBy;

    @ApiModelProperty(value = "代理的商户id 多个以英文逗号分割")
    private String shopId;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "地区")
    private String area;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "是否分账 1：是，2：否")
    private Integer divideAccounts;

    @ApiModelProperty(value = "分账比例 最大30 百分制")
    private Integer proportion;

    @ApiModelProperty(value = "是否可用 1：可用，2：不可用")
    private Integer isUse;

    @ApiModelProperty(value = "可提现金额")
    private BigDecimal unUsedPrice;

    @ApiModelProperty(value = "提现中金额")
    private BigDecimal inUsePrice;


}
