package com.tea.server.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import com.tea.server.entity.*;
import com.tea.server.entity.vo.OrdersInfo;
import com.tea.server.entity.vo.OutletVo;
import com.tea.server.entity.vo.ParamOrders;
import com.tea.server.service.*;
import com.tea.server.socket.CodeData;
import com.tea.server.socket.DeviceWebSocket;
import com.tea.server.socket.PlatformWebSocket;
import com.tea.server.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单记录表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Slf4j
@RestController
@RequestMapping("orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersFlowService ordersFlowService;

    @Autowired
    private OrderParamService orderParamService;

    @Autowired
    private GoodsToBatchService goodsToBatchService;

    @Autowired
    private BatchUseService batchUseService;

    @Autowired
    private BatchingService batchingService;

    @Autowired
    private AgentsService agentsService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private BatchOrderUseService batchOrderUseService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ReceiveCouponService receiveCouponService;


    // 新增订单
    @PostMapping("insertOrders")
    public R insertOrders(@RequestBody String params){
        JSONArray jsonArray = JSON.parseArray(params);
        List<ParamOrders> list = jsonArray.toJavaList(ParamOrders.class);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders :: getStatus, 1).eq(Orders :: getOrderNum, list.get(0).getOrderNo()).eq(Orders :: getOpenid, list.get(0).getOpenid());
        Orders one = ordersService.getOne(queryWrapper);

        if (null == one) {
            Orders orders = ordersService.insertOrders(list);
            return R.ok().data("orders", orders);
        }else {
            return R.ok().data("orders", one);
        }

    }

    // 支付成功改变订单状态
    @PostMapping("updateOrders/{id}")
    public R updateOrders(@PathVariable Long id){

        Orders orders = ordersService.getById(id);
        orders.setUpdateTime(LocalDateTime.now());
        orders.setPayStatus(2);
        // orders.setCouponId(couponId);
        ordersService.updateById(orders);

        String openid = orders.getOpenid();
        LambdaQueryWrapper<Users> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(Users :: getOpenid, openid);
        Users one = usersService.getOne(userQueryWrapper);
        Users users = new Users();
        BeanUtils.copyProperties(one, users);
        users.setIsNew(2);
        usersService.updateById(users);

        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setTitle("小程序订单支付成功");
        interfaceLog.setMethodName("updateOrders");
        String content = "小程序订单支付成功，订单编号为"+orders.getOrderNum()+"订单金额为："+orders.getTotalPrice();
        interfaceLog.setContent(content);
        interfaceLog.setShopId(orders.getShopId());
        interfaceLog.setTypeStatus(0);
        interfaceLog.setCreateTime(LocalDateTime.now());
        interfaceLog.setUpdateTime(LocalDateTime.now());
        interfaceLogService.save(interfaceLog);

        // PlatformWebSocket.sendOneMessage(orders.getAdminId(), new CodeData("ok", "您有新订单啦"));

        LambdaQueryWrapper<OrderParam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, orders.getId());
        List<OrderParam> list = orderParamService.list(queryWrapper);

        // final int[] count = {0};
        // final String[] paramName = {""};
        List<String> paramList = new ArrayList<>();
        // 新增流水
        if (null != list && list.size() > 0){
            list.stream().forEach(item -> {

                // 查询配料是否充足
                LambdaQueryWrapper<GoodsToBatch> goodWrapper = new LambdaQueryWrapper<>();
                goodWrapper.eq(GoodsToBatch :: getShopId, orders.getShopId()).eq(GoodsToBatch :: getGoodsId, item.getGoodsId()).eq(GoodsToBatch :: getStatus, 1).eq(GoodsToBatch :: getSizeId, item.getSizeId()).eq(GoodsToBatch :: getSugarId, item.getSugarId()).eq(GoodsToBatch :: getTemperatureId, item.getTemperatureId());
                List<GoodsToBatch> goodsToBatches = goodsToBatchService.list(goodWrapper);
                if (goodsToBatches.size() > 0){
                    goodsToBatches.stream().forEach(goods -> {
                        LambdaQueryWrapper<BatchUse> batchWrapper = new LambdaQueryWrapper<>();
                        batchWrapper.eq(BatchUse :: getStatus, 1).eq(BatchUse :: getShopId, orders.getShopId()).eq(BatchUse :: getBatchId, goods.getBatchId());
                        BatchUse batchUse = batchUseService.getOne(batchWrapper);
                        if (null != batchUse){
                            if (batchUse.getTotalCount() < goods.getUseNumber()){
                                Batching batching = batchingService.getById(batchUse.getBatchId());
                                // count[0] = 1;
                                // paramName[0] += paramName[0] + batching.getName() + "、";
                                paramList.add(batching.getName());
                            }

                        }

                    });


                }
            });
        }
        // if (orders.getEquipmentType() != 2){
            String message = "您有新订单啦";
            if (paramList.size() > 0){
                String paramName = paramList.stream() // 将List转换为Stream
                        .distinct() // 去除重复元素
                        .collect(Collectors.joining(","));
                message = paramName+"不足";
                // message = paramName[0].substring(0, paramName[0].length() - 1) + "配料不足";
                // Arrays.stream(paramName).distinct().toArray();
            }
            log.info("message:"+message);
            PlatformWebSocket.sendOneMessage(orders.getAdminId(), new CodeData("ok", message));
            DeviceWebSocket.sendOneMessage(orders.getShopId(), new CodeData("ok", "下单"));
        // }



        List<Long> ids = new ArrayList<>();
        ids.add(orders.getShopId());
        LambdaQueryWrapper<Agents> wrapper5 = new LambdaQueryWrapper<>();
        wrapper5.eq(Agents :: getStatus, 1).in(Agents :: getShopId, ids).eq(Agents :: getIsUse, 1);
        Agents agentsServiceOne = agentsService.getOne(wrapper5);

        // 添加流水
        OrdersFlow ordersFlow = new OrdersFlow();
        ordersFlow.setAdminId(orders.getAdminId());
        ordersFlow.setShopId(orders.getShopId());
        ordersFlow.setOrderId(orders.getId());


        ordersFlow.setPrice(orders.getTotalPrice());
        ordersFlow.setPayType(1);
        ordersFlow.setType(1);
        ordersFlow.setPayStatus(1);
        ordersFlow.setOutTradeNo(orders.getOutTradeNo());
        ordersFlow.setNumber(orders.getTotalCount());
        ordersFlow.setSerialNum(orders.getOrderNum());
        // String goodsName = list.stream().map(item -> item.getName()).collect(Collectors.joining(","));
        // ordersFlow.setGoodsName(goodsName);

        Shop shopServiceById = shopService.getById(orders.getShopId());
        BigDecimal price = new BigDecimal(0L);
        BigDecimal price2 = new BigDecimal(0L);
        InterfaceLog interfaceLog2 = new InterfaceLog();
        String content2 = "小程序分账";
        Shop shop2 = new Shop();
        BeanUtils.copyProperties(shopServiceById, shop2);
        if (shopServiceById.getDivideAccounts() == 1){
            BigDecimal aa = BigDecimal.valueOf(shopServiceById.getProportion()).divide(BigDecimal.valueOf(100));

            price =  orders.getTotalPrice().multiply(aa);

            ordersFlow.setShopDistributionPrice(price.setScale(2, RoundingMode.HALF_UP));



            shop2.setBalance(shopServiceById.getBalance().add(price.setScale(2, RoundingMode.HALF_UP)));

            interfaceLog2.setShopId(orders.getShopId());
            content2 = content2+"，商户入账金额为"+price.setScale(2, RoundingMode.HALF_UP)+"元";

            if (null != agentsServiceOne){
                ordersFlow.setAgentId(agentsServiceOne.getId());
                if (agentsServiceOne.getDivideAccounts() == 1){
                    BigDecimal bb = BigDecimal.valueOf(agentsServiceOne.getProportion()).divide(BigDecimal.valueOf(100));
                    price2 =  orders.getTotalPrice().multiply(bb);
                    ordersFlow.setDistributionPrice(price2.setScale(2, RoundingMode.HALF_UP));

                    content2 = content2+"，代理入账金额为"+price2.setScale(2, RoundingMode.HALF_UP)+"元";
                }
            }
            ordersFlow.setPlatDistributionPrice(orders.getTotalPrice().subtract(price).subtract(price2).setScale(2, RoundingMode.HALF_UP));

        }else {
            shop2.setBalance(shopServiceById.getBalance().add(orders.getTotalPrice()));
            content2 = content2+"，商户入账金额为"+orders.getTotalPrice().setScale(2, RoundingMode.HALF_UP)+"元";

        }
        shopService.updateById(shop2);



        ordersFlow.setCreateTime(LocalDateTime.now());
        ordersFlow.setUpdateTime(LocalDateTime.now());
        ordersFlowService.save(ordersFlow);


        interfaceLog2.setTitle("小程序分账");
        interfaceLog2.setMethodName("updateOrders");
        content2 = content2+",平台入账"+orders.getTotalPrice().subtract(price).subtract(price2).setScale(2, RoundingMode.HALF_UP)+"元";
        interfaceLog2.setContent(content2);
        interfaceLog2.setTypeStatus(0);
        interfaceLog2.setShopId(orders.getShopId());
        interfaceLog2.setCreateTime(LocalDateTime.now());
        interfaceLog2.setUpdateTime(LocalDateTime.now());
        interfaceLogService.save(interfaceLog2);


        return R.ok();
    }



    // 订单详情
    @GetMapping("getOrdersInfo")
    public R getOrdersInfo(@RequestParam("orderId") Long orderId){
        OrdersInfo info = ordersService.getOrdersInfo(orderId);
        Long couponId = info.getCouponId();
        if (null != couponId && couponId > 0){

            ReceiveCoupon coupon = receiveCouponService.getById(couponId);

            info.setParValue(coupon.getParValue());
        }

        return R.ok().data("info", info);
    }

    // 订单列表
    @GetMapping("getOrdersList")
    public R getOrdersList(@RequestParam("openid") String openid, @RequestParam("type") Integer type){
        List<OrdersInfo> list = ordersService.getOrdersList(openid, type);
        return R.ok().data("list", list);
    }

    // 茶饮机器 订单商品开始制作
    @GetMapping("makeParam/{id}")
    public R makeParam(@PathVariable Long id) {

        OrderParam orderParam = orderParamService.getById(id);

        Orders orders = ordersService.getById(orderParam.getOrderId());

        // 查询是否添加过配方
        LambdaQueryWrapper<GoodsToBatch> batchWrapper = new LambdaQueryWrapper<>();
        batchWrapper.eq(GoodsToBatch :: getStatus, 1).eq(GoodsToBatch :: getShopId, orders.getShopId()).eq(GoodsToBatch :: getGoodsId, orderParam.getGoodsId()).eq(GoodsToBatch :: getSizeId, orderParam.getSizeId()).eq(GoodsToBatch :: getSugarId, orderParam.getSugarId()).eq(GoodsToBatch :: getTemperatureId, orderParam.getTemperatureId());
        List<GoodsToBatch> goodsToBatchList = goodsToBatchService.list(batchWrapper);
        if (null != goodsToBatchList && goodsToBatchList.size() > 0){

            List<Integer> numbers = new ArrayList<>();
            List<String> paramList = new ArrayList<>();

            HashMap<Object, Object> arr = new HashMap<>();
            HashMap<Object, Integer> arr1 = new HashMap<>();

            goodsToBatchList.stream().forEach(batch -> {
                // 配料
                // OutletVo outletVo = new OutletVo();
                // Integer useNumber = batch.getUseNumber();
                // outletVo.setUseNumber(useNumber);
                LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                BatchUse batchUse = batchUseService.getOne(wrapper3);

                // if (null != batchUse) {
                //     arr.put(batchUse.getOutlet() + "T", batch.getUseNumber());
                //     arr1.put(batchUse.getOutlet(), 1);
                // }
                if (batchUse.getTotalCount() < batch.getUseNumber()){
                    Batching batching = batchingService.getById(batchUse.getBatchId());
                    paramList.add(batching.getName());
                }else {
                    // BatchUse batchUse1 = new BatchUse();
                    // BeanUtils.copyProperties(batchUse, batchUse1);
                    // batchUse1.setTotalCount(batchUse.getTotalCount() - batch.getUseNumber());
                    // batchUse1.setUseCount(batchUse.getUseCount() + batch.getUseNumber());
                    // batchUseService.updateById(batchUse1);
                    // numbers.add((batch.getUseNumber() / 10) + 1);
                    //
                    // BatchOrderUse batchOrderUse = new BatchOrderUse();
                    // batchOrderUse.setOrderId(orders.getId());
                    // batchOrderUse.setOrderNum(orders.getOrderNum());
                    // batchOrderUse.setBatchId(batchUse1.getBatchId());
                    // batchOrderUse.setUseCount(batch.getUseNumber());
                    // batchOrderUse.setShopId(batchUse1.getShopId());
                    // batchOrderUse.setOutlet(batchUse1.getOutlet());
                    // batchOrderUse.setCreateTime(LocalDateTime.now());
                    // batchOrderUse.setUpdateTime(LocalDateTime.now());
                    // batchOrderUseService.save(batchOrderUse);
                }
            });
            if (paramList.size() > 0){
                String paramName = paramList.stream() // 将List转换为Stream
                        .distinct() // 去除重复元素
                        .collect(Collectors.joining(","));
                String messageInfo = paramName+"不足";
                PlatformWebSocket.sendOneMessage(orders.getAdminId(), new CodeData("ok", messageInfo));
                return R.error().message(messageInfo+",请联系负责人添加");

            }else {

                goodsToBatchList.stream().forEach(aaa ->{
                    // 配料
                    OutletVo outletVo = new OutletVo();
                    Integer useNumber = aaa.getUseNumber();
                    outletVo.setUseNumber(useNumber);
                    LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                    wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, aaa.getBatchId());
                    BatchUse batchUse = batchUseService.getOne(wrapper3);
                    if (null != batchUse) {
                        outletVo.setOutlet(batchUse.getOutlet());
                        arr.put(batchUse.getOutlet() + "T", aaa.getUseNumber());
                        arr1.put(batchUse.getOutlet(), 1);
                        BatchUse batchUse1 = new BatchUse();
                        BeanUtils.copyProperties(batchUse, batchUse1);
                        batchUse1.setTotalCount(batchUse.getTotalCount() - aaa.getUseNumber());
                        batchUse1.setUseCount(batchUse.getUseCount() + aaa.getUseNumber());
                        batchUseService.updateById(batchUse1);
                        numbers.add((aaa.getUseNumber() / 10) + 1);

                        BatchOrderUse batchOrderUse = new BatchOrderUse();
                        batchOrderUse.setOrderId(orders.getId());
                        batchOrderUse.setOrderNum(orders.getOrderNum());
                        batchOrderUse.setBatchId(batchUse1.getBatchId());
                        batchOrderUse.setUseCount(aaa.getUseNumber());
                        batchOrderUse.setShopId(batchUse1.getShopId());
                        batchOrderUse.setOutlet(batchUse1.getOutlet());
                        batchOrderUse.setCreateTime(LocalDateTime.now());
                        batchOrderUse.setUpdateTime(LocalDateTime.now());
                        batchOrderUseService.save(batchOrderUse);
                    }
                });

                int max = 0;
                if (numbers.size() > 0) {
                    max = Collections.max(numbers);
                }

                // 链接机器*************************************
                String equipmentId = orderParam.getEquipmentNo(); // 712bc4e815e24c289a4b31f3e62731fb
                LocalDateTime expireTime = LocalDateTime.now();
                String mToekn = null;
                // 获取连接机器的token
                // 请求body参数
                Gson gson = new Gson();
                //调用统一下单API
                HttpPost httpPost = new HttpPost("http://enterpriseapi.gizwits.com/v1/products/712bc4e815e24c289a4b31f3e62731fb/access_token");
                HashMap<Object, Object> paramsMap = new HashMap<>();
                paramsMap.put("enterprise_id", "458e865ae79a47499836fa7b0be21425");
                paramsMap.put("enterprise_secret", "dc2fb31c26f8413d829330fc9015c692");
                paramsMap.put("product_secret", "4c57847f8e694cfbb6c06e365ef01a15");
                //将参数转化未json字符串
                String jsonParamsMap = gson.toJson(paramsMap);
                log.info("请求参数：" + jsonParamsMap);

                StringEntity entity = new StringEntity(jsonParamsMap, "utf-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");

                //完成签名并执行请求
                CloseableHttpResponse resp = null;
                try {

                    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                    resp = httpClient.execute(httpPost);
                    int statusCode = resp.getStatusLine().getStatusCode();
                    log.info("statusCode***************************************："+statusCode);
                    String bodyAsString = EntityUtils.toString(resp.getEntity());
                    if (statusCode == 201) { //处理成功
                        log.info("成功，返回结果 = " + bodyAsString);
                        //相应结果
                        HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
                        //获取机器的token
                        mToekn = resultMap.get("token");

                        // 请求机器 1 配料以及时间


                        HttpPost httpPost2 = new HttpPost("http://enterpriseapi.gizwits.com/v1/products/712bc4e815e24c289a4b31f3e62731fb/devices/" + equipmentId + "/control");

                        // // 创建存储 HashMap 的 ArrayList
                        // ArrayList<HashMap<Object, Object>> arrayList = new ArrayList<>();
                        // arrayList.add(arr);
                        //
                        // // 将 ArrayList 转换为数组
                        // HashMap<String, String>[] array = new HashMap[arrayList.size()];
                        // arrayList.toArray(array);
                        HashMap<String, Object> aaa = new HashMap<>();
                        aaa.put("attrs", arr);


                        //将参数转化未json字符串
                        String jsonParamsMap2 = gson.toJson(aaa);
                        log.info("请求参数11111：" + jsonParamsMap2);

                        StringEntity entity2 = new StringEntity(jsonParamsMap2, "utf-8");
                        entity2.setContentType("application/json");
                        httpPost2.setEntity(entity2);
                        httpPost2.setHeader("Accept", "application/json");
                        httpPost2.setHeader("Authorization", mToekn);
                        resp = httpClient.execute(httpPost2);
                        int statusCode2 = resp.getStatusLine().getStatusCode();
                        log.info("statusCode2222 = " + statusCode2);
                        String bodyAsString2 = EntityUtils.toString(resp.getEntity());
                        log.info("机器请求结果222222222 = " + bodyAsString2);
                        if (statusCode2 == 200) { //处理成功
                            log.info("机器请求结果 = " + bodyAsString2);

                            // 请求机器 2 制作
                            HttpPost httpPost3 = new HttpPost("http://enterpriseapi.gizwits.com/v1/products/712bc4e815e24c289a4b31f3e62731fb/devices/" + equipmentId + "/control");

                            // // 创建存储 HashMap 的 ArrayList
                            // ArrayList<HashMap<Object, Object>> arrayList = new ArrayList<>();
                            // arrayList.add(arr);
                            //
                            // // 将 ArrayList 转换为数组
                            // HashMap<String, String>[] array = new HashMap[arrayList.size()];
                            // arrayList.toArray(array);
                            HashMap<String, Object> bbb = new HashMap<>();
                            bbb.put("attrs", arr1);


                            //将参数转化未json字符串
                            String jsonParamsMap3 = gson.toJson(bbb);
                            log.info("请求参数222222：" + jsonParamsMap3);

                            StringEntity entity3 = new StringEntity(jsonParamsMap3, "utf-8");
                            entity3.setContentType("application/json");
                            httpPost3.setEntity(entity3);
                            httpPost3.setHeader("Accept", "application/json");
                            httpPost3.setHeader("Authorization", mToekn);
                            resp = httpClient.execute(httpPost3);
                            int statusCode3 = resp.getStatusLine().getStatusCode();
                            log.info("statusCode333333 = " + statusCode3);
                            String bodyAsString3 = EntityUtils.toString(resp.getEntity());
                            log.info("机器请求结果333333 = " + bodyAsString3);
                            if (statusCode3 == 200) { //处理成功
                                log.info("机器请求结果222222 = " + bodyAsString3);
                                OrderParam orderParam1 = new OrderParam();
                                BeanUtils.copyProperties(orderParam, orderParam1);
                                orderParam1.setMakeStatus(2);
                                orderParam1.setUpdateTime(LocalDateTime.now());
                                orderParamService.updateById(orderParam1);
                                LambdaQueryWrapper<OrderParam> wrapper8 = new LambdaQueryWrapper<>();
                                wrapper8.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, orderParam.getOrderId()).ne(OrderParam::getMakeStatus, 2);
                                List<OrderParam> list1 = orderParamService.list(wrapper8);
                                Orders orders2 = new Orders();
                                BeanUtils.copyProperties(orders, orders2);
                                if (null == list1 || list1.size() == 0) {

                                    orders2.setUpdateTime(LocalDateTime.now());
                                    orders2.setOrderStatus(2);

                                } else {
                                    orders2.setUpdateTime(LocalDateTime.now());
                                    orders2.setOrderStatus(1);
                                }
                                ordersService.updateById(orders2);

                            }

                        }

                    }
                    resp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InterfaceLog interfaceLog = new InterfaceLog();
                interfaceLog.setTitle("小程序用户点击订单制作商品");
                interfaceLog.setMethodName("makeParam");
                String content = "小程序用户点击商品制作，订单编号为"+orders.getOrderNum()+"，设备编码："+equipmentId+"，商品为："+orderParam.getName()+"（"+orderParam.getSpecifications()+"）";
                interfaceLog.setContent(content);
                interfaceLog.setShopId(orders.getShopId());
                interfaceLog.setTypeStatus(0);
                interfaceLog.setCreateTime(LocalDateTime.now());
                interfaceLog.setUpdateTime(LocalDateTime.now());
                interfaceLogService.save(interfaceLog);

                return R.ok().data("max", max);
            }

        }else {
            return R.error().message("产品配方没填写，请到商户端添加产品配方！");
        }

    }

}

