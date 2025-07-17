package com.tea.server.entity.query;

import lombok.Data;

@Data
public class AccountQuery {
    private String keyword;

    private Integer type;

    private String begin;

    private String end;
}
