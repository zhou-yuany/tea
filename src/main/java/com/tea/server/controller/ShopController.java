package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.*;

import com.tea.server.entity.data.ShopCoupon;
import com.tea.server.entity.vo.CateGoods;
import com.tea.server.service.*;
import com.tea.server.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Slf4j
@RestController
@RequestMapping("shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategorizeService categorizeService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentAllService equipmentAllService;

    @Autowired
    private GoodsToBatchService goodsToBatchService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ReceiveCouponService receiveCouponService;
    /**
     * 判断时间是否在时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        return nowTime.getTime() >= beginTime.getTime() && nowTime.getTime() <= endTime.getTime();
    }


    // 获取店家详情
    @GetMapping("getShopInfo")
    public R getShopInfo(@RequestParam("shopId") Long shopId){
        Shop shop = shopService.getById(shopId);
        // if (null != shop.getOnStartTime() && null != shop.getOnEndTime()){
        //     SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
        //     try {
        //         Date now = df.parse(df.format(new Date()));
        //         Date startTime = df.parse(shop.getOnStartTime());
        //         Date endTime = df.parse(shop.getOnEndTime());
        //         boolean isWithinRange = belongCalendar(now, startTime, endTime);
        //         shop.setIsBusiness(isWithinRange ? 1 : 2);
        //
        //     } catch (ParseException e) {
        //         e.printStackTrace();
        //     }
        //
        // }
        List<String> equipmentIds = Arrays.stream(shop.getEquipmentId().split(",")).collect(Collectors.toList());
        LambdaQueryWrapper<EquipmentAll> equipmentAllWrapper = new LambdaQueryWrapper<>();
        equipmentAllWrapper.eq(EquipmentAll :: getStatus, 1).eq(EquipmentAll :: getEquipmentNo, equipmentIds.get(0));
        EquipmentAll equipmentAll = equipmentAllService.getOne(equipmentAllWrapper);
        shop.setEquipmentType(equipmentAll.getType());
        return R.ok().data("info", shop);
    }

    // 获取店家
    @GetMapping("getShopList")
    public R getShopList(){
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop :: getStatus, 1).eq(Shop :: getIsUse,1);
        List<Shop> list = shopService.list(wrapper);
        if (null != list && list.size() > 0){
            list.stream().forEach(item -> {
                if (null != item.getOnStartTime() && null != item.getOnEndTime()){
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
                    try {
                        Date now = df.parse(df.format(new Date()));
                        Date startTime = df.parse(item.getOnStartTime());
                        Date endTime = df.parse(item.getOnEndTime());
                        boolean isWithinRange = belongCalendar(now, startTime, endTime);
                        item.setIsBusiness(isWithinRange ? 1 : 2);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                LambdaQueryWrapper<Equipment> equipmentWrapper = new LambdaQueryWrapper<>();
                equipmentWrapper.eq(Equipment :: getStatus, 1).eq(Equipment :: getShopId, item.getId()).last("limit 1");
                Equipment equipment = equipmentService.getOne(equipmentWrapper);
                item.setEquipmentType(equipment.getType());
            });
        }
        return R.ok().data("list", list);
    }

    /**
     * 根据商户id获取商品分类及商品
     *
     */
    @GetMapping("getGoodsById/{id}")
    public R getGoodsById(@PathVariable Long id, @RequestParam String openid){


        // 向用户下发商品优惠券
        if(null != openid && !openid.equals("")){
            LambdaQueryWrapper<Coupon> couponWrapper = new LambdaQueryWrapper<>();
            couponWrapper.eq(Coupon :: getStatus, 1).eq(Coupon :: getShopId, id);
            List<Coupon> list2 = couponService.list(couponWrapper);


            if (null != list2 && list2.size() > 0){
                list2.stream().forEach(coupon -> {

                    Integer days = coupon.getDays();
                    Integer months = coupon.getMonths();
                    if(months > 0){
                        days = days + months * 30;
                    }
                    LocalDateTime time = LocalDateTime.now().plusDays(days);

                    if (coupon.getStartTime().compareTo(LocalDateTime.now()) <= 0 && coupon.getEndTime().compareTo(LocalDateTime.now()) >= 0 && time.compareTo(LocalDateTime.now()) >= 0){

                        Integer count = coupon.getCount();


                        LambdaQueryWrapper<ReceiveCoupon> receiveWrapper = new LambdaQueryWrapper<>();
                        receiveWrapper.eq(ReceiveCoupon :: getStatus, 1).eq(ReceiveCoupon :: getShopId, id).eq(ReceiveCoupon :: getCouponId, coupon.getId());
                        List<ReceiveCoupon> receiveCoupons = receiveCouponService.list(receiveWrapper);
                        if (null != receiveCoupons && receiveCoupons.size() > 0){

                            if (receiveCoupons.size() < count){

                                int i = count - receiveCoupons.size();
                                for (int j = 0; j < i; j++) {

                                    ReceiveCoupon couponUser = new ReceiveCoupon();
                                    couponUser.setCouponId(coupon.getId());
                                    couponUser.setShopId(id);
                                    couponUser.setDays(coupon.getDays());
                                    couponUser.setOpenid(openid);
                                    couponUser.setName(coupon.getName());
                                    couponUser.setStartTime(coupon.getStartTime());
                                    couponUser.setEndTime(coupon.getEndTime());
                                    couponUser.setLimits(coupon.getLimits());
                                    couponUser.setMonths(coupon.getMonths());
                                    couponUser.setPlatType(coupon.getPlatType());
                                    couponUser.setType(coupon.getType());
                                    couponUser.setParValue(coupon.getParValue());
                                    couponUser.setCreateTime(LocalDateTime.now());
                                    couponUser.setUpdateTime(LocalDateTime.now());
                                    couponUser.setGoodRange(coupon.getGoodRange());
                                    receiveCouponService.save(couponUser);
                                }
                                if (!coupon.getGoodRange().equals(receiveCoupons.get(0).getGoodRange())){

                                    for (int k = 0; k < receiveCoupons.size(); k++) {
                                        ReceiveCoupon couponUser = new ReceiveCoupon();
                                        BeanUtils.copyProperties(receiveCoupons.get(k), couponUser);
                                        couponUser.setCouponId(coupon.getId());
                                        couponUser.setDays(coupon.getDays());
                                        couponUser.setName(coupon.getName());
                                        couponUser.setStartTime(coupon.getStartTime());
                                        couponUser.setEndTime(coupon.getEndTime());
                                        couponUser.setLimits(coupon.getLimits());
                                        couponUser.setMonths(coupon.getMonths());
                                        couponUser.setPlatType(coupon.getPlatType());
                                        couponUser.setType(coupon.getType());
                                        couponUser.setParValue(coupon.getParValue());
                                        couponUser.setCreateTime(LocalDateTime.now());
                                        couponUser.setUpdateTime(LocalDateTime.now());
                                        couponUser.setGoodRange(coupon.getGoodRange());
                                        receiveCouponService.updateById(couponUser);
                                    }
                                }


                            }else {
                                if (!coupon.getGoodRange().equals(receiveCoupons.get(0).getGoodRange())){

                                    receiveCoupons.stream().forEach(receiveCoupon -> {
                                        ReceiveCoupon couponUser = new ReceiveCoupon();
                                        BeanUtils.copyProperties(receiveCoupon, couponUser);
                                        couponUser.setCouponId(coupon.getId());
                                        couponUser.setDays(coupon.getDays());
                                        couponUser.setName(coupon.getName());
                                        couponUser.setStartTime(coupon.getStartTime());
                                        couponUser.setEndTime(coupon.getEndTime());
                                        couponUser.setLimits(coupon.getLimits());
                                        couponUser.setMonths(coupon.getMonths());
                                        couponUser.setPlatType(coupon.getPlatType());
                                        couponUser.setType(coupon.getType());
                                        couponUser.setParValue(coupon.getParValue());
                                        couponUser.setCreateTime(LocalDateTime.now());
                                        couponUser.setUpdateTime(LocalDateTime.now());
                                        couponUser.setGoodRange(coupon.getGoodRange());
                                        receiveCouponService.updateById(couponUser);
                                    });
                                }

                            }

                        }else {

                            for (int i = 0; i < count; i++) {

                                ReceiveCoupon couponUser = new ReceiveCoupon();
                                couponUser.setCouponId(coupon.getId());
                                couponUser.setShopId(id);
                                couponUser.setDays(coupon.getDays());
                                couponUser.setOpenid(openid);
                                couponUser.setName(coupon.getName());
                                couponUser.setStartTime(coupon.getStartTime());
                                couponUser.setEndTime(coupon.getEndTime());
                                couponUser.setLimits(coupon.getLimits());
                                couponUser.setMonths(coupon.getMonths());
                                couponUser.setPlatType(coupon.getPlatType());
                                couponUser.setType(coupon.getType());
                                couponUser.setParValue(coupon.getParValue());
                                couponUser.setCreateTime(LocalDateTime.now());
                                couponUser.setUpdateTime(LocalDateTime.now());
                                couponUser.setGoodRange(coupon.getGoodRange());
                                receiveCouponService.save(couponUser);
                            }

                        }
                    }

                });
            }
        }


        LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Categorize::getShopId, id).eq(Categorize::getStatus, 1);
        List<Categorize> list = categorizeService.list(wrapper);

        List<CateGoods> shopToGoods = new ArrayList<>();


        if (null != list && list.size() > 0){



            list.stream().forEach(item -> {
                CateGoods cateGoods = new CateGoods();
                BeanUtils.copyProperties(item, cateGoods);
                LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ShopToGoods::getShopId, id).eq(ShopToGoods::getCateId, item.getId()).eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getIsGrounding, 1);
                List<ShopToGoods> toGoods = shopToGoodsService.list(queryWrapper);
                if(null != toGoods && toGoods.size() > 0){


                            toGoods.stream().forEach(good ->{
                                // 查询商品是否有优惠券
                                String goodId = String.valueOf(good.getId());

                                LambdaQueryWrapper<GoodsToBatch> wrapper1 = new LambdaQueryWrapper<>();
                                wrapper1.eq(GoodsToBatch :: getShopId, id).eq(GoodsToBatch :: getStatus,1).eq(GoodsToBatch :: getGoodsId, good.getId());
                                List<GoodsToBatch> list1 = goodsToBatchService.list(wrapper1);
                                if (null != list1 && list1.size() > 0){
                                    List<BigDecimal> numbers = list1.stream().map(iii -> iii.getPrice()).collect(Collectors.toList());
                                    if (null != numbers && numbers.size() > 0) {
                                        BigDecimal min = numbers.stream().filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(new BigDecimal(0));
                                        LambdaQueryWrapper<ReceiveCoupon> receiveCouponWrapper = new LambdaQueryWrapper<>();
                                        receiveCouponWrapper.eq(ReceiveCoupon :: getStatus, 1).eq(ReceiveCoupon :: getShopId, id).eq(ReceiveCoupon :: getIsUsed, 2).like(ReceiveCoupon :: getGoodRange, goodId).orderByDesc(ReceiveCoupon :: getId);
                                        List<ReceiveCoupon> receiveCouponList = receiveCouponService.list(receiveCouponWrapper);

                                        if (null != receiveCouponList && receiveCouponList.size() > 0){
                                            ReceiveCoupon ranger = receiveCouponList.get(0);
                                            Integer days = ranger.getDays();
                                            Integer months = ranger.getMonths();
                                            if(months > 0){
                                                days = days + months * 30;
                                            }
                                            LocalDateTime time = LocalDateTime.now().plusDays(days);
                                            if ( ranger.getGoodRange().contains(goodId) && ranger.getStartTime().compareTo(LocalDateTime.now()) <= 0 && ranger.getEndTime().compareTo(LocalDateTime.now()) >= 0 && time.compareTo(LocalDateTime.now()) >= 0){

                                                BigDecimal subtract = min.subtract(ranger.getParValue());

                                                BigDecimal p = subtract.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtract;

                                                good.setIsCoupon(1);
                                                good.setPrice(p);
                                                good.setParValue(ranger.getParValue());
                                            }else {
                                                good.setPrice(min);
                                            }

                                        }else {
                                            good.setPrice(min);
                                        }


                                    }

                                }

                            });

                 



                }
                cateGoods.setGoodsList(toGoods);
                shopToGoods.add(cateGoods);

            });
        }

        Shop shop = shopService.getById(id);
        // if (null != shop.getOnStartTime() && null != shop.getOnEndTime()){
        //     LocalTime startTime = LocalTime.parse(shop.getOnStartTime());
        //     LocalTime endTime = LocalTime.parse(shop.getOnEndTime());
        //     LocalTime currentTime = LocalTime.now();
        //
        //     boolean isWithinRange = !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
        //     shop.setIsBusiness(isWithinRange ? 1 : 2);
        // }

        LambdaQueryWrapper<Equipment> equipmentWrapper = new LambdaQueryWrapper<>();
        equipmentWrapper.eq(Equipment :: getStatus, 1).eq(Equipment :: getShopId, id).last("limit 1");
        Equipment equipment = equipmentService.getOne(equipmentWrapper);
        shop.setEquipmentType(equipment.getType());

        return R.ok().data("list", shopToGoods).data("shop", shop);

    }

    /**
     * 根据扫码获取商品分类及商品
     *
     */
    @GetMapping("getGoodsByCode/{equipmentId}")
    public R getGoodsByCode(@PathVariable Long equipmentId, @RequestParam String openid){



        List<CateGoods> shopToGoods = new ArrayList<>();
        Equipment equipment = equipmentService.getById(equipmentId);

        // 向用户下发商品优惠券
        if(null != openid && !openid.equals("")){
            LambdaQueryWrapper<Coupon> couponWrapper = new LambdaQueryWrapper<>();
            couponWrapper.eq(Coupon :: getStatus, 1).eq(Coupon :: getShopId, equipment.getShopId());
            List<Coupon> list2 = couponService.list(couponWrapper);



            if (null != list2 && list2.size() > 0){
                list2.stream().forEach(coupon -> {

                    Integer days = coupon.getDays();
                    Integer months = coupon.getMonths();
                    if(months > 0){
                        days = days + months * 30;
                    }
                    LocalDateTime time = LocalDateTime.now().plusDays(days);

                    if (coupon.getStartTime().compareTo(LocalDateTime.now()) <= 0 && coupon.getEndTime().compareTo(LocalDateTime.now()) >= 0 && time.compareTo(LocalDateTime.now()) >= 0){

                        Integer count = coupon.getCount();
                        LambdaQueryWrapper<ReceiveCoupon> receiveWrapper = new LambdaQueryWrapper<>();
                        receiveWrapper.eq(ReceiveCoupon :: getStatus, 1).eq(ReceiveCoupon :: getShopId, coupon.getShopId()).eq(ReceiveCoupon :: getCouponId, coupon.getId());;
                        List<ReceiveCoupon> receiveCoupons = receiveCouponService.list(receiveWrapper);
                        if (null != receiveCoupons && receiveCoupons.size() > 0){
                            if (receiveCoupons.size() < count){
                                int i = count - receiveCoupons.size();
                                for (int j = 0; j < i; j++) {
                                    ReceiveCoupon couponUser = new ReceiveCoupon();
                                    couponUser.setCouponId(coupon.getId());
                                    couponUser.setShopId(coupon.getShopId());
                                    couponUser.setDays(coupon.getDays());
                                    couponUser.setOpenid(openid);
                                    couponUser.setName(coupon.getName());
                                    couponUser.setStartTime(coupon.getStartTime());
                                    couponUser.setEndTime(coupon.getEndTime());
                                    couponUser.setLimits(coupon.getLimits());
                                    couponUser.setMonths(coupon.getMonths());
                                    couponUser.setPlatType(coupon.getPlatType());
                                    couponUser.setType(coupon.getType());
                                    couponUser.setParValue(coupon.getParValue());
                                    couponUser.setCreateTime(LocalDateTime.now());
                                    couponUser.setUpdateTime(LocalDateTime.now());
                                    couponUser.setGoodRange(coupon.getGoodRange());
                                    receiveCouponService.save(couponUser);
                                }
                                if (!coupon.getGoodRange().equals(receiveCoupons.get(0).getGoodRange())){
                                    for (int k = 0; k < receiveCoupons.size(); k++) {
                                        ReceiveCoupon couponUser = new ReceiveCoupon();
                                        BeanUtils.copyProperties(receiveCoupons.get(k), couponUser);
                                        couponUser.setCouponId(coupon.getId());
                                        couponUser.setDays(coupon.getDays());
                                        couponUser.setName(coupon.getName());
                                        couponUser.setStartTime(coupon.getStartTime());
                                        couponUser.setEndTime(coupon.getEndTime());
                                        couponUser.setLimits(coupon.getLimits());
                                        couponUser.setMonths(coupon.getMonths());
                                        couponUser.setPlatType(coupon.getPlatType());
                                        couponUser.setType(coupon.getType());
                                        couponUser.setParValue(coupon.getParValue());
                                        couponUser.setCreateTime(LocalDateTime.now());
                                        couponUser.setUpdateTime(LocalDateTime.now());
                                        couponUser.setGoodRange(coupon.getGoodRange());
                                        receiveCouponService.updateById(couponUser);
                                    }
                                }


                            }else {
                                if (!coupon.getGoodRange().equals(receiveCoupons.get(0).getGoodRange())){
                                    receiveCoupons.stream().forEach(receiveCoupon -> {
                                        ReceiveCoupon couponUser = new ReceiveCoupon();
                                        BeanUtils.copyProperties(receiveCoupon, couponUser);
                                        couponUser.setCouponId(coupon.getId());
                                        couponUser.setDays(coupon.getDays());
                                        couponUser.setName(coupon.getName());
                                        couponUser.setStartTime(coupon.getStartTime());
                                        couponUser.setEndTime(coupon.getEndTime());
                                        couponUser.setLimits(coupon.getLimits());
                                        couponUser.setMonths(coupon.getMonths());
                                        couponUser.setPlatType(coupon.getPlatType());
                                        couponUser.setType(coupon.getType());
                                        couponUser.setParValue(coupon.getParValue());
                                        couponUser.setCreateTime(LocalDateTime.now());
                                        couponUser.setUpdateTime(LocalDateTime.now());
                                        couponUser.setGoodRange(coupon.getGoodRange());
                                        receiveCouponService.updateById(couponUser);
                                    });
                                }

                            }


                        }else {

                            for (int i = 0; i < count; i++) {
                                ReceiveCoupon couponUser = new ReceiveCoupon();
                                couponUser.setCouponId(coupon.getId());
                                couponUser.setShopId(coupon.getShopId());
                                couponUser.setDays(coupon.getDays());
                                couponUser.setOpenid(openid);
                                couponUser.setName(coupon.getName());
                                couponUser.setStartTime(coupon.getStartTime());
                                couponUser.setEndTime(coupon.getEndTime());
                                couponUser.setLimits(coupon.getLimits());
                                couponUser.setMonths(coupon.getMonths());
                                couponUser.setPlatType(coupon.getPlatType());
                                couponUser.setType(coupon.getType());
                                couponUser.setParValue(coupon.getParValue());
                                couponUser.setCreateTime(LocalDateTime.now());
                                couponUser.setUpdateTime(LocalDateTime.now());
                                couponUser.setGoodRange(coupon.getGoodRange());
                                receiveCouponService.save(couponUser);
                            }
                        }
                    }



                });
            }
        }



        // 查看自动还是手动
        // LambdaQueryWrapper<EquipmentAll> equipmentAllWrapper = new LambdaQueryWrapper<>();
        // equipmentAllWrapper.eq(EquipmentAll :: getStatus, 1).eq(EquipmentAll :: getEquipmentNo, equipment.getEquipmentNo());
        // EquipmentAll equipmentAll = equipmentAllService.getOne(equipmentAllWrapper);

        Shop shop = shopService.getById(equipment.getShopId());

        // if (null != shop.getOnStartTime() && null != shop.getOnEndTime()){
        //     LocalTime startTime = LocalTime.parse(shop.getOnStartTime());
        //     LocalTime endTime = LocalTime.parse(shop.getOnEndTime());
        //     LocalTime currentTime = LocalTime.now();
        //
        //     boolean isWithinRange = !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
        //     shop.setIsBusiness(isWithinRange ? 1 : 2);
        // }

        LambdaQueryWrapper<ShopToGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopToGoods::getEquipmentId, equipment.getEquipmentNo()).eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getIsGrounding, 1);
        List<ShopToGoods> toGoods = shopToGoodsService.list(queryWrapper);

        if (null != toGoods && toGoods.size() > 0){



            List<Long> cateIds = toGoods.stream().map(item -> item.getCateId()).distinct().collect(Collectors.toList());
            if (null != cateIds && cateIds.size() > 0){
                cateIds.stream().forEach(cateId ->{
                    Categorize categorize = categorizeService.getById(cateId);
                    CateGoods cateGoods = new CateGoods();
                    BeanUtils.copyProperties(categorize, cateGoods);
                    LambdaQueryWrapper<ShopToGoods> goodsWrapper = new LambdaQueryWrapper<>();
                    goodsWrapper.eq(ShopToGoods :: getCateId, cateId).eq(ShopToGoods::getEquipmentId, equipment.getEquipmentNo()).eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getIsGrounding, 1);
                    List<ShopToGoods> goods = shopToGoodsService.list(goodsWrapper);
                    cateGoods.setGoodsList(goods);
                    shopToGoods.add(cateGoods);

                            goods.stream().forEach(good ->{
                                // 查询商品是否有优惠券
                                String goodId = String.valueOf(good.getId());

                                LambdaQueryWrapper<GoodsToBatch> wrapper1 = new LambdaQueryWrapper<>();
                                wrapper1.eq(GoodsToBatch :: getShopId, equipment.getShopId()).eq(GoodsToBatch :: getStatus,1).eq(GoodsToBatch :: getGoodsId, good.getId());
                                List<GoodsToBatch> list1 = goodsToBatchService.list(wrapper1);
                                if (null != list1 && list1.size() > 0){
                                    List<BigDecimal> numbers = list1.stream().map(iii -> iii.getPrice()).collect(Collectors.toList());
                                    if (numbers.size() > 0) {
                                        BigDecimal min = numbers.stream().filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(new BigDecimal(0));
                                        LambdaQueryWrapper<ReceiveCoupon> receiveCouponWrapper = new LambdaQueryWrapper<>();
                                        receiveCouponWrapper.eq(ReceiveCoupon :: getStatus, 1).eq(ReceiveCoupon :: getShopId, equipment.getShopId()).eq(ReceiveCoupon :: getIsUsed, 2).like(ReceiveCoupon :: getGoodRange, goodId).orderByDesc(ReceiveCoupon :: getId);
                                        List<ReceiveCoupon> receiveCouponList = receiveCouponService.list(receiveCouponWrapper);

                                        if (null != receiveCouponList && receiveCouponList.size() > 0){
                                            ReceiveCoupon ranger = receiveCouponList.get(0);
                                            Integer days = ranger.getDays();
                                            Integer months = ranger.getMonths();
                                            if(months > 0){
                                                days = days + months * 30;
                                            }
                                            LocalDateTime time = LocalDateTime.now().plusDays(days);
                                            if ( ranger.getGoodRange().contains(goodId) && ranger.getStartTime().compareTo(LocalDateTime.now()) <= 0 && ranger.getEndTime().compareTo(LocalDateTime.now()) >= 0 && time.compareTo(LocalDateTime.now()) >= 0){

                                                BigDecimal subtract = min.subtract(ranger.getParValue());

                                                BigDecimal p = subtract.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtract;

                                                good.setIsCoupon(1);
                                                good.setPrice(p);
                                                good.setParValue(ranger.getParValue());
                                            }else {
                                                good.setPrice(min);
                                            }

                                        }else {
                                            good.setPrice(min);
                                        }

                                    }

                                }

                            });

                });


            }
        }



        return R.ok().data("list", shopToGoods).data("shop", shop).data("equipmentType", equipment.getType());

    }

}

