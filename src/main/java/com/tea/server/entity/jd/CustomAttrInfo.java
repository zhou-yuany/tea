package com.tea.server.entity.jd;

import lombok.Data;

import java.util.List;

@Data
public class CustomAttrInfo {
    private String customAttrName;
    private List<String> customAttrValueNameList;
}
