package com.tea.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.*;
import com.tea.server.entity.query.OrderQuery;
import com.tea.server.entity.vo.OrdersAndParam;
import com.tea.server.entity.vo.OrdersAndShopParam;
import com.tea.server.entity.vo.OrdersCall;
import com.tea.server.entity.vo.OutletVo;
import com.tea.server.mapper.*;
import com.tea.server.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author zyy
 * @since 2023-08-24
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private OrderParamMapper orderParamMapper;

    @Resource
    private GoodsToBatchMapper goodsToBatchMapper;

    @Resource
    private BatchUseMapper batchUseMapper;

    @Resource
    private ShopToGoodsMapper shopToGoodsMapper;

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private BatchingMapper batchingMapper;

    @Resource
    private OrdersFlowMapper ordersFlowMapper;

    @Resource
    private ShopToParamMapper shopToParamMapper;

    @Resource
    private InterfaceLogMapper interfaceLogMapper;

    /**
     * 为了测试暂时注释
     * @param params
     * @param orderNo
     * @param type
     * @param shopId
     * @param adminId
     * @return
     */

    // @Override
    // public Orders insertOrders(List<OrdersAndParam> params, String orderNo, Integer type, Long shopId, Long adminId) {
    //
    //
    //     LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
    //     LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);
    //
    //     Integer no = 1000;
    //
    //     LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(Orders :: getStatus, 1).gt(Orders :: getCreateTime, startTime).lt(Orders :: getCreateTime, endTime).orderByDesc(Orders ::getId).last("limit 1");
    //     Orders obj = baseMapper.selectOne(queryWrapper);
    //     if (null != obj) {
    //         no = obj.getPickupCode() + 1;
    //     }
    //     int count = params.stream().mapToInt(OrdersAndParam::getSelectCount).sum();
    //     BigDecimal totalPrice = new BigDecimal(0);
    //
    //     try {
    //         for (int i = 0; i < params.size(); i++) {
    //             BigDecimal multiply = params.get(i).getPrice().multiply(BigDecimal.valueOf(params.get(i).getSelectCount()));
    //             totalPrice = totalPrice.add(multiply);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } finally {
    //         Orders orders = new Orders();
    //         orders.setOrderNum(orderNo);
    //         orders.setPayType(type);
    // //时间戳，
    // String times = System.currentTimeMillis() + "";
    // //订单编号（自定义 这里以时间戳+随机数）
    // Random random = new Random();
    // String did = times+random.nextInt(1000);
    // orders.setSerialNum("A" + did);
    //         orders.setOrderType(1);
    //         orders.setTotalCount(count);
    //         orders.setPickupCode(no);
    //         orders.setAdminId(adminId);
    //         orders.setShopId(shopId);
    //         orders.setTotalPrice(totalPrice);
    //         orders.setCreateTime(LocalDateTime.now());
    //         orders.setUpdateTime(LocalDateTime.now());
    //         int insert = baseMapper.insert(orders);
    //         if (insert > 0) {
    //             params.stream().forEach(item -> {
    //                 Goods goods1 = goodsMapper.selectById(item.getGoodsId());
    //                 StringBuilder sb = new StringBuilder();
    //                 item.getSelectParams().forEach(pa -> sb.append(pa.getName() + '，'));
    //                 List<Long> paramIds = item.getSelectParams().stream().map(pp -> pp.getId()).collect(Collectors.toList());
    //                 String str = sb.substring(0, sb.length() - 1);
    //                 System.out.println(sb);
    //                 OrderParam orderParam = new OrderParam();
    //                 orderParam.setOrderId(orders.getId());
    //                 orderParam.setName(item.getGoodsName());
    //                 orderParam.setNumber(item.getSelectCount());
    //                 orderParam.setSpecifications(str);
    //                 orderParam.setGoodsId(item.getGoodsId());
    //                 orderParam.setPrice(item.getPrice());
    //                 orderParam.setUrl(goods1.getUrl());
    //                 orderParam.setCreateTime(LocalDateTime.now());
    //                 orderParam.setUpdateTime(LocalDateTime.now());
    //                 orderParamMapper.insert(orderParam);
    //                 // 消耗配方库存 固定
    //                 LambdaQueryWrapper<GoodsToBatch> goodWrapper = new LambdaQueryWrapper<>();
    //                 goodWrapper.eq(GoodsToBatch :: getGoodsId, item.getGoodsId()).eq(GoodsToBatch :: getStatus, 1).in(GoodsToBatch :: getParamId, paramIds);
    //                 List<GoodsToBatch> goodsToBatches = goodsToBatchMapper.selectList(goodWrapper);
    //                 if (goodsToBatches.size() > 0){
    //                     goodsToBatches.stream().forEach(goods -> {
    //                         LambdaQueryWrapper<BatchUse> batchWrapper = new LambdaQueryWrapper<>();
    //                         batchWrapper.eq(BatchUse :: getStatus, 1).eq(BatchUse :: getShopId, shopId).eq(BatchUse :: getBatchId, goods.getBatchId());
    //                         BatchUse batchUse = batchUseMapper.selectOne(batchWrapper);
    //                         if (null != batchUse){
    //                             batchUse.setUseCount(batchUse.getUseCount() + goods.getUseNumber() * Integer.valueOf(item.getSelectCount()));
    //                             batchUse.setTotalCount(batchUse.getTotalCount() - (goods.getUseNumber() * Integer.valueOf(item.getSelectCount())));
    //                             batchUse.setUpdateTime(LocalDateTime.now());
    //                             batchUseMapper.updateById(batchUse);
    //                         }
    //
    //                     });
    //
    //
    //                 }
    //                 // 商品剩余数量
    //                 LambdaQueryWrapper<ShopToGoods> shopWrap = new LambdaQueryWrapper<>();
    //                 shopWrap.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getGoodsId, item.getGoodsId()).eq(ShopToGoods :: getShopId, shopId);
    //                 ShopToGoods shopToGoods = shopToGoodsMapper.selectOne(shopWrap);
    //                 shopToGoods.setUseCount(shopToGoods.getUseCount() + Integer.valueOf(item.getSelectCount()));
    //                 shopToGoods.setTotalCount(shopToGoods.getTotalCount() - Integer.valueOf(item.getSelectCount()));
    //                 shopToGoods.setUpdateTime(LocalDateTime.now());
    //                 shopToGoodsMapper.updateById(shopToGoods);
    //
    //
    //
    //             });
    //
    //         }
    //         return orders;
    //     }
    //
    //
    // }


    /**
     * 给客户展示使用
     *
     * @param params
     * @param orderNo
     * @param type    // * @param shopId
     * @param adminId
     * @return
     */
    // @Override
    // public Orders insertOrders(List<OrdersAndParam> params, String orderNo, Integer type, Long shopId, Long adminId) {
    //
    //
    //     LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
    //     LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);
    //
    //     Integer no = 1000;
    //
    //     LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(Orders :: getStatus, 1).gt(Orders :: getCreateTime, startTime).lt(Orders :: getCreateTime, endTime).orderByDesc(Orders ::getId).last("limit 1");
    //     Orders obj = baseMapper.selectOne(queryWrapper);
    //     if (null != obj) {
    //         no = obj.getPickupCode() + 1;
    //     }
    //     int count = params.stream().mapToInt(OrdersAndParam::getSelectCount).sum();
    //     BigDecimal totalPrice = new BigDecimal(0);
    //
    //     try {
    //         for (int i = 0; i < params.size(); i++) {
    //             BigDecimal multiply = params.get(i).getPrice().multiply(BigDecimal.valueOf(params.get(i).getSelectCount()));
    //             totalPrice = totalPrice.add(multiply);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } finally {
    // //时间戳，
    // String times = System.currentTimeMillis() + "";
    // //订单编号（自定义 这里以时间戳+随机数）
    // Random random = new Random();
    // String did = times+random.nextInt(1000);
    // orders.setSerialNum("A" + did);
    //         Orders orders = new Orders();
    //         orders.setOrderNum(orderNo);
    //         orders.setPayType(type);
    //         orders.setOrderType(1);
    //         orders.setTotalCount(count);
    //         orders.setPickupCode(no);
    //         orders.setAdminId(adminId);
    //         orders.setShopId(shopId);
    //         orders.setPayStatus(2);
    //         orders.setTotalPrice(totalPrice);
    //         orders.setCreateTime(LocalDateTime.now());
    //         orders.setUpdateTime(LocalDateTime.now());
    //         int insert = baseMapper.insert(orders);
    //         if (insert > 0) {
    //             params.stream().forEach(item -> {
    //                 Goods goods1 = goodsMapper.selectById(item.getGoodsId());
    //                 StringBuilder sb = new StringBuilder();
    //                 item.getSelectParams().forEach(pa -> sb.append(pa.getName() + '，'));
    //                 List<Long> paramIds = item.getSelectParams().stream().map(pp -> pp.getId()).collect(Collectors.toList());
    //                 String str = sb.substring(0, sb.length() - 1);
    //                 System.out.println(sb);
    //                 OrderParam orderParam = new OrderParam();
    //                 orderParam.setOrderId(orders.getId());
    //                 orderParam.setName(item.getGoodsName());
    //                 orderParam.setNumber(item.getSelectCount());
    //                 orderParam.setSpecifications(str);
    //                 orderParam.setGoodsId(item.getGoodsId());
    //                 orderParam.setPrice(item.getPrice());
    //                 orderParam.setUrl(goods1.getUrl());
    //                 orderParam.setCreateTime(LocalDateTime.now());
    //                 orderParam.setUpdateTime(LocalDateTime.now());
    //                 orderParamMapper.insert(orderParam);
    //                 // 消耗配方库存 固定
    //                 LambdaQueryWrapper<GoodsToBatch> goodWrapper = new LambdaQueryWrapper<>();
    //                 goodWrapper.eq(GoodsToBatch :: getGoodsId, item.getGoodsId()).eq(GoodsToBatch :: getStatus, 1).in(GoodsToBatch :: getParamId, paramIds);
    //                 List<GoodsToBatch> goodsToBatches = goodsToBatchMapper.selectList(goodWrapper);
    //                 if (goodsToBatches.size() > 0){
    //                     goodsToBatches.stream().forEach(goods -> {
    //                         LambdaQueryWrapper<BatchUse> batchWrapper = new LambdaQueryWrapper<>();
    //                         batchWrapper.eq(BatchUse :: getStatus, 1).eq(BatchUse :: getShopId, shopId).eq(BatchUse :: getBatchId, goods.getBatchId());
    //                         BatchUse batchUse = batchUseMapper.selectOne(batchWrapper);
    //                         if (null != batchUse){
    //                             batchUse.setUseCount(batchUse.getUseCount() + goods.getUseNumber() * Integer.valueOf(item.getSelectCount()));
    //                             batchUse.setTotalCount(batchUse.getTotalCount() - (goods.getUseNumber() * Integer.valueOf(item.getSelectCount())));
    //                             batchUse.setUpdateTime(LocalDateTime.now());
    //                             batchUseMapper.updateById(batchUse);
    //                         }
    //
    //                     });
    //
    //
    //                 }
    //                 // 商品剩余数量
    //                 LambdaQueryWrapper<ShopToGoods> shopWrap = new LambdaQueryWrapper<>();
    //                 shopWrap.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getGoodsId, item.getGoodsId()).eq(ShopToGoods :: getShopId, shopId);
    //                 ShopToGoods shopToGoods = shopToGoodsMapper.selectOne(shopWrap);
    //                 shopToGoods.setUseCount(shopToGoods.getUseCount() + Integer.valueOf(item.getSelectCount()));
    //                 shopToGoods.setTotalCount(shopToGoods.getTotalCount() - Integer.valueOf(item.getSelectCount()));
    //                 shopToGoods.setUpdateTime(LocalDateTime.now());
    //                 shopToGoodsMapper.updateById(shopToGoods);
    //
    //
    //
    //             });
    //
    //         }
    //         return orders;
    //     }
    //
    //
    // }
    @Override
    public Orders insertOrders(List<OrdersAndParam> params, String orderNo, Integer type, Long adminId) {
        LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
        LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);

        Integer no = 1000;

        LambdaQueryWrapper<Shop> queryWrapper5 = new LambdaQueryWrapper<>();
        queryWrapper5.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
        Shop one = shopMapper.selectOne(queryWrapper5);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 1).gt(Orders::getCreateTime, startTime).lt(Orders::getCreateTime, endTime).orderByDesc(Orders::getId).last("limit 1");
        Orders obj = baseMapper.selectOne(queryWrapper);
        if (null != obj) {
            no = obj.getPickupCode() + 1;
        }
        int count = params.stream().mapToInt(OrdersAndParam::getSelectCount).sum();
        BigDecimal totalPrice = new BigDecimal(0);

        try {
            for (int i = 0; i < params.size(); i++) {
                BigDecimal multiply = params.get(i).getPrice().multiply(BigDecimal.valueOf(params.get(i).getSelectCount()));
                totalPrice = totalPrice.add(multiply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //时间戳，
            String times = System.currentTimeMillis() + "";
            //订单编号（自定义 这里以时间戳+随机数）
            Random random = new Random();
            String did = times + random.nextInt(1000);
            Orders orders = new Orders();
            orders.setOrderNum(orderNo);
            orders.setPayType(type);
            orders.setOrderType(1);
            orders.setTotalCount(count);
            orders.setSerialNum("A" + did);
            orders.setPickupCode(no);
            orders.setAdminId(adminId);
            orders.setShopId(one.getId());
            orders.setPayStatus(2);
            orders.setTotalPrice(totalPrice);
            orders.setCreateTime(LocalDateTime.now());
            orders.setUpdateTime(LocalDateTime.now());
            int insert = baseMapper.insert(orders);
            if (insert > 0) {

                params.stream().forEach(item -> {
                    ShopToGoods goods1 = shopToGoodsMapper.selectById(item.getGoodsId());
                    StringBuilder sb = new StringBuilder();
                    item.getSelectParams().forEach(pa -> sb.append(pa.getName() + '，'));
                    List<Long> paramIds = item.getSelectParams().stream().map(pp -> pp.getId()).collect(Collectors.toList());
                    String str = sb.substring(0, sb.length() - 1);
                    OrderParam orderParam = new OrderParam();
                    orderParam.setOrderId(orders.getId());
                    orderParam.setName(item.getGoodsName());
                    orderParam.setNumber(item.getSelectCount());
                    orderParam.setSpecifications(str);
                    orderParam.setGoodsId(item.getGoodsId());
                    orderParam.setPrice(item.getPrice());
                    orderParam.setUrl(goods1.getUrl());
                    List<Param> selectParams = item.getSelectParams();
                    selectParams.stream().forEach(eee ->{
                        if (eee.getType() == 1){
                            orderParam.setSizeId(eee.getId());
                        }else if(eee.getType() == 2){
                            orderParam.setSugarId(eee.getId());
                        }else if (eee.getType() == 3) {
                            orderParam.setTemperatureId(eee.getId());
                        }

                    });

                    orderParam.setCreateTime(LocalDateTime.now());
                    orderParam.setUpdateTime(LocalDateTime.now());
                    orderParamMapper.insert(orderParam);

                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("竖屏下单成功");
                    interfaceLog.setMethodName("startOrders");
                    String content = "竖屏下单成功，订单编号为"+orders.getOrderNum();
                    interfaceLog.setContent(content);
                    interfaceLog.setShopId(orders.getShopId());
                    interfaceLog.setTypeStatus(0);
                    interfaceLog.setCreateTime(LocalDateTime.now());
                    interfaceLog.setUpdateTime(LocalDateTime.now());
                    interfaceLogMapper.insert(interfaceLog);
                    // 消耗配方库存 固定
                    // LambdaQueryWrapper<GoodsToBatch> goodWrapper = new LambdaQueryWrapper<>();
                    // goodWrapper.eq(GoodsToBatch::getGoodsId, item.getGoodsId()).eq(GoodsToBatch::getStatus, 1).in(GoodsToBatch::getParamId, paramIds);
                    // List<GoodsToBatch> goodsToBatches = goodsToBatchMapper.selectList(goodWrapper);
                    // if (goodsToBatches.size() > 0) {
                    //     goodsToBatches.stream().forEach(goods -> {
                    //         LambdaQueryWrapper<BatchUse> batchWrapper = new LambdaQueryWrapper<>();
                    //         batchWrapper.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, shopId[0]).eq(BatchUse::getBatchId, goods.getBatchId());
                    //         BatchUse batchUse = batchUseMapper.selectOne(batchWrapper);
                    //         if (null != batchUse) {
                    //             batchUse.setUseCount(batchUse.getUseCount() + goods.getUseNumber() * Integer.valueOf(item.getSelectCount()));
                    //             batchUse.setTotalCount(batchUse.getTotalCount() - (goods.getUseNumber() * Integer.valueOf(item.getSelectCount())));
                    //             batchUse.setUpdateTime(LocalDateTime.now());
                    //             batchUseMapper.updateById(batchUse);
                    //         }
                    //
                    //     });
                    //
                    //
                    // }
                    // 商品剩余数量
                    // LambdaQueryWrapper<ShopToGoods> shopWrap = new LambdaQueryWrapper<>();
                    // shopWrap.eq(ShopToGoods :: getStatus, 1).eq(ShopToGoods :: getGoodsId, item.getGoodsId()).eq(ShopToGoods :: getShopId, shopId[0]);
                    // ShopToGoods shopToGoods = shopToGoodsMapper.selectOne(shopWrap);
                    // shopToGoods.setUseCount(shopToGoods.getUseCount() + Integer.valueOf(item.getSelectCount()));
                    // shopToGoods.setTotalCount(shopToGoods.getTotalCount() - Integer.valueOf(item.getSelectCount()));
                    // shopToGoods.setUpdateTime(LocalDateTime.now());
                    // shopToGoodsMapper.updateById(shopToGoods);

                    // goods1.setUseCount(goods1.getUseCount() + Integer.valueOf(item.getSelectCount()));
                    // goods1.setTotalCount(goods1.getTotalCount() - Integer.valueOf(item.getSelectCount()));
                    // goods1.setUpdateTime(LocalDateTime.now());
                    // shopToGoodsMapper.updateById(goods1);


                });

            }
            return orders;
        }
    }

    @Override
    public Page<OrdersCall> callOrders(Page<OrdersCall> ordersPage, Long shopId) {
        Page<OrdersCall> list = baseMapper.callOrders(ordersPage, shopId);
        return list;
    }

    @Override
    public Orders startOrders(OrdersAndShopParam params, String orderNo, Integer type, Long adminId) {
        LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
        LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);

        Integer no = 1000;

        LambdaQueryWrapper<Shop> queryWrapper5 = new LambdaQueryWrapper<>();
        queryWrapper5.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
        Shop one = shopMapper.selectOne(queryWrapper5);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 1).gt(Orders::getCreateTime, startTime).lt(Orders::getCreateTime, endTime).orderByDesc(Orders::getId).last("limit 1");
        Orders obj = baseMapper.selectOne(queryWrapper);
        if (null != obj) {
            no = obj.getPickupCode() + 1;
        }
        int count = 1;
        BigDecimal totalPrice = params.getPrice();
        //时间戳，
        String times = System.currentTimeMillis() + "";
        //订单编号（自定义 这里以时间戳+随机数）
        Random random = new Random();
        String did = times + random.nextInt(1000);
        Orders orders = new Orders();
        orders.setOrderNum(orderNo);
        orders.setPayType(type);
        orders.setOrderType(1);
        orders.setSerialNum("A" + did);
        orders.setTotalCount(count);
        orders.setPickupCode(no);
        orders.setAdminId(adminId);
        orders.setShopId(one.getId());
        orders.setPayStatus(2);
        orders.setPlatformType(4);
        orders.setOrderStatus(2);
        orders.setTotalPrice(totalPrice);
        orders.setCreateTime(LocalDateTime.now());
        orders.setUpdateTime(LocalDateTime.now());
        int insert = baseMapper.insert(orders);
        if (insert > 0) {
            // 新增流水
            OrdersFlow ordersFlow = new OrdersFlow();
            ordersFlow.setAdminId(adminId);
            ordersFlow.setShopId(one.getId());
            ordersFlow.setOrderId(orders.getId());
            ordersFlow.setPrice(totalPrice);
            ordersFlow.setGoodsId(params.getGoodsId());
            ordersFlow.setPayType(type);
            ordersFlow.setType(1);
            ordersFlow.setNumber(count);
            ordersFlow.setPayPlatform(3);
            ordersFlow.setShopDistributionPrice(totalPrice);
            ordersFlow.setSerialNum("A" + did);
            ordersFlow.setGoodsName(params.getGoodsName());
            ordersFlow.setCreateTime(LocalDateTime.now());
            ordersFlow.setUpdateTime(LocalDateTime.now());
            ordersFlowMapper.insert(ordersFlow);

            Shop shop = new Shop();
            BeanUtils.copyProperties(one, shop);
            shop.setBalance(one.getBalance().add(totalPrice));
            shopMapper.updateById(shop);

            ShopToGoods shopToGoods = shopToGoodsMapper.selectById(params.getGoodsId());
            StringBuilder sb = new StringBuilder();
            params.getSelectParams().forEach(pa -> sb.append(pa.getName() + '，'));
            // List<Long> paramIds = params.getSelectParams().stream().map(pp -> pp.getId()).collect(Collectors.toList());
            OrderParam orderParam = new OrderParam();
            List<ShopToParam> toParams = params.getSelectParams();
            if (null != toParams && toParams.size() > 0){
                toParams.stream().forEach(aaa ->{
                    if (aaa.getType() == 1){
                        orderParam.setSizeId(aaa.getParamId());
                    }else if (aaa.getType() == 2){
                        orderParam.setSugarId(aaa.getParamId());
                    }else if (aaa.getType() == 3){
                        orderParam.setTemperatureId(aaa.getParamId());
                    }
                });
            }
            String str = sb.substring(0, sb.length() - 1);

            orderParam.setOrderId(orders.getId());
            orderParam.setName(params.getGoodsName());
            orderParam.setNumber(1);
            orderParam.setMakeStatus(2);
            orderParam.setSpecifications(str);
            orderParam.setGoodsId(params.getGoodsId());
            orderParam.setPrice(params.getPrice());
            orderParam.setUrl(shopToGoods.getUrl());
            orderParam.setCreateTime(LocalDateTime.now());
            orderParam.setUpdateTime(LocalDateTime.now());
            orderParamMapper.insert(orderParam);
            // 消耗配方库存 固定
            // LambdaQueryWrapper<GoodsToBatch> goodWrapper = new LambdaQueryWrapper<>();
            // goodWrapper.eq(GoodsToBatch::getGoodsId, params.getGoodsId()).eq(GoodsToBatch::getStatus, 1);
            // List<GoodsToBatch> goodsToBatches = goodsToBatchMapper.selectList(goodWrapper);
            // if (goodsToBatches.size() > 0) {
            //     goodsToBatches.stream().forEach(goods -> {
            //         LambdaQueryWrapper<BatchUse> batchWrapper = new LambdaQueryWrapper<>();
            //         batchWrapper.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, one.getId()).eq(BatchUse::getBatchId, goods.getBatchId());
            //         BatchUse batchUse = batchUseMapper.selectOne(batchWrapper);
            //         if (null != batchUse) {
            //             batchUse.setUseCount(batchUse.getUseCount() + goods.getUseNumber() * Integer.valueOf(params.getSelectCount()));
            //             batchUse.setTotalCount(batchUse.getTotalCount() - (goods.getUseNumber() * Integer.valueOf(params.getSelectCount())));
            //             batchUse.setUpdateTime(LocalDateTime.now());
            //             batchUseMapper.updateById(batchUse);
            //         }
            //
            //     });
            //
            //
            // }
            // 规格配方消耗
            // List<ShopToParam> selectParams = params.getSelectParams();
            // if (null != selectParams && selectParams.size() > 0) {
            //     selectParams.stream().forEach(item -> {
            //         Long paramId = item.getParamId();
            //         LambdaQueryWrapper<ShopToParam> query1 = new LambdaQueryWrapper<>();
            //         query1.eq(ShopToParam::getStatus, 1).eq(ShopToParam::getParamId, paramId).eq(ShopToParam::getShopId, one.getId());
            //         ShopToParam shopToParam = shopToParamMapper.selectOne(query1);
            //         if (null != shopToParam) {
            //             LambdaQueryWrapper<Batching> query3 = new LambdaQueryWrapper<>();
            //             query3.eq(Batching::getStatus, 1);
            //             if (item.getType() == 3 && item.getName().indexOf("冰") > -1) {
            //                 query3.eq(Batching::getName, "冰");
            //             } else if (item.getType() == 3 && item.getName().indexOf("热") > -1) {
            //                 query3.eq(Batching::getName, "热");
            //             } else {
            //                 query3.eq(Batching::getName, item.getType() == 1 ? "水" : "糖");
            //             }
            //             Batching batching = batchingMapper.selectOne(query3);
            //             if (null != batching) {
            //                 LambdaQueryWrapper<BatchUse> query2 = new LambdaQueryWrapper<>();
            //                 query2.eq(BatchUse::getStatus, 1).eq(BatchUse::getBatchId, batching.getId()).eq(BatchUse::getShopId, one.getId());
            //                 BatchUse batchUse = batchUseMapper.selectOne(query2);
            //                 batchUse.setUseCount(batchUse.getUseCount() + shopToParam.getUseNumber());
            //                 batchUse.setTotalCount(batchUse.getTotalCount() - (shopToParam.getUseNumber()));
            //                 batchUse.setUpdateTime(LocalDateTime.now());
            //                 batchUseMapper.updateById(batchUse);
            //             }
            //         }
            //
            //     });
            // }

            // 商品剩余数量
            // LambdaQueryWrapper<ShopToGoods> shopWrap = new LambdaQueryWrapper<>();
            // shopWrap.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getGoodsId, params.getGoodsId()).eq(ShopToGoods::getShopId, one.getId());
            // ShopToGoods shopToGoods = shopToGoodsMapper.selectOne(shopWrap);
            // shopToGoods.setUseCount(shopToGoods.getUseCount() + Integer.valueOf(params.getSelectCount()));
            // shopToGoods.setTotalCount(shopToGoods.getTotalCount() - Integer.valueOf(params.getSelectCount()));
            // shopToGoods.setUpdateTime(LocalDateTime.now());
            // shopToGoodsMapper.updateById(shopToGoods);
        }
        return orders;

    }

    @Override
    public List<OutletVo> getOutletList(Long id, Long goodsId) {
        List<OutletVo> list = new ArrayList<>();
        LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, goodsId);
        List<GoodsToBatch> goodsToBatches = goodsToBatchMapper.selectList(queryWrapper);
        if (null != goodsToBatches && goodsToBatches.size() > 0) {
            goodsToBatches.stream().forEach(goods -> {
                Batching batching = batchingMapper.selectById(goods.getBatchId());
                OutletVo outletVo = new OutletVo();
                outletVo.setUseNumber(goods.getUseNumber());
                outletVo.setOutlet(batching.getOutlet());
                list.add(outletVo);
            });
        }
        return list;
    }

    @Override
    public Page<Orders> pageAllOrders(Page<Orders> ordersPage, OrderQuery orderQuery) {
        Page<Orders> list = baseMapper.pageAllOrders(ordersPage, orderQuery);
        return list;
    }


}
