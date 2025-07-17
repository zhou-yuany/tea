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
 * 地址管理表
 * </p>
 *
 * @author testjava
 * @since 2024-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Address对象", description="地址管理表")
public class Address extends BaseBean implements Serializable {

    @ApiModelProperty(value = "寄送姓名")
    private String username;

    @ApiModelProperty(value = "寄送手机号")
    private String phone;

    @ApiModelProperty(value = "寄送省份")
    private String province;

    @ApiModelProperty(value = "寄送城市")
    private String city;

    @ApiModelProperty(value = "寄送地区")
    private String area;

    @ApiModelProperty(value = "详细地址")
    private String address;

    private String openid;

    @ApiModelProperty(value = "是否默认地址 0：否，1：是")
    private Integer isDefault;

    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "性别 1：男，2：女")
    private Integer sex;

    @ApiModelProperty(value = "门牌号")
    private String roomNumber;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "经度")
    private String longitude;


}
