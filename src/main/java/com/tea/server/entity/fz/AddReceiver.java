package com.tea.server.entity.fz;

import lombok.Data;

@Data
public class AddReceiver {
    private String type; // MERCHANT_ID：商户号（mch_id或者sub_mch_id）PERSONAL_OPENID：个人openid
    private String account; // 分账接收方账号 类型是MERCHANT_ID时，是商户号（mch_id或者sub_mch_id）类型是PERSONAL_OPENID时，是个人openid
    private String relation_type; // USER

}
