package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
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
 * 管理员表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Admins对象", description="管理员表")
public class Admins extends BaseBean implements Serializable {

    @ApiModelProperty(value = "商户名称+类型 多个以英文逗号隔开")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "管理员类型  1：收银员 2：总管理")
    private Integer type;

    @ApiModelProperty(value = "登录名")
    private String username;

    @ApiModelProperty(value = "密码   md5加密")
    private String password;

    @ApiModelProperty(value = "创建id")
    private Long createdBy;

    @ApiModelProperty(value = "access_token传入时间")
    private LocalDateTime tokenIn;

    @ApiModelProperty(value = "access_token")
    private String accessToken;


}
