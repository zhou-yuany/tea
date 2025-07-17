package com.tea.server.entity;

import java.math.BigDecimal;
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
 * 骑手管理表
 * </p>
 *
 * @author testjava
 * @since 2024-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Takeaway对象", description="骑手管理表")
public class Takeaway extends BaseBean implements Serializable {

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "登录名")
    private String username;

    @ApiModelProperty(value = "密码   md5加密")
    private String password;

    @ApiModelProperty(value = "创建id")
    private Integer createdBy;

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

    @ApiModelProperty(value = "是否可用 1：可用，2：禁用")
    private Integer isUse;

    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "可提现金额")
    private BigDecimal unUsedPrice;

    @ApiModelProperty(value = "提现中金额")
    private BigDecimal inUsePrice;


}
