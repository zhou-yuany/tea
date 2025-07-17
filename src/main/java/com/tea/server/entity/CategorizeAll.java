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
 * 总的商品分类表
 * </p>
 *
 * @author zyy
 * @since 2024-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CategorizeAll对象", description="总的商品分类表")
public class CategorizeAll extends BaseBean implements Serializable {

    @ApiModelProperty(value = "名称 小程序左侧导航栏显示")
    private String name;

    @ApiModelProperty(value = "实际分类名称 后台查看")
    private String realName;

    @ApiModelProperty(value = "设备占位 01 奶茶，02果茶")
    private String machineNo;

}
