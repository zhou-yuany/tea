package com.tea.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.*;
import com.tea.server.entity.vo.OrdersInfo;
import com.tea.server.entity.vo.ParamOrders;
import com.tea.server.mapper.*;
import com.tea.server.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单记录表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private OrderParamMapper orderParamMapper;

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private BatchUseMapper batchUseMapper;

    @Resource
    private GoodsToBatchMapper goodsToBatchMapper;


    @Resource
    private ShopToGoodsMapper shopToGoodsMapper;

    @Resource
    private ShopToParamMapper shopToParamMapper;

    @Resource
    private ReceiveCouponMapper receiveCouponMapper;

    @Resource
    private ParamMapper paramMapper;

    @Resource
    private InterfaceLogMapper interfaceLogMapper;



    @Override
    public Orders insertOrders(List<ParamOrders> params) {

        Shop shop = shopMapper.selectById(params.get(0).getShopId());
        // Date date = new Date();
        // LocalDateTime localDateTime =
        //         LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
        LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);

        Integer no = 0;

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders :: getStatus, 1).gt(Orders :: getCreateTime, startTime).lt(Orders :: getCreateTime, endTime).orderByDesc(Orders ::getId).last("limit 1");
        Orders obj = baseMapper.selectOne(queryWrapper);
        if (null != obj) {
            no = obj.getPickupCode() + 1;
        }




        //时间戳，
        String times = System.currentTimeMillis() + "";

        //订单编号（自定义 这里以时间戳+随机数）
        Random random = new Random();
        String did = times+random.nextInt(1000);
        Orders orders = new Orders();
        if (params.get(0).getOrderType() == 1){
            orders.setConsignee(params.get(0).getConsignee());
            orders.setReceiverPhone(params.get(0).getReceiverPhone());
            orders.setAddress(params.get(0).getAddress());
            orders.setSex(params.get(0).getSex());
        }
        orders.setIsAnonymous(params.get(0).getIsAnonymous());
        orders.setGiver(params.get(0).getGiver());
        orders.setGiverNotes(params.get(0).getGiverNotes());
        orders.setDeliveryDate(params.get(0).getDeliveryDate());
        orders.setOrderType(params.get(0).getOrderType());
        orders.setEquipmentType(params.get(0).getEquipmentType());
        orders.setOpenid(params.get(0).getOpenid());
        orders.setOrderNum(params.get(0).getOrderNo());
        orders.setOrderType(params.get(0).getOrderType());
        orders.setSerialNum("A" + did);
        // orders.setCouponId(params.get(0).getCouponId());
        orders.setNotes(params.get(0).getRemark());
        orders.setPhone(params.get(0).getPhone());
        orders.setTotalCount(params.get(0).getTotalCount());
        orders.setPickupCode(no);
        orders.setAdminId(shop.getAdminId());
        orders.setShopId(params.get(0).getShopId());
        orders.setTotalPrice(new BigDecimal(params.get(0).getTotalPrice()));
        orders.setCreateTime(LocalDateTime.now());
        orders.setUpdateTime(LocalDateTime.now());
        int insert = baseMapper.insert(orders);
        if (insert > 0) {
            params.stream().forEach(item -> {
                Integer count = Integer.valueOf(item.getGoodsCount());
                for (int i = 0; i < count; i++) {
                    OrderParam orderParam = new OrderParam();
                    orderParam.setOrderId(orders.getId());
                    orderParam.setName(item.getName());
                    orderParam.setNumber(1);
                    orderParam.setSpecifications(item.getCurSelect());
                    orderParam.setGoodsId(item.getGoodsId());
                    orderParam.setPrice(item.getPrice());
                    orderParam.setMakeStatus(0);
                    orderParam.setUrl(item.getUrl());
                    List<Long> paramIds = item.getParamIds();
                    LambdaQueryWrapper<Param> wrapper3 = new LambdaQueryWrapper<>();
                    wrapper3.eq(Param :: getStatus, 1).in(Param :: getId, paramIds);
                    List<Param> paramList = paramMapper.selectList(wrapper3);
                    paramList.stream().forEach(iii ->{
                        if (iii.getType() == 1){
                            orderParam.setSizeId(iii.getId());
                        }else if (iii.getType() == 2) {
                            orderParam.setSugarId(iii.getId());
                        }else if (iii.getType() == 3) {
                            orderParam.setTemperatureId(iii.getId());
                        }
                    });
                    ShopToGoods shopToGoods = shopToGoodsMapper.selectById(item.getGoodsId());
                    String equipmentId = shopToGoods.getEquipmentId();
                    orderParam.setEquipmentNo(equipmentId);
                    orderParam.setCreateTime(LocalDateTime.now());
                    orderParam.setUpdateTime(LocalDateTime.now());
                    orderParamMapper.insert(orderParam);
                }


                // ShopToGoods shopToGoods = shopToGoodsMapper.selectById(item.getGoodsId());
                // shopToGoods.setUseCount(shopToGoods.getUseCount() + Integer.valueOf(item.getGoodsCount()));
                // shopToGoods.setTotalCount(shopToGoods.getTotalCount() - Integer.valueOf(item.getGoodsCount()));
                // shopToGoods.setUpdateTime(LocalDateTime.now());
                // shopToGoodsMapper.updateById(shopToGoods);


            });
            // 优惠券已使用
            if (null != params.get(0).getCouponId()){
                LambdaQueryWrapper<ReceiveCoupon> receiveCouponWrapper = new LambdaQueryWrapper<>();
                receiveCouponWrapper.eq(ReceiveCoupon :: getStatus, 1).eq(ReceiveCoupon :: getOpenid, params.get(0).getOpenid()).eq(ReceiveCoupon :: getCouponId, params.get(0).getCouponId());
                ReceiveCoupon receiveCoupon = receiveCouponMapper.selectOne(receiveCouponWrapper);
                receiveCoupon.setIsUsed(1);
                receiveCoupon.setUpdateTime(LocalDateTime.now());
                receiveCouponMapper.updateById(receiveCoupon);
            }



        }
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setTitle("小程序创建订单");
        interfaceLog.setMethodName("insertOrders");
        String content = "用户创建订单，订单编号为"+orders.getOrderNum()+"，订单金额为："+orders.getTotalPrice();
        interfaceLog.setContent(content);
        interfaceLog.setShopId(shop.getId());
        interfaceLog.setTypeStatus(0);
        interfaceLog.setCreateTime(LocalDateTime.now());
        interfaceLog.setUpdateTime(LocalDateTime.now());
        interfaceLogMapper.insert(interfaceLog);

        return orders;
    }

    @Override
    public OrdersInfo getOrdersInfo(Long orderId) {
        Orders orders = baseMapper.selectById(orderId);
        OrdersInfo ordersInfo = new OrdersInfo();
        BeanUtils.copyProperties(orders, ordersInfo);
        LambdaQueryWrapper<OrderParam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderParam :: getStatus, 1).eq(OrderParam :: getOrderId, orderId);
        List<OrderParam> orderParams = orderParamMapper.selectList(queryWrapper);
        if (null != orderParams && orderParams.size() > 0){
            orderParams.stream().forEach(item -> {
                List<Long> paramIds = new ArrayList<>();
                paramIds.add(item.getSizeId());
                paramIds.add(item.getSugarId());
                paramIds.add(item.getTemperatureId());
                item.setParamIds(paramIds);
            });
        }
        ordersInfo.setParams(orderParams);
        Shop shop = shopMapper.selectById(orders.getShopId());
        ordersInfo.setShopName(shop.getName());
        ordersInfo.setShopId(orders.getShopId());
        ordersInfo.setShopProvince(shop.getProvince());
        ordersInfo.setShopCity(shop.getCity());
        ordersInfo.setShopArea(shop.getArea());
        ordersInfo.setShopAddress(shop.getAddress());
        ordersInfo.setShopPhone(shop.getPhone());
        ordersInfo.setLat(shop.getLat());
        ordersInfo.setLng(shop.getLng());
        return ordersInfo;
    }

    @Override
    public List<OrdersInfo> getOrdersList(String openid, Integer type) {
        List<Orders> orders = new ArrayList<>();
        LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
        if (type == 0){
            LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Orders :: getStatus, 1).eq(Orders :: getPayStatus, 2).eq(Orders :: getOpenid, openid).gt(Orders :: getCreateTime, startTime).lt(Orders :: getCreateTime, endTime).orderByDesc(Orders :: getId);
            orders = ordersMapper.selectList(queryWrapper);
        }else {
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Orders :: getStatus, 1).eq(Orders :: getPayStatus, 2).eq(Orders :: getOpenid, openid).le(Orders :: getCreateTime, startTime).orderByDesc(Orders :: getId);
            orders = ordersMapper.selectList(queryWrapper);
        }

        List<OrdersInfo> list = new ArrayList<>();
        orders.stream().forEach(item -> {
            Shop shop = shopMapper.selectById(item.getShopId());
            OrdersInfo ordersInfo = new OrdersInfo();
            BeanUtils.copyProperties(item, ordersInfo);
            LambdaQueryWrapper<OrderParam> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderParam :: getStatus, 1).eq(OrderParam :: getOrderId, item.getId());
            List<OrderParam> orderParams = orderParamMapper.selectList(wrapper);
            ordersInfo.setParams(orderParams);
            ordersInfo.setShopName(shop.getName());
            list.add(ordersInfo);
        });
        return list;
    }
}
