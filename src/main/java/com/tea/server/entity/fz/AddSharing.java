package com.tea.server.entity.fz;

import lombok.Data;

import java.util.List;

@Data
public class AddSharing {
    private String mch_id; //微信支付分配的商户号
    private String appid;
    private String nonce_str; // 随机字符串，不长于32位
    private String sign;
    private AddReceiver receivers;
}
