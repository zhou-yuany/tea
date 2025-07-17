package com.tea.server.entity.jd;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LightFoodSkuInfoRequest {
    private String outSkuId;
    private BigDecimal skuPrice;
    private Integer stock;
    private String specDesc;
    private List<SaleAttrValueRelationInfo> saleAttrValues;
    private String barCode;
    private BigDecimal weight;
    private String weightUnit;
    private Integer fixedStatus;
}
