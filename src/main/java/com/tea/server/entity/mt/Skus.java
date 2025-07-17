package com.tea.server.entity.mt;

import lombok.Data;

import java.util.List;

@Data
public class Skus {
    private String sku_id; // sku唯一标识
    private String spec; // sku的规格
    private String upc; // upc码
    private String price; // sku的价格，不能为负数，不能超过10个字
    private String stock; // sku的库存数量，不能为负数，也不能为小数，传"*"表示表示库存无限
    private Integer sku_sequence;
    private String location_code;
    private String box_num;
    private String box_price;
    private Long weight;
    private String weight_unit;
    private List<SkuAttr> skuAttrs;
}
