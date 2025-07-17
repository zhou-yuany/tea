package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Coupon;
import com.tea.server.entity.Goods;
import com.tea.server.entity.Shop;
import com.tea.server.entity.ShopToGoods;
import com.tea.server.entity.vo.CouponQuery;
import com.tea.server.service.CouponService;
import com.tea.server.service.ShopToGoodsService;
import com.tea.server.service.ShopsService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
 * @author zyy
 * @since 2023-08-25
 */
@RestController
@RequestMapping("coupon")
@CrossOrigin
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private ShopsService shopsService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    /**
     * 优惠券列表 分页查询
     */
    @PostMapping("getCoupons/{page}/{limit}")
    public R getCoupons(@PathVariable long page, @PathVariable long limit, @RequestBody(required = false) CouponQuery couponQuery){
        // 创建page对象
        Page<Coupon> couponPage = new Page<>(page, limit);

        // 构建条件
        Page<Coupon> pageList = couponService.pageCoupons(couponPage, couponQuery);

        long total = pageList.getTotal();
        List<Coupon> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 添加优惠券
     */
    @PostMapping("addCoupon")
    public R addCoupon(@RequestBody Coupon coupon){
        Coupon coupon1 = new Coupon();
        BeanUtils.copyProperties(coupon, coupon1);
        // if (coupon.getType() == 2){
            coupon1.setLimits(coupon.getParValue());
        // }
        coupon1.setCreateTime(LocalDateTime.now());
        coupon1.setUpdateTime(LocalDateTime.now());
        boolean save = couponService.save(coupon1);
        if (save) {
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 修改优惠券
     */
    @PostMapping("updateCoupon")
    public R updateCoupon(@RequestBody Coupon coupon){
        Coupon coupon1 = couponService.getById(coupon.getId());
        coupon1.setCount(coupon.getCount());
        coupon1.setPlatType(coupon.getPlatType());
        coupon1.setDays(coupon.getDays());
        coupon1.setLimits(coupon.getLimits());
        coupon1.setMonths(coupon.getMonths());
        coupon1.setName(coupon.getName());
        coupon1.setParValue(coupon.getParValue());
        coupon1.setStartTime(coupon.getStartTime());
        coupon1.setType(coupon.getType());
        coupon1.setShopId(coupon.getShopId());
        // if (coupon.getType() == 2){
            coupon1.setLimits(coupon.getParValue());
        // }
        coupon1.setEndTime(coupon.getEndTime());
        coupon1.setUpdateTime(LocalDateTime.now());
        boolean save = couponService.updateById(coupon1);
        if (save) {
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 查询所有店铺
     */
    @GetMapping("getShops")
    public R getShops(){
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop :: getStatus, 1);
        List<Shop> list = shopsService.list(queryWrapper);
        return R.ok().data("shopList", list);
    }

    /**
     * 查询优惠券详情
     */
    @GetMapping("getCouponInfo/{id}")
    public R getCouponInfo(@PathVariable Long id){
        Coupon coupon = couponService.getById(id);
        String shopGoodRange = coupon.getGoodRange();
        if (null != shopGoodRange && !shopGoodRange.equals("")) {
            List<Long> goodRangeList = Arrays.asList(shopGoodRange.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
            coupon.setGoodRangeList(goodRangeList);
        }
        return R.ok().data("info", coupon);
    }

    /**
     * 删除优惠券
     */
    @DeleteMapping("/{id}")
    public R removeCoupon(@PathVariable("id") Long id) {
        boolean b = couponService.removeById(id);
        if (b) {
            return R.ok();
        }else {
            return R.error();
        }
    }

    /**
     * 店铺优惠券列表 分页查询
     */
    @PostMapping("getShopCoupons/{page}/{limit}/{shopId}")
    public R getShopCoupons(@PathVariable long page, @PathVariable long limit, @PathVariable long shopId, @RequestBody(required = false) CouponQuery couponQuery){
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupon :: getStatus, 1).eq(Coupon :: getShopId, shopId);
        if (null != couponQuery.getType() && !couponQuery.getType().equals("")) {
            wrapper.like(Coupon :: getType, couponQuery.getType());
        }
        if (null != couponQuery.getBegin() && !couponQuery.getBegin().equals("")) {
            wrapper.ge(Coupon :: getStartTime, couponQuery.getBegin());
        }
        if (null != couponQuery.getEnd() && !couponQuery.getEnd().equals("")) {
            wrapper.le(Coupon :: getEndTime, couponQuery.getEnd());
        }
        wrapper.orderByDesc(Coupon :: getCreateTime);
        List<Coupon> list = couponService.list(wrapper);

        if (null != list && list.size() > 0){
            list.stream().forEach(item -> {
                String shopGoodRange = item.getGoodRange();
                if (null != shopGoodRange && !shopGoodRange.equals("")) {
                    List<Long> goodRangeList = Arrays.asList(shopGoodRange.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());

                    if (goodRangeList.size() > 0) {
                        List<String> goodsList = new ArrayList<>();
                        goodRangeList.stream().forEach(good -> {
                            ShopToGoods goods = shopToGoodsService.getById(good);
                            if (null != goods) {
                                goodsList.add(goods.getName());

                            }

                        });
                        String goodRange = goodsList.stream().map(String::valueOf).collect(Collectors.joining(","));
                        item.setGoodsName(goodRange);
                    }
                }
            });
        }


        int listSize = list.size();
        int startCount = Integer.parseInt((page - 1) * limit + "");
        int endCount = Integer.parseInt(page * limit + "");
        return R.ok().data("total", list.size()).data("rows", list.subList(startCount, listSize > endCount ? endCount : listSize));
    }

    /**
     * 商户修改优惠券
     */
    @PostMapping("updateShopCoupon")
    public R updateShopCoupon(@RequestBody Coupon coupon){
        List<Long> goodRangeList = coupon.getGoodRangeList();
        String goodRange = goodRangeList.stream().map(String::valueOf).collect(Collectors.joining(","));
        Coupon coupon2 = new Coupon();
        BeanUtils.copyProperties(coupon, coupon2);
        coupon2.setGoodRange(goodRange);
        boolean save = couponService.updateById(coupon2);
        if (save) {
            return R.ok();
        }else {
            return R.error();
        }
    }




}

