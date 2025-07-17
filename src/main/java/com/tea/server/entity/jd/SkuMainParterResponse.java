package com.tea.server.entity.jd;

import lombok.Data;

import java.util.List;

@Data
public class SkuMainParterResponse {
    private String skuId;
    private String outSkuId;
    private String resultCode;
    private String resultMsg;
    private List<SkuResult> skuResultList;
}
