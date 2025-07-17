package com.tea.server.entity.jd;

import lombok.Data;

import java.util.List;

@Data
public class SaleAttrRelationInfo {
    private String attrName;
    private List<String> attrValues;
}
