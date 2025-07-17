package com.tea.server.entity.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WxApiType {
    /**
     * Native下单
     */
    NATIVE_PAY("/v3/pay/transactions/native"),

    /**
     * JSAPI下单
     */
    JSAPI_PAY("/v3/pay/transactions/jsapi"),

    /**
     * 查询订单
     */
    ORDER_QUERY_BY_NO("/v3/pay/transactions/out-trade-no/%s"),

    /**
     * 关闭订单
     */
    CLOSE_ORDER_BY_NO("/v3/pay/transactions/out-trade-no/%s/close"),

    /**
     * 支付通知
     */
    NATIVE_NOTIFY("/client/order/pay/notify");

    /**
     * 类型
     */
    private final String type;

}
