package com.tea.server.core;

import cn.hutool.core.map.MapUtil;

import java.util.Map;

/*

10 待报价
	-> 后台报价
20 已报价
	-> 客户/合伙人 确认下单(合伙人下单同时可加价)
30 已下单
    后台分配代驾
    后台分配城市经理
    后台分配承运人(可能是多个)
	-> 1. 上门提车
        -> a. 后台分配代驾, 改为40状态
        -> b. 后台分配城市经理(验收车辆(拍照)), 改为50状态
	-> 2. 无需上门提车 客户/合伙人送车到 后台分配城市经理(验收车辆(拍照)), 改为50状态 (显示无需提车)
40 待提车
	-> 代驾验收车辆(拍照)
45 已提车

50 待中转
	-> 城市经理人-验收车辆(拍照)
60 已中转
	-> 承运人-验收车辆(拍照)
70 运输中
    -> 签到(上传位置)
80 已到达
	-> 等待取货人收货并支付
90 已支付
	-> 取货人支付成功

 */
public interface OrderCast {

    //9) 统一下单API（扫码）
    String PRETRADE_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    Map<String, String> AREA = MapUtil
            .builder("北京市", "北区").put("天津市", "北区").put("河北省", "北区").put("山西省", "北区").put("内蒙古自治区", "北区").put("辽宁省", "北区").put("吉林省", "北区").put("黑龙江省", "北区").put("山东省", "北区").put("河南省", "北区")
            .put("广东省", "南区").put("广西壮族自治区", "南区").put("海南省", "南区").put("湖南省", "南区").put("江西省", "南区").put("福建省", "南区").put("香港特别行政区", "南区").put("台湾省", "南区").put("澳门特别行政区", "南区")
            .put("上海市", "东区").put("江苏省", "东区").put("浙江省", "东区").put("安徽省", "东区")
            .put("湖北省", "西区").put("重庆市", "西区").put("四川省", "西区").put("贵州省", "西区").put("云南省", "西区").put("西藏自治区", "西区").put("陕西省", "西区").put("甘肃省", "西区").put("青海省", "西区").put("宁夏回族自治区", "西区").put("新疆维吾尔自治区", "西区")
            .build();

    Map<Byte, String> IMAGE_TYPE = MapUtil.builder((byte) 10, "INFO")
            .put((byte) 20, "DJ_ERROR")
            .put((byte) 30, "START_CJ_ERROR")
            .put((byte) 40, "ZZ_CJ_ERROR")
            .put((byte) 50, "DEST_CJ_ERROR")
            .put((byte) 60, "CYR_ERROR")
            .put((byte) 70, "ZZ_CYR_ERROR")
            .build();

    /**
     * 待报价
     */
    Byte WAIT_OFFER = 10;
    /**
     * 已报价
     */
    Byte ALREADY_OFFER = 20;
    /**
     * 已下单
     */
    Byte ALREADY_PLACE_ORDER = 30;
    /**
     * 待提车
     */
    Byte WAIT_GET_CAR = 40;
    /**
     * 已提车
     */
    Byte ALREADY_GET_CAR = 45;
    /**
     * 待中转
     */
    Byte WAIT_TRANSFER = 50;
    /**
     * 已中转
     */
    Byte ALREADY_TRANSFER = 60;
    /**
     * 运输中
     */
    Byte TRANSFER_ING = 70;
    /**
     * 待交付
     */
    Byte TRANSFER_ING_AFTER = 75;
    /**
     * 已到达
     */
    Byte ALREADY_GET_TO = 80;
    /**
     * 已支付
     */
    Byte ALREADY_PAYMENT = 90;


}
