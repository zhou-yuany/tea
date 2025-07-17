package com.tea.server.entity.jd;

import lombok.Data;

@Data
public class SaveSpuResponse {
    private String code;
    private String msg;
    private SaveSpuResultData data;

    public boolean isSuccess() {
        return "0".equals(code);
    }
}
