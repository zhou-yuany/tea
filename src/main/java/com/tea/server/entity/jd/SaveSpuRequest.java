package com.tea.server.entity.jd;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaveSpuRequest {

    // 应用级别参数
    private String idempotentId;
    private String outSpuId;
    private String spuName;
    private List<Long> shopCategories;
    private Long categoryId;
    private Long brandId;
    private Integer fixedStatus;
    private List<String> images;
    private String productDesc;
    private Integer ifViewDesc;
    private List<SaleAttrRelationInfo> saleAttrRelationInfoList;
    private List<CustomAttrInfo> customAttrList;
    private Integer singleNoDelivery;
    private BigDecimal packageFee;
    private List<LightFoodSkuInfoRequest> skuMainInfoList;
    private List<String> outStationNos;
    private List<StationShopCategoryRelationRequest> stationShopCategories;
    private Integer packageOnlySale;
}
