package com.tea.server.entity.mt;


public enum MtGoods {
    app_id(""),
    timestamp(""),
    sig(""),
    app_poi_code(""), // 门店id
    app_food_code(""),
    name(""),
    description(""),
    price(""),
    min_order_count(""),
    unit(""),
    box_num(""),
    box_price(""),
    category_name(""),
    is_sold_out(""), // 0表上架，1表下架
    picture(""), // 只支持jpg格式，图片需要小于1600*1200
    longPictures(""), // 图文详情图片id或者图片URL，部分商家发放
    sequence(""), // 当前分类下的排序序号
    pictures(""),
    speciality(""), // 是否设置为招牌商品，同一门店最多可以设置15个。1-是，2-否，默认0，不修改该信息
    is_not_single(""), // 是否设置为单点不送，1-是，2-否，默认0，不修改该信息
    onlySellInCombo(""); // true 为 仅套餐中售卖, false 为 非仅套餐中售卖, 默认为false

    private String productDesc;

    private MtGoods(String productDesc) {
        this.productDesc = productDesc;
    }
}
