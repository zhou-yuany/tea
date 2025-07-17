package com.tea.server.entity.jd;

import lombok.Data;

import java.util.List;

@Data
public class StationShopCategoryRelationRequest {
    private String outStationNo;
    private List<Long> shopCategoryIds;
}
