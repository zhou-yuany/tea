package com.tea.server.entity.jd;

import lombok.Data;

import java.util.List;

@Data
public class SaveSpuResult {
    private Long superId;
    private String outSuperId;
    private List<SkuMainParterResponse> skuMainParterResponseList;
    private String taskId;
}
