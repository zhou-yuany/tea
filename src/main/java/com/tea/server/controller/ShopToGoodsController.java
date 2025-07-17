package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.*;
import com.tea.server.entity.data.ShopCoupon;
import com.tea.server.entity.vo.GoodsInfoAndParam;
import com.tea.server.service.CategorizeService;
import com.tea.server.service.GoodsToBatchService;
import com.tea.server.service.ReceiveCouponService;
import com.tea.server.service.ShopToGoodsService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 店家与商品对应关系表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@RestController
@RequestMapping("shopToGoods")
public class ShopToGoodsController {
    @Autowired
    private CategorizeService categorizeService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    @Autowired
    private GoodsToBatchService goodsToBatchService;

    @Autowired
    private ReceiveCouponService receiveCouponService;

    // 获取店家的商品分类
    @GetMapping("getCates")
    public R getCates(@RequestParam("shopId") Long shopId) {
        LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Categorize::getShopId, shopId).eq(Categorize::getStatus, 1);
        List<Categorize> list = categorizeService.list(wrapper);

        return R.ok().data("list", list);
    }

    // 获取分类下的商品列表
    @GetMapping("getGoods")
    public R getGoods(@RequestParam("shopId") Long shopId, @RequestParam("cateId") Long cateId) {
        List<ShopToGoods> list = shopToGoodsService.getGoods(shopId, cateId);
        return R.ok().data("list", list);
    }

    // 获取商家全部商品列表
    @GetMapping("getGoodsList")
    public R getGoodsList(@RequestParam("shopId") Long shopId) {
        List<ShopToGoods> list = shopToGoodsService.getGoodsList(shopId);
        return R.ok().data("list", list);
    }

    // 获取商品详情及规格
    @GetMapping("getGoodsInfoAndParam")
    public R getGoodsInfoAndParam(@RequestParam("shopId") Long shopId, @RequestParam("cateId") Long cateId, @RequestParam("goodsId") Long goodsId) {
        GoodsInfoAndParam info = shopToGoodsService.getGoodsInfoAndParam(shopId, goodsId);
        LambdaQueryWrapper<GoodsToBatch> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getShopId, shopId).eq(GoodsToBatch :: getGoodsId, goodsId);
        List<GoodsToBatch> list = goodsToBatchService.list(wrapper3);






        if (null != list && list.size() > 0){

                    list.stream().forEach(item -> {
                        LambdaQueryWrapper<ReceiveCoupon> couponWrapper = new LambdaQueryWrapper<>();
                        couponWrapper.eq(ReceiveCoupon :: getStatus, 1).eq(ReceiveCoupon :: getShopId, shopId).eq(ReceiveCoupon :: getIsUsed, 2).like(ReceiveCoupon :: getGoodRange, item.getGoodsId()).orderByDesc(ReceiveCoupon :: getId);
                        List<ReceiveCoupon> receiveCouponList = receiveCouponService.list(couponWrapper);

                        if (null != receiveCouponList && receiveCouponList.size() > 0){
                            ReceiveCoupon ranger = receiveCouponList.get(0);
                            Integer days = ranger.getDays();
                            Integer months = ranger.getMonths();
                            if(months > 0){
                                days = days + months * 30;
                            }
                            LocalDateTime time = LocalDateTime.now().plusDays(days);
                            if ( ranger.getStartTime().compareTo(LocalDateTime.now()) <= 0 && ranger.getEndTime().compareTo(LocalDateTime.now()) >= 0 && time.compareTo(LocalDateTime.now()) >= 0){

                                BigDecimal subtract = item.getPrice().subtract(ranger.getParValue());
                                BigDecimal p = subtract.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtract;

                                item.setPrice(p);
                            }

                        }

                    });




        }

        return R.ok().data("info", info).data("list", list);
    }

}

