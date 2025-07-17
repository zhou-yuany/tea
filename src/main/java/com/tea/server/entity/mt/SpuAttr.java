package com.tea.server.entity.mt;

import lombok.Data;

import java.util.List;

/**
 * spu售卖属性
 * spuAttr与skuAttr中的属性数量相同且属性内容相同（mode=2)
 */
@Data
public class SpuAttr {

    private Integer defaultSelect; // 是否默认选中 默认值为0
    private Integer no; // 属性编号；例：温度(1)，口味(2)等 no必须从大于等于1开始且no在name相同时候，no必须相同
    private Integer mode; // 售卖属性模式,1 ：普通售卖属性，2：决定价格库存的售卖属性
    private String name; // 名称example=冷热 name⻓度不能⼤于10
    private String value; // example=冷 value⻓度不能⼤于10
    private double price; // 属性价格 price小数点后最多两位 符合要求示例：1.整数 2.小数点后一位 3.小数点后两位
    private Integer sell_status; // 售卖状态（0：上架，1：下架）
    private Integer value_sequence; // 属性值排序序号,1开始 value_sequence必须从大于等于1开始且value_sequence在name相同时，value_sequence必须不同
    private List<ExcludeAttr> excludeAttrs;


}
