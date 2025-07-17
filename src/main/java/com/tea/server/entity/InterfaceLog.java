package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 请求接口日志
 * </p>
 *
 * @author zyy
 * @since 2024-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="InterfaceLog对象", description="请求接口日志")
public class InterfaceLog extends BaseBean implements Serializable {
    @ApiModelProperty(value = "店铺名")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty(value = "接口说明")
    private String title;

    @ApiModelProperty(value = "接口入参")
    private String requestParam;

    @ApiModelProperty(value = "响应参数")
    private String responseParam;

    @ApiModelProperty(value = "方法名称")
    private String methodName;

    @ApiModelProperty(value = "请求方式")
    private String requestWay;

    @ApiModelProperty(value = "请求耗时 ms")
    private String handleTime;

    @ApiModelProperty(value = "请求时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "request_time")
    private LocalDateTime requestTime;

    @ApiModelProperty(value = "错误信息")
    private String errorInfo;

    @ApiModelProperty(value = "错误码")
    private String errorCode;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "操作内容")
    private String content;

    @ApiModelProperty(value = "服务器状态 0正常 1异常")
    private Integer typeStatus;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;


}
