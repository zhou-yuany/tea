package com.tea.server.entity.jd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JdToken {
    private String token;
    @JsonProperty("expires in")  // 处理带空格的字段
    private String expiresIn;
    private String time;
    private String uid;
    @JsonProperty("user nick")   // 处理带空格的字段
    private String userNick;
    @JsonProperty("venderld")    // 注意字段名可能是拼写错误（应该是 vendorId？）
    private String venderId;
}
