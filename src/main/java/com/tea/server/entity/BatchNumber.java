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
 * 配料规格表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BatchNumber对象", description="配料规格表")
public class BatchNumber extends BaseBean implements Serializable {

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "配料id")
    private Long batchId;

    @ApiModelProperty(value = "配料名称")
    private String batchName;

    @ApiModelProperty(value = "单位数量")
    private Integer useNumber;

    @ApiModelProperty(value = "单位")
    private String unit;

}
