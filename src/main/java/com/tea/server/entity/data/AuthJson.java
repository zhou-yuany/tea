package com.tea.server.entity.data;

import lombok.Data;

@Data
public class AuthJson {
    private String access_token;
    private Integer expires_in;
}
