package com.tea.server.entity.query;

import lombok.Data;

@Data
public class BatchUseQuery {
    private String orderNum;

    private String name;

    private Long shopId;

    private String begin;

    private String end;
}
