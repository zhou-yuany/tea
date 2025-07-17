package com.tea.server.entity.data;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OrderAttrs {
    private List<Map<String, Integer>> attrs;
}
