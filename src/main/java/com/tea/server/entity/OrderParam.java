package com.tea.server.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tea.server.core.BaseBean;
import com.tea.server.entity.vo.OutletVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单参数记录表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderParam对象", description="订单参数记录表")
public class OrderParam extends BaseBean implements Serializable {

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "规格参数")
    private String specifications;

    @ApiModelProperty(value = "数量")
    private Integer number;

    @ApiModelProperty(value = "制作状态 1：制作中 2：已完成")
    private Integer makeStatus;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品图片")
    private String url;

    @ApiModelProperty(value = "杯型规格参数id  paramId type=1")
    private Long sizeId;

    @ApiModelProperty(value = "糖度规格参数id  paramId type=2")
    private Long sugarId;

    @ApiModelProperty(value = "温度规格参数id  paramId type=3")
    private Long temperatureId;

    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "商品图片")
    @TableField(exist = false)
    private List<OutletVo> outletList;

    @ApiModelProperty(value = "设备占位 01奶茶，02果茶")
    @TableField(exist = false)
    private String machineNo;

    @ApiModelProperty(value = "杯型")
    @TableField(exist = false)
    private String cupSize;

    @ApiModelProperty(value = "请求设备参数")
    @TableField(exist = false)
    private String urlParam;

    @ApiModelProperty(value = "请求设备参数 配料用料")
    @TableField(exist = false)
    private HashMap<Object, Object> arr;

    @ApiModelProperty(value = "请求设备参数确认制作 ")
    @TableField(exist = false)
    private HashMap<Object, Integer> arr1;

    @ApiModelProperty(value = "授权设备型号 单个")
    @TableField(exist = false)
    private String equipmentId;


}
