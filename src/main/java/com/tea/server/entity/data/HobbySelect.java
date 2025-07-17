package com.tea.server.entity.data;

import lombok.Data;

import java.util.List;

@Data
public class HobbySelect {
    private String other;

    private List<String> select;

    private Integer no;
}
