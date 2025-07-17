package com.tea.server.entity.fz;

import lombok.Data;

@Data
public class Receivers {
    private String type; // MERCHANT_ID：商户号（mch_id或者sub_mch_id）PERSONAL_OPENID：个人openid
    private String account; // 分账接收方账号 类型是MERCHANT_ID时，是商户号（mch_id或者sub_mch_id）类型是PERSONAL_OPENID时，是个人openid
    private long amount; // 分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额
    private String description;
}
