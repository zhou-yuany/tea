package com.tea.server.entity.query;

import lombok.Data;

@Data
public class WithdrawalQuery {

    private Integer type;

    private String begin;

    private String end;
}
