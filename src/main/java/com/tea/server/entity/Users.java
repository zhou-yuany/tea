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
 * 用户表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Users对象", description="用户表")
public class Users extends BaseBean implements Serializable {

    @ApiModelProperty(value = "用户昵称")
    private String username;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "邮件地址")
    private String email;

    @ApiModelProperty(value = "用户在微信小程序对应的 openid")
    private String openid;

    @ApiModelProperty(value = "是否是新用户 1：新用户，2：老用户")
    private Integer isNew;

    @ApiModelProperty(value = "性别 1男 0女")
    private Integer sex;

    @ApiModelProperty(value = "累计总积分")
    private Integer points;

    @ApiModelProperty(value = "用户注册纬度")
    private String lat;

    @ApiModelProperty(value = "用户注册经度")
    private String lng;

    @ApiModelProperty(value = "统一微信用户id")
    private String unionid;

    @ApiModelProperty(value = "qq用户openid")
    private String qqopenid;

    @ApiModelProperty(value = "支付宝用户openid")
    private String zfbopenid;

    @ApiModelProperty(value = "所在省份")
    private String province;

    @ApiModelProperty(value = "所在城市")
    private String city;

    @ApiModelProperty(value = "所在地区")
    private String area;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "生日")
    private String brithday;


}
