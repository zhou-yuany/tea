package com.tea.server.entity.query;

import lombok.Data;

@Data
public class InterfaceLogQuery {
    private String begin;
    private String end;
    private Long shopId;
}
