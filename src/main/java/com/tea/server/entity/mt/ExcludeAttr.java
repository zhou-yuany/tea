package com.tea.server.entity.mt;

import lombok.Data;

import java.util.List;

@Data
public class ExcludeAttr {
    private String attr_name;
    private List<String> attr_values;
}
