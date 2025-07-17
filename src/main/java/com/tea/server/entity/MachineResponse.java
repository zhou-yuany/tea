package com.tea.server.entity;

import lombok.Data;

@Data
public class MachineResponse {
    private String token;
    private long expire_at;
}
