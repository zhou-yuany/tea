package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.sql.Blob;
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
 * 店铺表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Shop对象", description="店铺表")
public class Shop extends BaseBean implements Serializable {

    @ApiModelProperty(value = "品牌名称")
    @TableField(exist = false)
    private String brandName;

    @ApiModelProperty(value = "商品id数组")
    @TableField(exist = false)
    private List<Long> goodRangeList;

    @ApiModelProperty(value = "授权平台数组")
    @TableField(exist = false)
    private List<Integer> platformType;

    @ApiModelProperty(value = "经营商品名称")
    @TableField(exist = false)
    private String goodsName;

    @ApiModelProperty(value = "所属平台名称")
    @TableField(exist = false)
    private String platformName;

    @ApiModelProperty(value = "设备编号")
    @TableField(exist = false)
    private List<String> equipmentIdList;

    @ApiModelProperty(value = "设备编号")
    @TableField(exist = false)
    private List<String> equipmentCodeList;

    @ApiModelProperty(value = "类型 1:手动 2：自动")
    @TableField(exist = false)
    private Integer equipmentType;

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "模板颜色")
    private String color;

    @ApiModelProperty(value = "余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "用户在微信小程序对应的 openid")
    private String openid;

    @ApiModelProperty(value = "是否可用 1：可用，2：不可用")
    private Integer isUse;

    @ApiModelProperty(value = "收银、竖屏登录id")
    private Long adminId;

    @ApiModelProperty(value = "是否分账 1：是，2：否")
    private Integer divideAccounts;

    @ApiModelProperty(value = "是否使用设备 1：是，2：否")
    private Integer isUseDevice;

    @ApiModelProperty(value = "分账比例 最大30 百分制")
    private Integer proportion;

    @ApiModelProperty(value = "商户logo")
    private String logo;

    @ApiModelProperty(value = "授权设备型号 多个设备以英文逗号隔开")
    private String equipmentId;

    @ApiModelProperty(value = "地址纬度")
    private String lat;

    @ApiModelProperty(value = "地址经度")
    private String lng;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "登录名称")
    private String username;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "地区")
    private String area;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "密码   md5加密")
    private String password;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "编号 后台展示")
    private String number;

    @ApiModelProperty(value = "所属行业")
    private String sector;

    @ApiModelProperty(value = "月营业额")
    private String turnoverM;

    @ApiModelProperty(value = "商品范围 多个商品id英文逗号隔开")
    private String goodRange;

    @ApiModelProperty(value = "饿了么 店铺id")
    private Long eleShopId;

    @ApiModelProperty(value = "饿了么 回调地址")
    private String redirectUri;

    @ApiModelProperty(value = "平台分类 0 茶饮小程序，1：饿了么，2：美团 英文逗号隔开")
    private String type;

    @ApiModelProperty(value = "饿了么店铺token信息")
    private String accessToken;

    @ApiModelProperty(value = "饿了么店铺token信息")
    private String tokenType;

    @ApiModelProperty(value = "饿了么店铺token信息")
    private Long expiresIn;

    @ApiModelProperty(value = "饿了么店铺token信息")
    private String refreshToken;

    @ApiModelProperty(value = "美团 店铺id")
    private Long mtShopId;

    @ApiModelProperty(value = "美团 回调地址")
    private String mtRedirectUri;

    @ApiModelProperty(value = "美团店铺token信息")
    private String mtAccessToken;

    @ApiModelProperty(value = "美团店铺token信息")
    private String mtTokenType;

    @ApiModelProperty(value = "美团店铺token信息")
    private Long mtExpiresIn;

    @ApiModelProperty(value = "美团店铺token信息")
    private String mtRefreshToken;

    @ApiModelProperty(value = "商户二维码")
    private String codeUrl;

    @ApiModelProperty(value = "合同 多个地址以英文逗号隔开")
    private String agreement;

    @ApiModelProperty(value = "机器二维码 多个地址以英文逗号隔开")
    private String equipmentCode;

    @ApiModelProperty(value = "营业开始时间")
    private String onStartTime;

    @ApiModelProperty(value = "营业结束时间")
    private String onEndTime;

    @ApiModelProperty(value = "京东 店铺id")
    private Long jdShopId;

    @ApiModelProperty(value = "京东 回调地址")
    private String jdRedirectUri;

    @ApiModelProperty(value = "京东店铺token信息")
    private String jdAccessToken;

    @ApiModelProperty(value = "京东店铺token信息")
    private String jdTokenType;

    @ApiModelProperty(value = "京东店铺token信息")
    private Long jdExpiresIn;

    @ApiModelProperty(value = "京东店铺token信息")
    private String jdRefreshToken;

}
