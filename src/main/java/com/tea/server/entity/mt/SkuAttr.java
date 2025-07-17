package com.tea.server.entity.mt;

import lombok.Data;

@Data
public class SkuAttr {
    private String skuId;
    private Integer no; // 属性编号；例：温度(1)，口味(2)等
    private String name; // 属性名称 example="冷热" name长度不能大于10
    private String value; // 属性值, example="冷" value长度不能大于10
}
