package com.tea.server.controller;


import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.google.gson.Gson;
import com.tea.server.entity.*;
import com.tea.server.entity.msg.MessageTemplateEntity;
import com.tea.server.entity.msg.MessageValueEntity;
import com.tea.server.entity.query.OrderQuery;
import com.tea.server.entity.vo.*;
import com.tea.server.service.*;
import com.tea.server.socket.CodeData;
import com.tea.server.socket.PlatformWebSocket;

import com.tea.server.utils.HttpRequestUtil;
import com.tea.server.utils.R;
import eleme.openapi.sdk.api.exception.ServiceException;
import eleme.openapi.sdk.api.service.OrderService;
import eleme.openapi.sdk.config.Config;
import eleme.openapi.sdk.oauth.OAuthClient;
import eleme.openapi.sdk.oauth.response.Token;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单记录表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Slf4j
@RestController
@RequestMapping("orders")
public class OrdersController {

    // 设置是否沙箱环境
    private static final boolean isSandbox = true;
    // 设置APPKEY
    private static final String key = "HTsjSlMBbm";
    // 设置APPSECRET
    private static final String secret1 = "bb5041114807043c51a59b208609ac11d97c9136";
    // 初始化OAuthClient
    private static OAuthClient client = null;
    private static Map<String, String> tokenMap = new HashMap<String, String>();
    private static Config config = null;

    static {
        // 初始化全局配置工具
        config = new Config(isSandbox, key, secret1);
        client = new OAuthClient(config);
    }

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderParamService orderParamService;

    @Autowired
    private AdminsService adminsService;

    @Autowired
    private ShopsService shopService;


    @Autowired
    private GoodsToBatchService goodsToBatchService;

    @Autowired
    private BatchingService batchingService;


    // ***************
    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.template-id}")
    private String templateId;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    @Autowired
    private BatchUseService batchUseService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private BatchOrderUseService batchOrderUseService;


    // // 新增订单
    // @PostMapping("insertOrders/{orderNo}/{type}/{shopId}/{adminId}")
    // public R insertOrders(@PathVariable("orderNo") String orderNo, @PathVariable("type") Integer type, @PathVariable("shopId") Long shopId, @PathVariable("adminId") Long adminId, @RequestBody List<OrdersAndParam> params) {
    //     LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(Orders::getStatus, 1).eq(Orders::getPayStatus, 1).eq(Orders::getOrderNum, orderNo);
    //     Orders one = ordersService.getOne(queryWrapper);
    //     if (null == one) {
    //         Orders orders = ordersService.insertOrders(params, orderNo, type, shopId, adminId);
    //         return R.ok().data("orders", orders);
    //     } else {
    //         return R.ok().data("orders", one);
    //     }
    //
    // }
    // 新增订单
    @PostMapping("insertOrders/{orderNo}/{type}/{adminId}")
    public R insertOrders(@PathVariable("orderNo") String orderNo, @PathVariable("type") Integer type, @PathVariable("adminId") Long adminId, @RequestBody List<OrdersAndParam> params) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 1).eq(Orders::getPayStatus, 1).eq(Orders::getOrderNum, orderNo);
        Orders one = ordersService.getOne(queryWrapper);
        if (null == one) {
            Orders orders = ordersService.insertOrders(params, orderNo, type, adminId);
            return R.ok().data("orders", orders);
        } else {
            return R.ok().data("orders", one);
        }

    }

    // 支付成功改变订单状态
    @PostMapping("updateOrders/{id}")
    public R updateOrders(@PathVariable Long id) {

        Orders orders = ordersService.getById(id);
        orders.setUpdateTime(LocalDateTime.now());
        orders.setPayStatus(2);
        boolean b = ordersService.updateById(orders);
        if (b) {
            // try{
            //     Socket socket = new Socket("47.94.56.51", 8282);
            //     System.out.println("Connected to server");
            //
            //     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //
            //     String message = "addOrder:" + orders.getShopId() + "_" + orders.getId();
            //     writer.write(message);
            //     writer.newLine();
            //     writer.flush();
            //     System.out.println("Sent message to server: " + message);
            //
            //     String response = reader.readLine();
            //     System.out.println("Received response from server: " + response);
            //
            //     // 关闭连接
            //     socket.close();
            //     System.out.println("Connection closed");
            //
            // }catch (Exception e){
            //     e.printStackTrace();
            // }
            return R.ok();
        } else {
            return R.error();
        }
    }

    // 点击完成订单按钮改变订单制作状态
    @PostMapping("finishOrders/{id}")
    public R finishOrders(@PathVariable Long id) {

        Orders orders = ordersService.getById(id);
        orders.setUpdateTime(LocalDateTime.now());
        orders.setOrderStatus(2);
        boolean b = ordersService.updateById(orders);
        if (b) {

            return R.ok();
        } else {
            return R.error();
        }
    }


    // 订单商品开始制作
    @PostMapping("makeOrders/{id}")
    public R makeOrders(@PathVariable Long id) {
        Orders orders = ordersService.getById(id);
        orders.setUpdateTime(LocalDateTime.now());
        orders.setOrderStatus(1);
        boolean b = ordersService.updateById(orders);
        if (b) {
            // LambdaQueryWrapper<OrderParam> queryWrapper = new LambdaQueryWrapper<>();
            // queryWrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, orders.getId());
            // List<OrderParam> list = orderParamService.list(queryWrapper);
            // // 新增流水
            // if (null != list && list.size() > 0){
            //     list.stream().forEach(item -> {
            //         OrdersFlow ordersFlow = new OrdersFlow();
            //         ordersFlow.setAdminId(orders.getAdminId());
            //         ordersFlow.setShopId(orders.getShopId());
            //         ordersFlow.setOrderId(orders.getId());
            //         ordersFlow.setPrice(item.getPrice());
            //         ordersFlow.setGoodsId(item.getGoodsId());
            //         ordersFlow.setPayType(orders.getPayType());
            //         ordersFlow.setType(1);
            //         ordersFlow.setNumber(item.getNumber());
            //         ordersFlow.setSerialNum(orders.getSerialNum());
            //         ordersFlow.setGoodsName(item.getName());
            //         ordersFlow.setCreateTime(LocalDateTime.now());
            //         ordersFlow.setUpdateTime(LocalDateTime.now());
            //         ordersFlowService.save(ordersFlow);
            //     });
            // }

            return R.ok();
        } else {
            return R.error();
        }
    }


    // 茶饮机器选择商品直接制作
    @PostMapping("startOrders/{orderNo}/{type}/{adminId}/{goodsId}")
    public R startOrders(@PathVariable("orderNo") String orderNo, @PathVariable("type") Integer type, @PathVariable("adminId") Long adminId, @PathVariable("goodsId") Long goodsId, @RequestBody OrdersAndShopParam params) {
        LambdaQueryWrapper<Shop> shopWrapper = new LambdaQueryWrapper<>();
        shopWrapper.eq(Shop :: getStatus, 1).eq(Shop :: getAdminId, adminId);
        Shop one = shopService.getOne(shopWrapper);

        // 查询是否添加过配方
        LambdaQueryWrapper<GoodsToBatch> batchWrapper = new LambdaQueryWrapper<>();
        batchWrapper.eq(GoodsToBatch :: getStatus, 1).eq(GoodsToBatch :: getShopId, one.getId()).eq(GoodsToBatch :: getGoodsId, params.getGoodsId());

        List<ShopToParam> toParams = params.getSelectParams();
        if (null != toParams && toParams.size() > 0){
            toParams.stream().forEach(aaa ->{
                if (aaa.getType() == 1){
                    batchWrapper.eq(GoodsToBatch :: getSizeId, aaa.getParamId());
                }else if (aaa.getType() == 2){
                    batchWrapper.eq(GoodsToBatch :: getSugarId, aaa.getParamId());
                }else if (aaa.getType() == 3){
                    batchWrapper.eq(GoodsToBatch :: getTemperatureId, aaa.getParamId());
                }
            });
        }

        List<GoodsToBatch> goodsToBatchList = goodsToBatchService.list(batchWrapper);

        if (null != goodsToBatchList && goodsToBatchList.size() > 0){
            LambdaQueryWrapper<Orders> query = new LambdaQueryWrapper<>();
            query.eq(Orders::getStatus, 1).eq(Orders::getPayStatus, 1).eq(Orders::getOrderNum, orderNo);
            Orders orders1 = ordersService.getOne(query);

            if (null == orders1) {
                Orders orders = ordersService.startOrders(params, orderNo, type, adminId);


                List<OutletVo> outletVoList = new ArrayList<>();

                // 获取配料和规格配料
                List<ShopToParam> selectParams = params.getSelectParams();
                StringBuilder sb = new StringBuilder();
                selectParams.forEach(pa -> sb.append(pa.getName() + '，'));
                String str = sb.substring(0, sb.length() - 1);
                List<Long> ids = selectParams.stream().map(rrr -> rrr.getParamId()).collect(Collectors.toList());

                List<Integer> numbers = new ArrayList<>();

                String result = "";
                LambdaQueryWrapper<GoodsToBatch> wrapper2 = new LambdaQueryWrapper<>();

                wrapper2.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, goodsId).eq(GoodsToBatch::getShopId, orders.getShopId()).in(GoodsToBatch::getSizeId, ids).in(GoodsToBatch::getSugarId, ids).in(GoodsToBatch::getTemperatureId, ids);
                List<GoodsToBatch> list = goodsToBatchService.list(wrapper2);

                List<String> paramList = new ArrayList<>();
                List<String> batchList = new ArrayList<>();
                if (null != list && list.size() > 0) {
                    HashMap<Object, Object> arr = new HashMap<Object, Object>();
                    HashMap<Object, Integer> arr1 = new HashMap<Object, Integer>();

                    list.stream().forEach(batch -> {
                        Batching batching = batchingService.getById(batch.getBatchId());
                        // 配料
                        // OutletVo outletVo = new OutletVo();
                        // Integer useNumber = batch.getUseNumber();
                        // outletVo.setUseNumber(useNumber);
                        LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                        wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                        BatchUse batchUse = batchUseService.getOne(wrapper3);
                        if (null != batchUse){
                            if (batchUse.getTotalCount() < batch.getUseNumber()){
                                paramList.add(batching.getName());
                            }else {
                                // BatchUse batchUse1 = new BatchUse();
                                // BeanUtils.copyProperties(batchUse, batchUse1);
                                // batchUse1.setTotalCount(batchUse.getTotalCount() - batch.getUseNumber());
                                // batchUse1.setUseCount(batchUse.getUseCount() + batch.getUseNumber());
                                // // batchUse1.setOrderId(orders.getId());
                                // // batchUse1.setOrderNum(orders.getOrderNum());
                                // batchUseService.updateById(batchUse1);
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
                                //
                                // outletVo.setOutlet(batchUse.getOutlet());
                                //
                                // arr.put(batchUse.getOutlet() + "T", batch.getUseNumber());
                                // arr1.put(batchUse.getOutlet(), 1);
                                // // Integer outletNum = Integer.valueOf(batchUse.getOutlet().replaceAll("[a-zA-Z]", ""));
                                // numbers.add((batch.getUseNumber() / 10) + 1);
                                // outletVoList.add(outletVo);
                            }
                        }else {
                            batchList.add(batching.getName());
                        }


                    });
                    if (batchList.size() > 0){
                        String batchName = batchList.stream() // 将List转换为Stream
                                .distinct() // 去除重复元素
                                .collect(Collectors.joining(","));
                        return R.error().message("原材料"+batchName+"没有,请及时添加");

                    }else {
                        if (paramList.size() > 0){
                            String paramName = paramList.stream() // 将List转换为Stream
                                    .distinct() // 去除重复元素
                                    .collect(Collectors.joining(","));
                            String messageInfo = paramName+"不足";
                            PlatformWebSocket.sendOneMessage(orders.getAdminId(), new CodeData("ok", messageInfo));
                            return R.error().message(messageInfo+"不足,请及时添加");

                        }else {
                            list.stream().forEach(batch -> {
                                // Batching batching = batchingService.getById(batch.getBatchId());
                                // 配料
                                OutletVo outletVo = new OutletVo();
                                Integer useNumber = batch.getUseNumber();
                                outletVo.setUseNumber(useNumber);
                                LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                                wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                                BatchUse batchUse = batchUseService.getOne(wrapper3);

                                BatchUse batchUse1 = new BatchUse();
                                BeanUtils.copyProperties(batchUse, batchUse1);
                                batchUse1.setTotalCount(batchUse.getTotalCount() - batch.getUseNumber());
                                batchUse1.setUseCount(batchUse.getUseCount() + batch.getUseNumber());
                                // batchUse1.setOrderId(orders.getId());
                                // batchUse1.setOrderNum(orders.getOrderNum());
                                batchUseService.updateById(batchUse1);

                                BatchOrderUse batchOrderUse = new BatchOrderUse();
                                batchOrderUse.setOrderId(orders.getId());
                                batchOrderUse.setOrderNum(orders.getOrderNum());
                                batchOrderUse.setBatchId(batchUse1.getBatchId());
                                batchOrderUse.setUseCount(batch.getUseNumber());
                                batchOrderUse.setShopId(batchUse1.getShopId());
                                batchOrderUse.setOutlet(batchUse1.getOutlet());
                                batchOrderUse.setCreateTime(LocalDateTime.now());
                                batchOrderUse.setUpdateTime(LocalDateTime.now());
                                batchOrderUseService.save(batchOrderUse);

                                outletVo.setOutlet(batchUse.getOutlet());

                                arr.put(batchUse.getOutlet() + "T", batch.getUseNumber());
                                arr1.put(batchUse.getOutlet(), 1);
                                // Integer outletNum = Integer.valueOf(batchUse.getOutlet().replaceAll("[a-zA-Z]", ""));
                                numbers.add((batch.getUseNumber() / 10) + 1);
                                outletVoList.add(outletVo);
                            });

                            // 链接机器*************************************
                            ShopToGoods toGoods = shopToGoodsService.getById(goodsId);
                            String equipmentId = toGoods.getEquipmentId();
                            LocalDateTime expireTime = LocalDateTime.now();
                            String mToekn = null;
                            // 获取连接机器的token
                            // 请求body参数
                            Gson gson = new Gson();
                            //调用统一下单API    712bc4e815e24c289a4b31f3e62731fb
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
                                log.info("eeeeeeeeeeeeeee*************:"+httpPost);

                                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                                resp = httpClient.execute(httpPost);
                                int statusCode = resp.getStatusLine().getStatusCode();
                                String bodyAsString = EntityUtils.toString(resp.getEntity());
                                    log.info("33333333333***************************************："+resp);
                                    log.info("statusCode***************************************："+statusCode);
                                if (statusCode == 201) { //处理成功
                                    log.info("成功，返回结果 = " + bodyAsString);
                                    //相应结果
                                    HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
                                    //获取机器的token
                                    mToekn = resultMap.get("token");

                                    // 请求机器 1 配料以及时间

                                    HttpPost httpPost2 = new HttpPost("http://enterpriseapi.gizwits.com/v1/products/712bc4e815e24c289a4b31f3e62731fb/devices/" + equipmentId + "/control");

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
                                            InterfaceLog interfaceLog = new InterfaceLog();
                                            interfaceLog.setTitle("竖屏上下单的商品制作成功");
                                            interfaceLog.setMethodName("startOrders");
                                            String content = "竖屏上下单的商品制作成功,"+"订单金额为："+orders.getTotalPrice()+",操作人："+one.getUsername()+"，设备编码:"+equipmentId+"，订单编号为"+orders.getOrderNum()+"商品为"+toGoods.getName()+"（"+str+"）";
                                            interfaceLog.setContent(content);
                                            interfaceLog.setShopId(orders.getShopId());
                                            interfaceLog.setTypeStatus(0);
                                            interfaceLog.setCreateTime(LocalDateTime.now());
                                            interfaceLog.setUpdateTime(LocalDateTime.now());
                                            interfaceLogService.save(interfaceLog);

                                        }

                                    }

                                }
                                resp.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }





                }


                int max = 0;
                if (numbers.size() > 0) {
                    max = Collections.max(numbers);
                }





                return R.ok().data("orders", orders).data("outletVoList", outletVoList).data("result", result).data("max", max);

            } else {
                List<OutletVo> outletVoList = new ArrayList<>();

                // // 规格参数
                List<ShopToParam> selectParams = params.getSelectParams();
                List<Long> ids = selectParams.stream().map(rrr -> rrr.getParamId()).collect(Collectors.toList());


                LambdaQueryWrapper<GoodsToBatch> wrapper2 = new LambdaQueryWrapper<>();
                // wrapper2.eq(GoodsToBatch :: getStatus, 1).eq(GoodsToBatch :: getShopId, orders1.getShopId()).eq(GoodsToBatch :: getSizeId, ).eq(GoodsToBatch :: getSugarId, sugarId).eq(GoodsToBatch :: getTemperatureId, temperatureId);
                wrapper2.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, goodsId).eq(GoodsToBatch::getShopId, orders1.getShopId()).in(GoodsToBatch::getSizeId, ids).in(GoodsToBatch::getSugarId, ids).in(GoodsToBatch::getTemperatureId, ids);
                List<GoodsToBatch> list = goodsToBatchService.list(wrapper2);
                if (null != list && list.size() > 0) {
                    list.stream().forEach(batch -> {
                        // 配料
                        OutletVo outletVo = new OutletVo();
                        Integer useNumber = batch.getUseNumber();
                        outletVo.setUseNumber(useNumber);
                        LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                        wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders1.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                        BatchUse batchUse = batchUseService.getOne(wrapper3);
                        outletVo.setOutlet(batchUse.getOutlet());
                        outletVoList.add(outletVo);
                        // outletVos.add(outletVo);
                    });
                    String collect = list.stream().map(batch -> {
                        // 配料
                        OutletVo outletVo = new OutletVo();
                        Integer useNumber = batch.getUseNumber();
                        outletVo.setUseNumber(useNumber);
                        LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                        wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders1.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                        BatchUse batchUse = batchUseService.getOne(wrapper3);
                        outletVo.setOutlet(batchUse.getOutlet());
                        return batchUse.getOutlet() + "T=" + batch.getUseNumber() + "&" + batchUse.getOutlet() + "=" + "1";
                    }).collect(Collectors.joining("&"));
                    log.info("*************************************");
                    log.info("wwwwwwwwwwwwwwww," + collect);
                    String url = "http://yp.yinghai-tec.com/api/column/sendmessage";
                    HttpRequestUtil.GetURL(url, collect);
                }



                return R.ok().data("orders", orders1).data("outletVoList", outletVoList);

            }
        }else {
            return R.error().message("产品配方没填写，请到商户端添加产品配方！");
        }



    }

    // 茶饮机器查询订单 列表
    @GetMapping("getOrderList/{adminId}")
    public R getOrderList(@PathVariable("adminId") Long adminId) throws ServiceException {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
        Shop one = shopService.getOne(queryWrapper);

        LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
        LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);


        // 全部
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getStatus, 1).eq(Orders::getShopId, one.getId()).gt(Orders::getCreateTime, startTime).lt(Orders::getCreateTime, endTime).orderByDesc(Orders::getCreateTime).eq(Orders :: getPayStatus, 2).eq(Orders :: getOrderType, 1);
        List<Orders> list = ordersService.list(wrapper);

        // 待制作
        LambdaQueryWrapper<Orders> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Orders::getStatus, 1).eq(Orders::getShopId, one.getId()).between(Orders::getOrderStatus, 0, 1).gt(Orders::getCreateTime, startTime).lt(Orders::getCreateTime, endTime).orderByDesc(Orders::getCreateTime).eq(Orders :: getPayStatus, 2).eq(Orders :: getOrderType, 1);
        List<Orders> list2 = ordersService.list(wrapper2);

        // 已制作
        LambdaQueryWrapper<Orders> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(Orders::getStatus, 1).eq(Orders::getShopId, one.getId()).eq(Orders::getOrderStatus, 2).gt(Orders::getCreateTime, startTime).lt(Orders::getCreateTime, endTime).orderByDesc(Orders::getCreateTime).eq(Orders :: getPayStatus, 2).eq(Orders :: getOrderType, 1);
        List<Orders> list3 = ordersService.list(wrapper3);

        List<OrdersCate> ordersCateList = new ArrayList<>();
        OrdersCate ordersCate = new OrdersCate();
        ordersCate.setId(1L);
        ordersCate.setName("全部");
        ordersCate.setAllList(list);
        ordersCateList.add(ordersCate);

        OrdersCate ordersCate2 = new OrdersCate();
        ordersCate2.setId(2L);
        ordersCate2.setName("待制作");
        ordersCate2.setAllList(list2);
        ordersCateList.add(ordersCate2);

        OrdersCate ordersCate3 = new OrdersCate();
        ordersCate3.setId(3L);
        ordersCate3.setName("已制作");
        ordersCate3.setAllList(list3);
        ordersCateList.add(ordersCate3);

        return R.ok().data("orderList", ordersCateList);


    }

    @GetMapping("getUrl")
    public R getUrl(@RequestParam String url) {
        String uri = "http://yp.yinghai-tec.com/api/column/sendmessage";
        HttpRequestUtil.GetURL(uri, url);
        return R.ok();
    }

    // 茶饮机器 订单商品开始制作
    @PostMapping("makeParam/{id}")
    public R makeParam(@PathVariable Long id, @RequestBody OrderParam params) {

        OrderParam orderParam = orderParamService.getById(id);

        Orders orders = ordersService.getById(orderParam.getOrderId());

        // 查询是否添加过配方
        LambdaQueryWrapper<GoodsToBatch> batchWrapper = new LambdaQueryWrapper<>();
        batchWrapper.eq(GoodsToBatch :: getStatus, 1).eq(GoodsToBatch :: getShopId, orders.getShopId()).eq(GoodsToBatch :: getGoodsId, orderParam.getGoodsId()).eq(GoodsToBatch :: getSizeId, orderParam.getSizeId()).eq(GoodsToBatch :: getSugarId, orderParam.getSugarId()).eq(GoodsToBatch :: getTemperatureId, orderParam.getTemperatureId());
        List<GoodsToBatch> goodsToBatchList = goodsToBatchService.list(batchWrapper);
        if (null != goodsToBatchList && goodsToBatchList.size() > 0){
            // 饿了么商家接单
            if (null != orders.getEleId()) {
                Shop shopServiceById = shopService.getById(orders.getShopId());
                String accessToken = shopServiceById.getAccessToken();
                Long expiresIn = shopServiceById.getExpiresIn();
                String refreshToken = shopServiceById.getRefreshToken();
                String tokenType = shopServiceById.getTokenType();
                Token token = new Token();
                token.setAccessToken(accessToken);
                token.setTokenType(tokenType);
                token.setExpires(expiresIn);
                token.setRefreshToken(refreshToken);
                OrderService eleOrderService = new OrderService(config, token);
                try {
                    eleOrderService.confirmOrderLite(orders.getOrderNum());
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }





            List<Integer> numbers = new ArrayList<>();
            // LambdaQueryWrapper<GoodsToBatch> wrapper2 = new LambdaQueryWrapper<>();
            // wrapper2.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, orderParam.getGoodsId()).eq(GoodsToBatch::getShopId, orders.getShopId()).eq(GoodsToBatch::getSizeId, orderParam.getSizeId()).eq(GoodsToBatch::getSugarId, orderParam.getSugarId()).eq(GoodsToBatch::getTemperatureId, orderParam.getTemperatureId());
            // List<GoodsToBatch> list = goodsToBatchService.list(wrapper2);
            // if (null != list && list.size() > 0) {

            List<String> paramList = new ArrayList<>();
            goodsToBatchList.stream().forEach(batch -> {
                    // 配料
                    // OutletVo outletVo = new OutletVo();
                    // Integer useNumber = batch.getUseNumber();
                    // outletVo.setUseNumber(useNumber);
                    LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                    wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                    BatchUse batchUse = batchUseService.getOne(wrapper3);

                    // outletVo.setOutlet(batchUse.getOutlet());

                if (null != batchUse && batchUse.getTotalCount() < batch.getUseNumber()){
                    Batching batching = batchingService.getById(batchUse.getBatchId());
                    paramList.add(batching.getName());
                }
                });
            if (paramList.size() > 0){
                String paramName = paramList.stream() // 将List转换为Stream
                        .distinct() // 去除重复元素
                        .collect(Collectors.joining(","));
                String messageInfo = paramName+"不足";
                PlatformWebSocket.sendOneMessage(orders.getAdminId(), new CodeData("ok", messageInfo));
                return R.error().message(messageInfo+",请及时添加");

            }else {
                goodsToBatchList.stream().forEach(batch -> {
                    // 配料
                    OutletVo outletVo = new OutletVo();
                    Integer useNumber = batch.getUseNumber();
                    outletVo.setUseNumber(useNumber);
                    LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                    wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                    BatchUse batchUse = batchUseService.getOne(wrapper3);

                    outletVo.setOutlet(batchUse.getOutlet());

                    BatchUse batchUse1 = new BatchUse();
                    BeanUtils.copyProperties(batchUse, batchUse1);
                    batchUse1.setTotalCount(batchUse.getTotalCount() - batch.getUseNumber());
                    batchUse1.setUseCount(batchUse.getUseCount() + batch.getUseNumber());
                    batchUseService.updateById(batchUse1);
                    numbers.add((batch.getUseNumber() / 10) + 1);

                    BatchOrderUse batchOrderUse = new BatchOrderUse();
                    batchOrderUse.setOrderId(orders.getId());
                    batchOrderUse.setOrderNum(orders.getOrderNum());
                    batchOrderUse.setBatchId(batchUse1.getBatchId());
                    batchOrderUse.setUseCount(batch.getUseNumber());
                    batchOrderUse.setShopId(batchUse1.getShopId());
                    batchOrderUse.setOutlet(batchUse1.getOutlet());
                    batchOrderUse.setCreateTime(LocalDateTime.now());
                    batchOrderUse.setUpdateTime(LocalDateTime.now());
                    batchOrderUseService.save(batchOrderUse);
                });


                int max = 0;
                if (numbers.size() > 0) {
                    max = Collections.max(numbers);
                }

                // 链接机器*************************************
                String equipmentId = params.getEquipmentId(); // 712bc4e815e24c289a4b31f3e62731fb
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


                StringEntity entity = new StringEntity(jsonParamsMap, "utf-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");

                //完成签名并执行请求
                CloseableHttpResponse resp = null;
                try {
                    log.info("eeeeeeeeeeeeeee*************:"+httpPost);
                    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                    resp = httpClient.execute(httpPost);
                    int statusCode = resp.getStatusLine().getStatusCode();
                    log.info("33333333333***************************************："+resp);
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

                        HashMap<Object, Object> arr = params.getArr();
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

                            HashMap<Object, Integer> arr1 = params.getArr1();
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

                                Shop shop = shopService.getById(orders.getShopId());
                                InterfaceLog interfaceLog = new InterfaceLog();
                                interfaceLog.setTitle("竖屏订单商品制作成功");
                                interfaceLog.setMethodName("startOrders");
                                String content = "竖屏订单商品制作成功，"+"操作人："+shop.getUsername()+"，设备编码："+equipmentId+"，订单编号为"+orders.getOrderNum()+"商品为"+orderParam.getName()+"（"+orderParam.getSpecifications()+"）";
                                interfaceLog.setContent(content);
                                interfaceLog.setShopId(orders.getShopId());
                                interfaceLog.setTypeStatus(0);
                                interfaceLog.setCreateTime(LocalDateTime.now());
                                interfaceLog.setUpdateTime(LocalDateTime.now());
                                interfaceLogService.save(interfaceLog);

                            }

                        }

                    }
                    resp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return R.ok().data("max", max);
            }

        }else {
            return R.error().message("产品配方没填写，请到商户端添加产品配方！");
        }

    }

    /**
     * 订单 分页
     */
    @PostMapping("getOrders/{page}/{limit}/{shopId}/{type}")
    public R getOrders(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @PathVariable Integer type) {
        // 创建page对象
        Page<Orders> ordersPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Orders::getStatus, 1).eq(Orders::getShopId, shopId).eq(Orders :: getPayStatus, 2);
        if (type != 0) {
            wrapper.eq(Orders::getPayStatus, type);
        }
        wrapper.orderByDesc(Orders::getId);

        ordersService.page(ordersPage, wrapper);

        List<Orders> records = ordersPage.getRecords();
        long total = ordersPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }
    /**
     * 根据订单号后六位订单详情 新 加入美团和饿了么
     *
     * @param orderNo
     * @return
     */
    @GetMapping("getOrdersInfoBy/{orderNo}/{adminId}")
    public R getOrdersInfoBy(@PathVariable String orderNo, @PathVariable Long adminId) {
        OrdersInfo ordersInfo = new OrdersInfo();
        LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
        LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);

        LambdaQueryWrapper<Orders> wrapper6 = new LambdaQueryWrapper<>();
        wrapper6.eq(Orders :: getStatus, 1).eq(Orders :: getAdminId, adminId).like(Orders :: getOrderNum, orderNo).gt(Orders::getCreateTime, startTime).lt(Orders::getCreateTime, endTime).eq(Orders :: getPayStatus, 2).eq(Orders :: getOrderType, 1);
        List<Orders> ordersList = ordersService.list(wrapper6);
        if (null != ordersList && ordersList.size() > 0){
            List<Orders> ordersList1 = ordersList.stream().filter(item -> item.getOrderNum().endsWith(orderNo)).collect(Collectors.toList());
            if (null != ordersList1 && ordersList1.size() > 0) {
                Orders orders = ordersList1.get(0);

                BeanUtils.copyProperties(orders, ordersInfo);
                Admins admins = adminsService.getById(adminId);
                ordersInfo.setAdminName(admins.getUsername());
                LambdaQueryWrapper<Shop> queryWrapper5 = new LambdaQueryWrapper<>();
                queryWrapper5.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
                Shop shop = shopService.getOne(queryWrapper5);
                ordersInfo.setShopName(shop.getName());
                LambdaQueryWrapper<OrderParam> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, orders.getId());
                List<OrderParam> list = orderParamService.list(wrapper);

                List<String> batchNameList = new ArrayList<>();
                if (null != list && list.size() > 0) {
                    list.stream().forEach(param -> {
                        ShopToGoods toGoods = shopToGoodsService.getById(param.getGoodsId());
                        param.setEquipmentId(toGoods.getEquipmentId());
                        LambdaQueryWrapper<GoodsToBatch> wrapper2 = new LambdaQueryWrapper<>();
                        wrapper2.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, param.getGoodsId()).eq(GoodsToBatch::getShopId, shop.getId()).eq(GoodsToBatch::getSizeId, param.getSizeId()).eq(GoodsToBatch::getSugarId, param.getSugarId()).eq(GoodsToBatch::getTemperatureId, param.getTemperatureId());
                        List<GoodsToBatch> list3 = goodsToBatchService.list(wrapper2);
                        // String collect = "";
                        HashMap<Object, Object> arr = new HashMap<>();
                        HashMap<Object, Integer> arr1 = new HashMap<>();
                        if (null != list3 && list3.size() > 0) {
                            list3.stream().forEach(batch -> {
                                // 配料
                                OutletVo outletVo = new OutletVo();
                                Integer useNumber = batch.getUseNumber();
                                outletVo.setUseNumber(useNumber);
                                LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                                wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                                BatchUse batchUse = batchUseService.getOne(wrapper3);
                                if (null != batchUse){
                                    outletVo.setOutlet(batchUse.getOutlet());
                                    arr.put(batchUse.getOutlet() + "T", batch.getUseNumber());
                                    arr1.put(batchUse.getOutlet(), 1);
                                }else {
                                    Batching batching = batchingService.getById(batch.getBatchId());
                                    batchNameList.add(batching.getName());
                                }

                                // return batchUse.getOutlet() + "T=" + batch.getUseNumber() + "&" + batchUse.getOutlet() + "=" + "1";
                            });
                            // .collect(Collectors.joining("&"));

                        }
                        // param.setUrlParam(collect);
                        param.setArr(arr);
                        param.setArr1(arr1);
                    });

                }
                if (batchNameList.size() > 0){
                    String paramName = batchNameList.stream() // 将List转换为Stream
                            .distinct() // 去除重复元素
                            .collect(Collectors.joining(","));
                    String messageInfo = paramName+"不足";
                    PlatformWebSocket.sendOneMessage(orders.getAdminId(), new CodeData("ok", messageInfo));
                    return R.error().message("没有原材料"+messageInfo+"，请到原材料中添加");

                }else {
                    ordersInfo.setParams(list);
                    return R.ok().data("info", ordersInfo);
                }
            }else {
                return R.error().message("没有此订单，请重新输入！");
            }
        }else {
            return R.error().message("没有此订单，请重新输入！");
        }



    }
    /**
     * 订单详情 新 加入美团和饿了么
     *
     * @param id
     * @return
     */
    @GetMapping("getOrdersInfo/{id}/{adminId}")
    public R getOrdersInfo(@PathVariable Long id, @PathVariable Long adminId) {
        Orders orders = ordersService.getById(id);
        orders.setAdminId(adminId);
        ordersService.updateById(orders);
        OrdersInfo ordersInfo = new OrdersInfo();
        BeanUtils.copyProperties(orders, ordersInfo);
        Admins admins = adminsService.getById(adminId);
        ordersInfo.setAdminName(admins.getUsername());
        LambdaQueryWrapper<Shop> queryWrapper5 = new LambdaQueryWrapper<>();
        queryWrapper5.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
        Shop shop = shopService.getOne(queryWrapper5);
        ordersInfo.setShopName(shop.getName());
        LambdaQueryWrapper<OrderParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, id);
        List<OrderParam> list = orderParamService.list(wrapper);

        List<String> batchNameList = new ArrayList<>();
        if (null != list && list.size() > 0) {
            list.stream().forEach(param -> {
                ShopToGoods toGoods = shopToGoodsService.getById(param.getGoodsId());
                param.setEquipmentId(toGoods.getEquipmentId());
                LambdaQueryWrapper<GoodsToBatch> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, param.getGoodsId()).eq(GoodsToBatch::getShopId, shop.getId()).eq(GoodsToBatch::getSizeId, param.getSizeId()).eq(GoodsToBatch::getSugarId, param.getSugarId()).eq(GoodsToBatch::getTemperatureId, param.getTemperatureId());
                List<GoodsToBatch> list3 = goodsToBatchService.list(wrapper2);
                // String collect = "";
                HashMap<Object, Object> arr = new HashMap<>();
                HashMap<Object, Integer> arr1 = new HashMap<>();
                if (null != list3 && list3.size() > 0) {
                    list3.stream().forEach(batch -> {
                        // 配料
                        OutletVo outletVo = new OutletVo();
                        Integer useNumber = batch.getUseNumber();
                        outletVo.setUseNumber(useNumber);
                        LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                        wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                        BatchUse batchUse = batchUseService.getOne(wrapper3);
                        if (null != batchUse){
                            outletVo.setOutlet(batchUse.getOutlet());
                            arr.put(batchUse.getOutlet() + "T", batch.getUseNumber());
                            arr1.put(batchUse.getOutlet(), 1);
                        }else {
                            Batching batching = batchingService.getById(batch.getBatchId());
                            batchNameList.add(batching.getName());
                        }

                        // return batchUse.getOutlet() + "T=" + batch.getUseNumber() + "&" + batchUse.getOutlet() + "=" + "1";
                    });
                    // .collect(Collectors.joining("&"));

                }
                // param.setUrlParam(collect);
                param.setArr(arr);
                param.setArr1(arr1);
            });

        }
        if (batchNameList.size() > 0){
            String paramName = batchNameList.stream() // 将List转换为Stream
                    .distinct() // 去除重复元素
                    .collect(Collectors.joining(","));
            String messageInfo = paramName+"不足";
            PlatformWebSocket.sendOneMessage(orders.getAdminId(), new CodeData("ok", messageInfo));
            return R.error().message("没有原材料"+messageInfo+"，请到原材料中添加");

        }else {
            ordersInfo.setParams(list);
            return R.ok().data("info", ordersInfo);
        }


    }


    // /**
    //  * 订单详情 原
    //  *
    //  * @param id
    //  * @return
    //  */
    // @GetMapping("getOrdersInfo/{id}/{adminId}")
    // public R getOrdersInfo(@PathVariable Long id, @PathVariable Long adminId) {
    //     Orders orders = ordersService.getById(id);
    //     orders.setAdminId(adminId);
    //     ordersService.updateById(orders);
    //     OrdersInfo ordersInfo = new OrdersInfo();
    //     BeanUtils.copyProperties(orders, ordersInfo);
    //     Admins admins = adminsService.getById(adminId);
    //     ordersInfo.setAdminName(admins.getUsername());
    //     Shop shop = shopService.getById(admins.getShopId());
    //     ordersInfo.setShopName(shop.getName());
    //     LambdaQueryWrapper<OrderParam> wrapper = new LambdaQueryWrapper<>();
    //     wrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, id);
    //     List<OrderParam> list = orderParamService.list(wrapper);
    //     list.stream().forEach(item -> {
    //         String specifications = item.getSpecifications();
    //         List<String> stringList = Arrays.asList(specifications.split("，"));
    //         String aaa = stringList.get(0);
    //         String bbb = stringList.get(1);
    //         if (aaa.indexOf("推荐") > -1) {
    //             aaa = aaa.replaceAll("（推荐）", "");
    //         }
    //         if (bbb.indexOf("推荐") > -1) {
    //             bbb = bbb.replaceAll("（推荐）", "");
    //             ;
    //         }
    //
    //         LambdaQueryWrapper<Param> paramQuery = new LambdaQueryWrapper<>();
    //         paramQuery.eq(Param::getStatus, 1).eq(Param::getName, aaa);
    //         Param one = paramService.getOne(paramQuery);
    //
    //         LambdaQueryWrapper<Param> paramQuery1 = new LambdaQueryWrapper<>();
    //         paramQuery1.eq(Param::getStatus, 1).eq(Param::getName, bbb);
    //         Param two = paramService.getOne(paramQuery1);
    //
    //
    //         List<OutletVo> outletVoList = new ArrayList<>();
    //         LambdaQueryWrapper<ParamBatch> paramBatchQuery = new LambdaQueryWrapper<>();
    //
    //
    //         Goods goodsInfo = goodsService.getById(item.getGoodsId());
    //         LambdaQueryWrapper<Categorize> categorizeQuery = new LambdaQueryWrapper<>();
    //         categorizeQuery.eq(Categorize::getStatus, 1).eq(Categorize::getId, goodsInfo.getCateId());
    //         Categorize categorize = categorizeService.getOne(categorizeQuery);
    //         item.setMachineNo(categorize.getMachineNo());
    //
    //         if (aaa.indexOf("糖") > -1) {
    //             paramBatchQuery.eq(ParamBatch::getStatus, 1).eq(ParamBatch::getHeatParamId, two.getId()).eq(ParamBatch::getSugarParamId, one.getId()).eq(ParamBatch::getShopId, orders.getShopId());
    //             ParamBatch paramBatch = paramBatchService.getOne(paramBatchQuery);
    //             if (null != paramBatch) {
    //                 LambdaQueryWrapper<Batching> batchQuery = new LambdaQueryWrapper<>();
    //                 batchQuery.eq(Batching::getStatus, 1).eq(Batching::getMachineNo, categorize.getMachineNo()).like(Batching::getName, "糖").last("limit 1");
    //                 Batching one1 = batchingService.getOne(batchQuery);
    //                 if (null != one1) {
    //                     OutletVo outletVo = new OutletVo();
    //                     outletVo.setOutlet(one1.getOutlet());
    //                     outletVo.setUseNumber(paramBatch.getSugarUseNumber());
    //                     outletVoList.add(outletVo);
    //                 }
    //
    //                 LambdaQueryWrapper<Batching> batchQuery1 = new LambdaQueryWrapper<>();
    //                 if (bbb.indexOf("冰") > -1) {
    //                     batchQuery1.eq(Batching::getStatus, 1).eq(Batching::getMachineNo, categorize.getMachineNo()).like(Batching::getName, "冰").last("limit 1");
    //
    //                 } else if (bbb.indexOf("热") > -1) {
    //                     batchQuery1.eq(Batching::getStatus, 1).eq(Batching::getMachineNo, categorize.getMachineNo()).like(Batching::getName, "热").last("limit 1");
    //                 }
    //                 Batching one2 = batchingService.getOne(batchQuery1);
    //                 if (null != one2) {
    //                     OutletVo outletVo1 = new OutletVo();
    //                     outletVo1.setOutlet(one2.getOutlet());
    //                     outletVo1.setUseNumber(paramBatch.getHeatUseNumber());
    //                     outletVoList.add(outletVo1);
    //                 }
    //
    //
    //             }
    //
    //
    //         } else {
    //             paramBatchQuery.eq(ParamBatch::getStatus, 1).eq(ParamBatch::getHeatParamId, one.getId()).eq(ParamBatch::getSugarParamId, two.getId()).eq(ParamBatch::getShopId, orders.getShopId());
    //             ParamBatch paramBatch = paramBatchService.getOne(paramBatchQuery);
    //             if (paramBatch != null) {
    //                 LambdaQueryWrapper<Batching> batchQuery = new LambdaQueryWrapper<>();
    //                 if (aaa.indexOf("冰") > -1) {
    //                     batchQuery.eq(Batching::getStatus, 1).eq(Batching::getMachineNo, categorize.getMachineNo()).like(Batching::getName, "冰").last("limit 1");
    //
    //                 } else if (aaa.indexOf("热") > -1) {
    //                     batchQuery.eq(Batching::getStatus, 1).eq(Batching::getMachineNo, categorize.getMachineNo()).like(Batching::getName, "热").last("limit 1");
    //                 }
    //                 Batching one1 = batchingService.getOne(batchQuery);
    //                 if (null != one1) {
    //                     OutletVo outletVo = new OutletVo();
    //                     outletVo.setOutlet(one1.getOutlet());
    //                     outletVo.setUseNumber(paramBatch.getHeatUseNumber());
    //                     outletVoList.add(outletVo);
    //                 }
    //
    //                 LambdaQueryWrapper<Batching> batchQuery1 = new LambdaQueryWrapper<>();
    //                 batchQuery1.eq(Batching::getStatus, 1).eq(Batching::getMachineNo, categorize.getMachineNo()).like(Batching::getName, "糖").last("limit 1");
    //                 Batching one2 = batchingService.getOne(batchQuery1);
    //                 if (null != one2) {
    //                     OutletVo outletVo1 = new OutletVo();
    //                     outletVo1.setOutlet(one2.getOutlet());
    //                     outletVo1.setUseNumber(paramBatch.getSugarUseNumber());
    //                     outletVoList.add(outletVo1);
    //                 }
    //             }
    //
    //
    //         }
    //
    //         LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
    //         queryWrapper.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, item.getGoodsId());
    //         List<GoodsToBatch> goodsToBatches = goodsToBatchService.list(queryWrapper);
    //
    //         goodsToBatches.stream().forEach(goods -> {
    //             Batching batching = batchingService.getById(goods.getBatchId());
    //
    //             if (stringList.get(0).indexOf("热") > -1 || stringList.get(1).indexOf("热") > -1) {
    //                 if (!batching.getName().contains("热")) {
    //                     OutletVo outletVo = new OutletVo();
    //                     outletVo.setUseNumber(goods.getUseNumber());
    //                     outletVo.setOutlet(batching.getOutlet());
    //                     outletVoList.add(outletVo);
    //                 }
    //             } else {
    //                 OutletVo outletVo = new OutletVo();
    //                 outletVo.setUseNumber(goods.getUseNumber());
    //                 outletVo.setOutlet(batching.getOutlet());
    //                 outletVoList.add(outletVo);
    //             }
    //
    //         });
    //
    //         item.setOutletList(outletVoList);
    //     });
    //     ordersInfo.setParams(list);
    //     return R.ok().data("info", ordersInfo);
    // }


    private void sendMessage(Long adminId, String shopName, String pickupCode, String goodsName, String time, String openid) {
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("appid", appid); //小程序appid
        requestParams.put("secret", secret); //小程序secret
        requestParams.put("grant_type", "client_credential"); //授权类型这里填写client_credential
        String tokenStr = "";
        String accessToken = "";
        Admins admins = adminsService.getById(adminId);
        LocalDateTime tokenIn = admins.getTokenIn();
        if (null != tokenIn) {
            long seconds1 = tokenIn.toEpochSecond(ZoneOffset.UTC);
            long seconds2 = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            if (seconds2 - seconds1 > 7200) {

                tokenStr = HttpRequestUtil.sendGet("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=" + appid + "&secret=" + secret);
                // tokenStr = HttpRequestUtil.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"secret=" + secret);
                JSONObject token = JSON.parseObject(tokenStr);
                System.out.println("token:" + token);
                accessToken = token.getString("access_token");
                if (null != accessToken) {
                    Admins admins1 = new Admins();
                    BeanUtils.copyProperties(admins, admins1);
                    admins1.setAccessToken(accessToken);
                    admins1.setTokenIn(LocalDateTime.now());
                    adminsService.updateById(admins1);
                }
            } else {
                accessToken = admins.getAccessToken();
            }
        } else {
            tokenStr = HttpRequestUtil.sendGet("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=" + appid + "&secret=" + secret);
            // tokenStr = HttpRequestUtil.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"secret=" + secret);
            JSONObject token = JSON.parseObject(tokenStr);
            accessToken = token.getString("access_token");

            if (null != accessToken) {
                Admins admins1 = new Admins();
                BeanUtils.copyProperties(admins, admins1);
                admins1.setAccessToken(accessToken);
                admins1.setTokenIn(LocalDateTime.now());
                adminsService.updateById(admins1);
            }

        }

        RestTemplate restTemplate = new RestTemplate();

        MessageTemplateEntity messageTemplateEntity = new MessageTemplateEntity();
        messageTemplateEntity.setMessageData(new MessageValueEntity(shopName), new MessageValueEntity(pickupCode), new MessageValueEntity(goodsName), new MessageValueEntity(time), new MessageValueEntity("您的商品已制作完成，请尽快去取呦~"));

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("touser", openid); //用户openid
        paramsMap.put("template_id", templateId); //推送消息模板id
        paramsMap.put("data", messageTemplateEntity); //消息体：{{"thing1":"项目名称"},{"time2":"2022-08-23"},{"thing3":"这是描述"}}
        HttpHeaders headers = new HttpHeaders(); //构建请求头
        headers.setContentType(MediaType.APPLICATION_JSON); //设置内容类型为json
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(paramsMap, headers); //构建http请求实体

        //发送请求路径拼接获取到的access_token
        // String sendMessageVo = HttpRequestUtil.doGet("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+ accessToken);
        // String sendMessageVo = HttpRequestUtil.doPOST("https://api.weixin.qq.com/cgi-bin/message/subscribe/send", "access_token=" + accessToken + request + SendMessageVo.class);
        SendMessageVo sendMessageVo = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken, request, SendMessageVo.class);
        // JSONObject sendMessage = JSON.parseObject(sendMessageVo);
        // return R.ok().data("sendMessageVo", sendMessageVo);
        // if (null == sendMessageVo) {
        //     throw new RuntimeException("推送消息失败");
        // }
        //
        // if (sendMessageVo.getErrcode() != 0) {
        //     log.error("推送消息失败,原因：{}", sendMessageVo.getErrmsg());
        //     throw new RuntimeException("推送消息失败");
        // }

    }


    /**
     * 叫号取餐列表 分页
     */
    @PostMapping("callOrders/{page}/{limit}/{shopId}")
    public R callOrders(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId) {
        // 创建page对象
        // Page<OrdersCall> ordersPage = new Page<>(page, limit);
        // Page<OrdersCall> pageList = ordersService.callOrders(ordersPage, shopId);
        //
        // long total = pageList.getTotal();
        // List<OrdersCall> records = pageList.getRecords();
        // 创建page对象
        Page<Orders> ordersPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Orders::getStatus, 1).eq(Orders::getShopId, shopId).eq(Orders::getIsTake, 1).ne(Orders::getOrderStatus, 0).orderByDesc(Orders::getId).eq(Orders :: getPayStatus, 2);
        ordersService.page(ordersPage, wrapper);

        List<Orders> records = ordersPage.getRecords();
        List<OrdersCall> list = new ArrayList<>();
        records.stream().forEach(item -> {
            OrdersCall ordersCall = new OrdersCall();
            BeanUtils.copyProperties(item, ordersCall);
            LambdaQueryWrapper<OrderParam> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, item.getId());
            List<OrderParam> list1 = orderParamService.list(queryWrapper);
            ordersCall.setOrderParams(list1);
            list.add(ordersCall);
        });
        long total = ordersPage.getTotal();
        return R.ok().data("total", total).data("rows", list);
    }

    /**
     * 呼叫取餐改变呼叫状态
     *
     * @param id
     * @return
     */
    @PostMapping("callOrdersBy/{id}")
    public R callOrdersBy(@PathVariable Long id) {
        Orders orders = ordersService.getById(id);
        orders.setIsCall(2);
        orders.setOrderStatus(2);
        orders.setUpdateTime(LocalDateTime.now());
        boolean b = ordersService.updateById(orders);
        if (b) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //当前 LocalDateTime 类型 时间
            LocalDateTime ldt = LocalDateTime.now();
            //转换为 String 类型
            String time24 = ldt.format(dtf);
            LambdaQueryWrapper<OrderParam> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, id);
            List<OrderParam> list = orderParamService.list(queryWrapper);
            StringBuilder sb = new StringBuilder();
            list.stream().forEach(item -> {
                sb.append(sb + item.getName() + "(" + item.getSpecifications() + ")" + "、");
            });
            String goodsName = sb.substring(0, sb.length() - 1);
            LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Shop::getStatus, 1).eq(Shop::getId, orders.getShopId());
            Shop one = shopService.getOne(wrapper);
            sendMessage(orders.getAdminId(), one.getName(), String.valueOf(orders.getPickupCode()), goodsName, time24, orders.getOpenid());
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 根据取餐码呼叫取餐改变呼叫状态
     *
     * @return
     */
    @PostMapping("callOrdersByCode")
    public R callOrdersByCode(@RequestParam Integer pickupCode) {
        LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
        LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 1).eq(Orders::getPickupCode, pickupCode).gt(Orders::getCreateTime, startTime).lt(Orders::getCreateTime, endTime);
        Orders one = ordersService.getOne(queryWrapper);
        if (null != one) {
            one.setIsCall(2);
            one.setOrderStatus(2);
            one.setUpdateTime(LocalDateTime.now());
            boolean b = ordersService.updateById(one);
            if (b) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                //当前 LocalDateTime 类型 时间
                LocalDateTime ldt = LocalDateTime.now();
                //转换为 String 类型
                String time24 = ldt.format(dtf);
                LambdaQueryWrapper<OrderParam> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, one.getId());
                List<OrderParam> list = orderParamService.list(wrapper);
                StringBuilder sb = new StringBuilder();
                list.stream().forEach(item -> {
                    sb.append(sb + item.getName() + "(" + item.getSpecifications() + ")" + "、");
                });
                String goodsName = sb.substring(0, sb.length() - 1);
                LambdaQueryWrapper<Shop> shopWrapper = new LambdaQueryWrapper<>();
                shopWrapper.eq(Shop::getStatus, 1).eq(Shop::getId, one.getShopId());
                Shop shop = shopService.getOne(shopWrapper);
                sendMessage(one.getAdminId(), shop.getName(), String.valueOf(one.getPickupCode()), goodsName, time24, one.getOpenid());
                return R.ok();
            } else {
                return R.error();
            }
        } else {
            return R.ok().message("暂无此号！");
        }

    }

    /**
     * 点击已取餐改变取餐状态
     *
     * @param id
     * @return
     */
    @PostMapping("orderTake/{id}")
    public R orderTake(@PathVariable Long id) {
        Orders orders = ordersService.getById(id);
        orders.setIsTake(2);
        boolean b = ordersService.updateById(orders);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("getOrdersInfoBy/{id}")
    public R getOrdersInfoBy(@PathVariable Long id) {
        Orders orders = ordersService.getById(id);
        return R.ok().data("payStatus", orders.getPayStatus());
    }

    // ****************

    /**
     * 商户端订单 分页
     */
    @PostMapping("getShopOrders/{page}/{limit}/{shopId}")
    public R getShopOrders(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId, @RequestBody OrderQuery orderQuery) {
        // 创建page对象
        Page<Orders> ordersPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Orders::getStatus, 1).eq(Orders::getShopId, shopId).eq(Orders :: getPayStatus, 2);
        String orderNum = orderQuery.getOrderNum();
        Integer platformType = orderQuery.getPlatformType();
        String begin = orderQuery.getBegin();
        String end = orderQuery.getEnd();
        if (null != orderNum && !orderNum.equals("")) {
            wrapper.like(Orders::getOrderNum, orderNum);
        }
        if (null != platformType && platformType > 0) {
            wrapper.eq(Orders::getPlatformType, platformType);
        }
        if (null != begin && !begin.equals("")) {
            wrapper.ge(Orders :: getCreateTime, begin);
        }
        if (null != end && !end.equals("")) {
            wrapper.le(Orders :: getCreateTime, end);
        }
        wrapper.orderByDesc(Orders::getId);

        ordersService.page(ordersPage, wrapper);

        List<Orders> records = ordersPage.getRecords();
        long total = ordersPage.getTotal();

        LambdaQueryWrapper<Orders> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Orders :: getPayStatus,2).eq(Orders :: getStatus, 1);
        if (null != orderNum && !orderNum.equals("")){
            wrapper2.like(Orders :: getOrderNum, orderNum);
        }
        if (null != begin && !begin.equals("")){
            wrapper2.ge(Orders :: getCreateTime, begin);
        }
        if (null != end && !end.equals("")){
            wrapper2.le(Orders :: getCreateTime, end);
        }
        if (null != platformType && platformType > 0){
            wrapper2.eq(Orders :: getPlatformType, platformType);
        }
        List<Orders> list = ordersService.list(wrapper2);
        BigDecimal price = new BigDecimal(0);
        int sum = 0;
        if (null != list && list.size() > 0){
            price = list.stream().map(item -> item.getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
            sum = list.stream().map(item -> item.getTotalCount()).mapToInt(Integer::intValue).sum();
        }
        return R.ok().data("total", total).data("rows", records).data("price", price).data("sum", sum);
    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("getOrdersInfoShop/{id}")
    public R getOrdersInfoShop(@PathVariable Long id) {
        Orders orders = ordersService.getById(id);
        Long adminId = orders.getAdminId();
        // *********************************
        OrdersInfo ordersInfo = new OrdersInfo();
        BeanUtils.copyProperties(orders, ordersInfo);
        Admins admins = adminsService.getById(adminId);
        ordersInfo.setAdminName(admins.getUsername());
        LambdaQueryWrapper<Shop> queryWrapper5 = new LambdaQueryWrapper<>();
        queryWrapper5.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
        Shop shop = shopService.getOne(queryWrapper5);
        ordersInfo.setShopName(shop.getName());
        LambdaQueryWrapper<OrderParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, id);
        List<OrderParam> list = orderParamService.list(wrapper);

        if (null != list && list.size() > 0) {
            list.stream().forEach(param -> {
                ShopToGoods toGoods = shopToGoodsService.getById(param.getGoodsId());
                param.setEquipmentId(toGoods.getEquipmentId());
                LambdaQueryWrapper<GoodsToBatch> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, param.getGoodsId()).eq(GoodsToBatch::getShopId, shop.getId()).eq(GoodsToBatch::getSizeId, param.getSizeId()).eq(GoodsToBatch::getSugarId, param.getSugarId()).eq(GoodsToBatch::getTemperatureId, param.getTemperatureId());
                List<GoodsToBatch> list3 = goodsToBatchService.list(wrapper2);

                if (null != list3 && list3.size() > 0) {
                    list3.stream().forEach(batch -> {
                        // 配料
                        OutletVo outletVo = new OutletVo();
                        Integer useNumber = batch.getUseNumber();
                        outletVo.setUseNumber(useNumber);
                        LambdaQueryWrapper<BatchUse> wrapper3 = new LambdaQueryWrapper<>();
                        wrapper3.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batch.getBatchId());
                        BatchUse batchUse = batchUseService.getOne(wrapper3);
                        outletVo.setOutlet(batchUse.getOutlet());
                    });


                }

            });

        }


        ordersInfo.setParams(list);
        return R.ok().data("info", ordersInfo);

        // ***********************************

        // ordersService.updateById(orders);
        // OrdersInfo ordersInfo = new OrdersInfo();
        // BeanUtils.copyProperties(orders, ordersInfo);
        // Admins admins = adminsService.getById(adminId);
        // ordersInfo.setAdminName(admins.getUsername());
        // Shop shop = shopService.getById(orders.getShopId());
        // // Shop shop = shopService.getOne(queryWrapper5);
        // ordersInfo.setShopName(shop.getName());
        // LambdaQueryWrapper<OrderParam> wrapper = new LambdaQueryWrapper<>();
        // wrapper.eq(OrderParam :: getStatus, 1).eq(OrderParam :: getOrderId, id);
        // List<OrderParam> list = orderParamService.list(wrapper);
        // list.stream().forEach(item -> {
        //     String specifications = item.getSpecifications();
        //     List<String> stringList = Arrays.asList(specifications.split("，"));
        //     String aaa = stringList.get(0);
        //     String bbb = stringList.get(1);
        //     if (aaa.indexOf("推荐") > -1){
        //         aaa = aaa.replaceAll("（推荐）", "");
        //     }
        //     if (bbb.indexOf("推荐") > -1){
        //         bbb = bbb.replaceAll("（推荐）", "");;
        //     }
        //
        //     LambdaQueryWrapper<Param> paramQuery = new LambdaQueryWrapper<>();
        //     paramQuery.eq(Param :: getStatus, 1).eq(Param :: getName, aaa);
        //     Param one = paramService.getOne(paramQuery);
        //
        //     LambdaQueryWrapper<Param> paramQuery1 = new LambdaQueryWrapper<>();
        //     paramQuery1.eq(Param :: getStatus, 1).eq(Param :: getName, bbb);
        //     Param two = paramService.getOne(paramQuery1);
        //
        //
        //     List<OutletVo> outletVoList = new ArrayList<>();
        //     LambdaQueryWrapper<ParamBatch> paramBatchQuery = new LambdaQueryWrapper<>();
        //
        //
        //     Goods goodsInfo = goodsService.getById(item.getGoodsId());
        //     LambdaQueryWrapper<Categorize> categorizeQuery = new LambdaQueryWrapper<>();
        //     categorizeQuery.eq(Categorize :: getStatus, 1).eq(Categorize :: getId, goodsInfo.getCateId());
        //     Categorize categorize = categorizeService.getOne(categorizeQuery);
        //     item.setMachineNo(categorize.getMachineNo());
        //
        //     if (aaa.indexOf("糖") > -1){
        //         paramBatchQuery.eq(ParamBatch :: getStatus, 1).eq(ParamBatch :: getHeatParamId, two.getId()).eq(ParamBatch :: getSugarParamId, one.getId()).eq(ParamBatch :: getShopId, orders.getShopId());
        //         ParamBatch paramBatch = paramBatchService.getOne(paramBatchQuery);
        //         if (null != paramBatch){
        //             LambdaQueryWrapper<Batching> batchQuery = new LambdaQueryWrapper<>();
        //             batchQuery.eq(Batching :: getStatus, 1).eq(Batching :: getMachineNo, categorize.getMachineNo()).like(Batching :: getName, "糖").last("limit 1");
        //             Batching one1 = batchingService.getOne(batchQuery);
        //             if (null != one1){
        //                 OutletVo outletVo = new OutletVo();
        //                 outletVo.setOutlet(one1.getOutlet());
        //                 outletVo.setUseNumber(paramBatch.getSugarUseNumber());
        //                 outletVoList.add(outletVo);
        //             }
        //
        //             LambdaQueryWrapper<Batching> batchQuery1 = new LambdaQueryWrapper<>();
        //             if (bbb.indexOf("冰") > -1){
        //                 batchQuery1.eq(Batching :: getStatus, 1).eq(Batching :: getMachineNo, categorize.getMachineNo()).like(Batching :: getName, "冰").last("limit 1");
        //
        //             }else if(bbb.indexOf("热") > -1){
        //                 batchQuery1.eq(Batching :: getStatus, 1).eq(Batching :: getMachineNo, categorize.getMachineNo()).like(Batching :: getName, "热").last("limit 1");
        //             }
        //             Batching one2 = batchingService.getOne(batchQuery1);
        //             if (null != one2) {
        //                 OutletVo outletVo1 = new OutletVo();
        //                 outletVo1.setOutlet(one2.getOutlet());
        //                 outletVo1.setUseNumber(paramBatch.getHeatUseNumber());
        //                 outletVoList.add(outletVo1);
        //             }
        //
        //
        //         }
        //
        //
        //     }else {
        //         paramBatchQuery.eq(ParamBatch :: getStatus, 1).eq(ParamBatch :: getHeatParamId, one.getId()).eq(ParamBatch :: getSugarParamId, two.getId()).eq(ParamBatch :: getShopId, orders.getShopId());
        //         ParamBatch paramBatch = paramBatchService.getOne(paramBatchQuery);
        //         if (paramBatch != null) {
        //             LambdaQueryWrapper<Batching> batchQuery = new LambdaQueryWrapper<>();
        //             if (aaa.indexOf("冰") > -1){
        //                 batchQuery.eq(Batching :: getStatus, 1).eq(Batching :: getMachineNo, categorize.getMachineNo()).like(Batching :: getName, "冰").last("limit 1");
        //
        //             }else if(aaa.indexOf("热") > -1){
        //                 batchQuery.eq(Batching :: getStatus, 1).eq(Batching :: getMachineNo, categorize.getMachineNo()).like(Batching :: getName, "热").last("limit 1");
        //             }
        //             Batching one1 = batchingService.getOne(batchQuery);
        //             if (null != one1) {
        //                 OutletVo outletVo = new OutletVo();
        //                 outletVo.setOutlet(one1.getOutlet());
        //                 outletVo.setUseNumber(paramBatch.getHeatUseNumber());
        //                 outletVoList.add(outletVo);
        //             }
        //
        //             LambdaQueryWrapper<Batching> batchQuery1 = new LambdaQueryWrapper<>();
        //             batchQuery1.eq(Batching :: getStatus, 1).eq(Batching :: getMachineNo, categorize.getMachineNo()).like(Batching :: getName, "糖").last("limit 1");
        //             Batching one2 = batchingService.getOne(batchQuery1);
        //             if (null != one2) {
        //                 OutletVo outletVo1 = new OutletVo();
        //                 outletVo1.setOutlet(one2.getOutlet());
        //                 outletVo1.setUseNumber(paramBatch.getSugarUseNumber());
        //                 outletVoList.add(outletVo1);
        //             }
        //         }
        //
        //
        //     }
        //
        //     LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
        //     queryWrapper.eq(GoodsToBatch :: getStatus, 1).eq(GoodsToBatch :: getGoodsId, item.getGoodsId());
        //     List<GoodsToBatch> goodsToBatches = goodsToBatchService.list(queryWrapper);
        //
        //     goodsToBatches.stream().forEach(goods -> {
        //         Batching batching = batchingService.getById(goods.getBatchId());
        //
        //         if(stringList.get(0).indexOf("热") > -1 || stringList.get(1).indexOf("热") > -1){
        //             if (!batching.getName().contains("热")){
        //                 OutletVo outletVo = new OutletVo();
        //                 outletVo.setUseNumber(goods.getUseNumber());
        //                 outletVo.setOutlet(batching.getOutlet());
        //                 outletVoList.add(outletVo);
        //             }
        //         }else {
        //             OutletVo outletVo = new OutletVo();
        //             outletVo.setUseNumber(goods.getUseNumber());
        //             outletVo.setOutlet(batching.getOutlet());
        //             outletVoList.add(outletVo);
        //         }
        //
        //     });
        //
        //     item.setOutletList(outletVoList);
        // });
        // ordersInfo.setParams(list);
        // return R.ok().data("info", ordersInfo);
    }


    /**
     * 总后台订单 分页
     */
    @PostMapping("getAllOrders/{page}/{limit}")
    public R getAllOrders(@PathVariable long page, @PathVariable long limit, @RequestBody OrderQuery orderQuery) {
        // 创建page对象
        Page<Orders> ordersPage = new Page<>(page, limit);

        // 构建条件
        Page<Orders> pageList = ordersService.pageAllOrders(ordersPage, orderQuery);

        long total = pageList.getTotal();
        List<Orders> records = pageList.getRecords();


        String orderNum = orderQuery.getOrderNum();
        String begin = orderQuery.getBegin();
        String end = orderQuery.getEnd();
        Integer platformType = orderQuery.getPlatformType();
        Long shopId = orderQuery.getShopId();
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders :: getPayStatus,2).eq(Orders :: getStatus, 1);
        if (null != orderNum && !orderNum.equals("")){
            wrapper.like(Orders :: getOrderNum, orderNum);
        }
        if (null != begin && !begin.equals("")){
            wrapper.ge(Orders :: getCreateTime, begin);
        }
        if (null != end && !end.equals("")){
            wrapper.le(Orders :: getCreateTime, end);
        }
        if (null != platformType && platformType > 0){
            wrapper.eq(Orders :: getPlatformType, platformType);
        }
        if (null != shopId && shopId > 0){
            wrapper.eq(Orders :: getShopId, shopId);
        }
        List<Orders> list = ordersService.list(wrapper);
        BigDecimal price = new BigDecimal(0);
        int sum = 0;
        if (null != list && list.size() > 0){
            price = list.stream().map(item -> item.getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
            sum = list.stream().map(item -> item.getTotalCount()).mapToInt(Integer::intValue).sum();
        }


        return R.ok().data("total", total).data("rows", records).data("price", price).data("sum", sum);
    }
}

