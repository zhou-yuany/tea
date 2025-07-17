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
 * 商品分类与规格对应关系表
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CateToParamCate对象", description="商品分类与规格对应关系表")
public class CateToParamCate extends BaseBean implements Serializable {


    @ApiModelProperty(value = "规格分类id")
    private Long paramCateId;

    @ApiModelProperty(value = "商品分类id")
    private Long cateAllId;



}
