package com.tea.server.entity.params;

import lombok.Data;

/**
 * 请求饿了么的参数
 */
@Data
public class EleParam {
    private String token;
    private String nop;
    private String action;
    private String id;
    private String signature;
    private Metas metas;
    private Params params;
    // {
    //     "token": "ed72bdaef7b635ecf9279147d76d6a55",
    //         "nop": "1.0.0",
    //         "metas": {
    //     "app_key": "wYO4C8ZLzB",
    //             "timestamp": 1486217703
    // },
    //     "params": {
    //     "orderId": "100027526535707064"
    // },
    //     "action": "eleme.order.getOrder",
    //         "id": "59802291567A41A584EE6947DEA777F0|1631082049766",
    //         "signature": "B450D7598455D163488A2ABEB1E0E0C5"
    // }
}
