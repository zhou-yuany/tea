package com.tea.server.entity.jd;

import lombok.Data;

@Data
public class ShopCategoryRequest {
    // 应用级别参数
    private Long pid;
    private String shopCategoryName;
    private Integer shopCategoryLevel;
    private Integer categoryType;
    private String createPin;
    private Integer optType;
    private String outStationNo;
}
