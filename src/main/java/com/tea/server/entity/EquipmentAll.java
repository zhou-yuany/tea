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
 * 平台设备表
 * </p>
 *
 * @author zyy
 * @since 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EquipmentAll对象", description="平台设备表")
public class EquipmentAll extends BaseBean implements Serializable {

    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "是否禁用 1;已禁用 2：未禁用")
    private Integer isUse;

    @ApiModelProperty(value = "类型 1:手动 外卖店 2：自动 自助店")
    private Integer type;

    @ApiModelProperty(value = "是否绑定设备型号 1:绑定 2：未绑定")
    private Integer isBinding;


}
