package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.Coupon;
import com.tea.server.entity.ReceiveCoupon;
import com.tea.server.entity.ShopToGoods;
import com.tea.server.service.CouponService;
import com.tea.server.service.ReceiveCouponService;
import com.tea.server.service.ShopToGoodsService;
import com.tea.server.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-08-25
 */
@RestController
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private ReceiveCouponService receiveCouponService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    /**
     * 查询店铺下的优惠券 以及用户是否领取过优惠券
     */
    @GetMapping("getCouponByOpenid")
    public R getCouponByOpenid(@RequestParam("shopId") Long shopId, @RequestParam("openid") String openid) {
        List<ReceiveCoupon> list = couponService.getCouponByOpenid(shopId, openid);
        return R.ok().data("list", list);
    }

    /**
     * 领取优惠券
     */
    @PostMapping("receiveCouponByOpenid")
    public R receiveCouponByOpenid(Long couponId, String openid){
        ReceiveCoupon receiveCoupon = new ReceiveCoupon();
        receiveCoupon.setOpenid(openid);
        receiveCoupon.setCouponId(couponId);
        receiveCoupon.setCreateTime(LocalDateTime.now());
        receiveCoupon.setUpdateTime(LocalDateTime.now());
        boolean save = receiveCouponService.save(receiveCoupon);
        if (save) {
            return R.ok();
        }else {
            return R.error();
        }

    }

    /**
     * 用户所有优惠券列表
     */
    @GetMapping("getCouponList")
    public R getCouponList(@RequestParam("type") Integer type, @RequestParam("openid") String openid){
        LambdaQueryWrapper<ReceiveCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReceiveCoupon :: getStatus, 1).eq(ReceiveCoupon :: getOpenid, openid).orderByDesc(ReceiveCoupon :: getId);
        List<ReceiveCoupon> list = receiveCouponService.list(queryWrapper);
        List<ReceiveCoupon> couponList = new ArrayList<>();
        if (list.size() > 0) {
            if (type == 0){
                list.stream().forEach(item -> {

                    if (item.getIsUsed() == 2){
                        Integer days = item.getDays();
                        Integer months = item.getMonths();
                        if(months > 0){
                            days = days + months * 30;
                        }
                        LocalDateTime time = LocalDateTime.now().plusDays(days);
                        item.setOverdueTime(time);

                        if (null != item.getGoodRange() && !item.getGoodRange().equals("")){

                            List<Long> goodsIds = Arrays.asList(item.getGoodRange().split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
                            LambdaQueryWrapper<ShopToGoods> goodWrapper = new LambdaQueryWrapper<>();
                            goodWrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, item.getShopId()).in(ShopToGoods :: getId, goodsIds);
                            List<ShopToGoods> shopToGoods = shopToGoodsService.list(goodWrapper);

                            if (null != shopToGoods && shopToGoods.size() > 0){
                                List<String> strings = shopToGoods.stream().map(good -> good.getName()).collect(Collectors.toList());
                                String goodsName = strings.stream().map(String::valueOf).collect(Collectors.joining(","));

                                item.setGoodsName(goodsName);
                            }
                        }

                        if (item.getStartTime().compareTo(LocalDateTime.now()) <= 0 && item.getEndTime().compareTo(LocalDateTime.now()) >= 0 && time.compareTo(LocalDateTime.now()) >= 0){
                            couponList.add(item);
                        }
                    }

                });
            }else {
                list.stream().forEach(item -> {
                    Integer days = item.getDays();
                    Integer months = item.getMonths();
                    if(months > 0){
                        days = days + months * 30;
                    }
                    LocalDateTime time = LocalDateTime.now().plusDays(days);
                    item.setOverdueTime(time);
                    if (null != item.getGoodRange() && !item.getGoodRange().equals("")){

                        List<Long> goodsIds = Arrays.asList(item.getGoodRange().split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
                        LambdaQueryWrapper<ShopToGoods> goodWrapper = new LambdaQueryWrapper<>();
                        goodWrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, item.getShopId()).in(ShopToGoods :: getId, goodsIds);
                        List<ShopToGoods> shopToGoods = shopToGoodsService.list(goodWrapper);

                        if (null != shopToGoods && shopToGoods.size() > 0){
                            List<String> strings = shopToGoods.stream().map(good -> good.getName()).collect(Collectors.toList());
                            String goodsName = strings.stream().map(String::valueOf).collect(Collectors.joining(","));

                            item.setGoodsName(goodsName);
                        }
                    }
                        if (item.getIsUsed() == 1){
                            item.setExpireType(2);
                            couponList.add(item);
                        }
                        if (item.getIsUsed() == 2 && item.getEndTime().compareTo(LocalDateTime.now()) < 0 && time.compareTo(LocalDateTime.now()) < 0){
                            item.setExpireType(1);
                            couponList.add(item);
                        }
                });

            }
        }

        return R.ok().data("list", couponList);
    }

    /**
     * 用户可用优惠券列表
     */
    @GetMapping("getIsCoupons")
    public R getIsCoupons(@RequestParam("shopId") Long shopId, @RequestParam("openid") String openid){
        LambdaQueryWrapper<ReceiveCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReceiveCoupon :: getStatus, 1).eq(ReceiveCoupon :: getOpenid, openid).eq(ReceiveCoupon :: getIsUsed, 2).orderByDesc(ReceiveCoupon :: getId);
        List<ReceiveCoupon> list = receiveCouponService.list(queryWrapper);
        List<ReceiveCoupon> couponList = new ArrayList<>();
        if (list.size() > 0) {
                list.stream().forEach(item -> {
                            Integer days = item.getDays();
                            Integer months = item.getMonths();
                            if(months > 0){
                                days = days + months * 30;
                            }
                            LocalDateTime time = LocalDateTime.now().plusDays(days);
                            item.setOverdueTime(time);
                            if (null != item.getGoodRange() && !item.getGoodRange().equals("")){

                                List<Long> goodsIds = Arrays.asList(item.getGoodRange().split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
                                LambdaQueryWrapper<ShopToGoods> goodWrapper = new LambdaQueryWrapper<>();
                                goodWrapper.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getShopId, shopId).in(ShopToGoods :: getId, goodsIds);
                                List<ShopToGoods> shopToGoods = shopToGoodsService.list(goodWrapper);

                                if (null != shopToGoods && shopToGoods.size() > 0){
                                    List<String> strings = shopToGoods.stream().map(good -> good.getName()).collect(Collectors.toList());
                                    String goodsName = strings.stream().map(String::valueOf).collect(Collectors.joining(","));

                                    item.setGoodsName(goodsName);
                                }
                            }



                    if (item.getPlatType() == 1){
                        if (item.getStartTime().compareTo(LocalDateTime.now()) <= 0 && item.getEndTime().compareTo(LocalDateTime.now()) >= 0 && time.compareTo(LocalDateTime.now()) >= 0){
                            couponList.add(item);
                        }
                    }else {
                        if (item.getShopId() == shopId && item.getStartTime().compareTo(LocalDateTime.now()) <= 0 && item.getEndTime().compareTo(LocalDateTime.now()) >= 0 && time.compareTo(LocalDateTime.now()) >= 0){
                            couponList.add(item);
                        }
                    }



                });
            }
        return R.ok().data("list", couponList);
    }
}

