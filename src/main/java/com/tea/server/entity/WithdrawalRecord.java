package com.tea.server.entity;

import java.math.BigDecimal;
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
 * 提现表
 * </p>
 *
 * @author testjava
 * @since 2024-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WithdrawalRecord对象", description="提现表")
public class WithdrawalRecord extends BaseBean implements Serializable {


    @ApiModelProperty(value = "提现金额")
    private BigDecimal price;

    @ApiModelProperty(value = "店家id")
    private Long shopId;

    @ApiModelProperty(value = "状态 1:提现中 2：提现到账 3：提现驳回")
    private Integer type;

    @ApiModelProperty(value = "代理id")
    private Long agentId;


}
