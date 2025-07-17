package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.tea.server.entity.*;
import com.tea.server.entity.query.AccountQuery;
import com.tea.server.entity.query.BatchUseQuery;
import com.tea.server.entity.vo.BatchingVo;
import com.tea.server.service.*;
import com.tea.server.socket.CodeData;
import com.tea.server.socket.DeviceWebSocket;
import com.tea.server.socket.PlatformWebSocket;
import com.tea.server.utils.R;
import eleme.openapi.sdk.api.service.ShopService;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 店家商品与配方消耗对应关系表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Slf4j
@RestController
@RequestMapping("batchUse")
public class BatchUseController {

    @Autowired
    private BatchUseService batchUseService;

    @Autowired
    private BatchingService batchingService;

    @Autowired
    private AdminsService adminsService;

    @Autowired
    private ShopsService shopService;

    @Autowired
    private GoodsAllBatchService goodsAllBatchService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 根据店铺id获取当前库配方存 分页
     */
    @PostMapping("getInventories/{page}/{limit}/{shopId}")
    public R getInventories(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId) {
        // 创建page对象
        Page<BatchingVo> batchingVoPage = new Page<>(page, limit);

        // 构建条件
        Page<BatchingVo> pageList = batchUseService.pageInventories(batchingVoPage, shopId);

        long total = pageList.getTotal();
        List<BatchingVo> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }


    // 查询所有当前店铺配料
    @GetMapping("getBatchList1/{shopId}")
    public R getBatchList1(@PathVariable Long shopId) {
        LambdaQueryWrapper<BatchUse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchUse::getShopId, shopId).eq(BatchUse::getStatus, 1);
        List<BatchUse> list = batchUseService.list(queryWrapper);
        List<Long> batchIds = list.stream().map(item -> item.getBatchId()).collect(Collectors.toList());

        LambdaQueryWrapper<Batching> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Batching::getStatus, 1).in(Batching::getId, batchIds);
        List<Batching> batchings = batchingService.list(wrapper);

        return R.ok().data("batchList", batchings);
    }


    /**
     * 茶饮竖屏机器 获取配方列表
     */
    @GetMapping("getShopBatchList/{adminId}")
    public R getShopBatchList(@PathVariable Long adminId, @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
        Shop one = shopService.getOne(queryWrapper);

        List<BatchUse> list = batchUseService.getListByName(one.getId(), keyword);
        List<Batching> allBatchs = batchingService.list(null);

        if (null != list && list.size() > 0){
            List<Long> batchIds = list.stream().filter(item -> item.getTotalCount() < 0).map(ee -> ee.getBatchId()).collect(Collectors.toList());
            if (null != batchIds && batchIds.size() > 0){
                LambdaQueryWrapper<Batching> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Batching :: getStatus, 1).in(Batching :: getId, batchIds);
                List<Batching> batchingList = batchingService.list(wrapper);
                if (null != batchingList && batchingList.size() > 0){
                    List<String> strings = batchingList.stream().map(aa -> aa.getName()).collect(Collectors.toList());
                    String paramName = strings.stream() // 将List转换为Stream
                            .distinct() // 去除重复元素
                            .collect(Collectors.joining(","));
                    PlatformWebSocket.sendOneMessage(adminId, new CodeData("ok", paramName+"不足"));
                }
            }


        }

        return R.ok().data("list", list).data("allBatchs", allBatchs);
    }


    // 配料列表
    @PostMapping("getBatchList/{page}/{limit}/{shopId}")
    public R getBatchList(@PathVariable long page, @PathVariable long limit, @PathVariable Long shopId) {
        // 创建page对象
        Page<BatchUse> batchPage = new Page<>(page, limit);

        // 构建条件
        Page<BatchUse> pageList = batchUseService.pageList(batchPage, shopId);

        long total = pageList.getTotal();
        List<BatchUse> records = pageList.getRecords();


        Shop shop = shopService.getById(shopId);
        List<Long> goodsIds = Arrays.stream(shop.getGoodRange().split(",")).map(Long::valueOf).collect(Collectors.toList());
        LambdaQueryWrapper<GoodsAllBatch> allBatchWrapper = new LambdaQueryWrapper<>();
        allBatchWrapper.in(GoodsAllBatch :: getGoodsId, goodsIds).eq(GoodsAllBatch :: getStatus, 1);
        List<GoodsAllBatch> goodsAllBatches = goodsAllBatchService.list(allBatchWrapper);
        List<Long> batchList = goodsAllBatches.stream().map(iii -> iii.getBatchId()) // 将List转换为Stream
                .distinct() // 去除重复元素
                .collect(Collectors.toList());

        LambdaQueryWrapper<Batching> batchWrapper = new LambdaQueryWrapper<>();
        batchWrapper.eq(Batching :: getStatus, 1).in(Batching :: getId, batchList);

        List<Batching> allBatchs = batchingService.list(batchWrapper);
        return R.ok().data("total", total).data("rows", records).data("allBatchs", allBatchs);
    }

    @GetMapping("getBatchInfo/{id}")
    public R getBatchInfo(@PathVariable Long id) {
        BatchUse info = batchUseService.getById(id);
        return R.ok().data("info", info);
    }

    /**
     * 添加
     *
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody BatchUse obj) {
        BatchUse batchUse = new BatchUse();
        LambdaQueryWrapper<BatchUse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BatchUse::getStatus, 1).eq(BatchUse::getBatchId, obj.getBatchId()).eq(BatchUse::getShopId, obj.getShopId());
        List<BatchUse> list = batchUseService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该配料已添加");
        } else {
            BeanUtils.copyProperties(obj, batchUse);
            batchUse.setCreateTime(LocalDateTime.now());
            batchUse.setTotalCount(obj.getTotalCount());
            batchUse.setUpdateTime(LocalDateTime.now());
            boolean save = batchUseService.save(batchUse);
            if (save) {
                Batching batching = batchingService.getById(obj.getBatchId());
                Shop shop = shopService.getById(obj.getShopId());
                InterfaceLog interfaceLog = new InterfaceLog();
                interfaceLog.setTitle("商户添加配料");
                interfaceLog.setMethodName("");
                String content = "商户"+shop.getName()+"添加配料，配料名为"+batching.getName();
                interfaceLog.setContent(content);
                interfaceLog.setShopId(obj.getShopId());
                interfaceLog.setTypeStatus(0);
                interfaceLog.setCreateTime(LocalDateTime.now());
                interfaceLog.setUpdateTime(LocalDateTime.now());
                interfaceLogService.save(interfaceLog);
                return R.ok();
            } else {
                return R.error();
            }
        }

    }

    /**
     * 修改
     *
     * @return
     */
    @PostMapping("updateData/{id}")
    public R updateData(@PathVariable long id, @RequestBody BatchUse obj) {
        BatchUse batchUse = batchUseService.getById(id);
        LambdaQueryWrapper<BatchUse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BatchUse::getStatus, 1).eq(BatchUse::getBatchId, obj.getBatchId()).ne(BatchUse::getId, batchUse.getId()).eq(BatchUse::getShopId, obj.getShopId());
        List<BatchUse> list = batchUseService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该配料已添加");
        } else {
            batchUse.setBatchId(obj.getBatchId());
            batchUse.setTotalCount(obj.getTotalCount());
            batchUse.setOutlet(obj.getOutlet());
            batchUse.setUpdateTime(LocalDateTime.now());
            boolean save = batchUseService.updateById(batchUse);
            if (save) {
                Batching batching = batchingService.getById(obj.getBatchId());
                Shop shop = shopService.getById(obj.getShopId());
                InterfaceLog interfaceLog = new InterfaceLog();
                interfaceLog.setTitle("商户修改配料");
                interfaceLog.setMethodName("");
                String content = "商户"+shop.getName()+"修改配料，配料名为"+batching.getName();
                interfaceLog.setContent(content);
                interfaceLog.setShopId(obj.getShopId());
                interfaceLog.setTypeStatus(0);
                interfaceLog.setCreateTime(LocalDateTime.now());
                interfaceLog.setUpdateTime(LocalDateTime.now());
                interfaceLogService.save(interfaceLog);
                return R.ok();
            } else {
                return R.error();
            }
        }

    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        BatchUse batchUse = batchUseService.getById(id);
        boolean b = batchUseService.removeById(id);
        if (b) {
            Batching batching = batchingService.getById(batchUse.getBatchId());
            Shop shop = shopService.getById(batchUse.getShopId());
            InterfaceLog interfaceLog = new InterfaceLog();
            interfaceLog.setTitle("商户删除配料");
            interfaceLog.setMethodName("");
            String content = "商户"+shop.getName()+"删除配料，配料名为"+batching.getName();
            interfaceLog.setContent(content);
            interfaceLog.setShopId(batchUse.getShopId());
            interfaceLog.setTypeStatus(0);
            interfaceLog.setCreateTime(LocalDateTime.now());
            interfaceLog.setUpdateTime(LocalDateTime.now());
            interfaceLogService.save(interfaceLog);
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 选择机器
     */
    @GetMapping("getMachineList/{adminId}")
    public R getMachineList(@PathVariable Long adminId) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
        Shop shop = shopService.getOne(queryWrapper);
        String equipmentId = shop.getEquipmentId();

        List<String> machineList = new ArrayList<>();
        if (null != equipmentId && !equipmentId.equals("")){
            machineList = Arrays.asList(equipmentId.split(","));
        }
        return R.ok().data("machineList", machineList);
    }

    /**
     * 竖屏清洗机器
     */
    @PostMapping("clearMachine/{adminId}")
    public R clearMachine(@PathVariable Long adminId, @RequestParam String equipmentId) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
        Shop shop = shopService.getOne(queryWrapper);
        Long shopId = shop.getId();

        Integer num = 200;
        LambdaQueryWrapper<BatchUse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, shopId);
        List<BatchUse> list = batchUseService.list(wrapper);

        HashMap<Object, Object> arr = new HashMap<>();
        HashMap<Object, Integer> arr1 = new HashMap<>();

        if (null != list && list.size() > 0) {
            list.stream().forEach(item -> {
                arr.put(item.getOutlet() + "T", num);
                arr1.put(item.getOutlet(), 1);
            });
        }


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
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            resp = httpClient.execute(httpPost);
            int statusCode = resp.getStatusLine().getStatusCode();
            log.info("清洗11111 = " + statusCode);
            String bodyAsString = EntityUtils.toString(resp.getEntity());
            if (statusCode == 201) { //处理成功
                log.info("清洗成功，返回结果 = " + bodyAsString);
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
                log.info("清洗请求参数11111：" + jsonParamsMap2);

                StringEntity entity2 = new StringEntity(jsonParamsMap2, "utf-8");
                entity2.setContentType("application/json");
                httpPost2.setEntity(entity2);
                httpPost2.setHeader("Accept", "application/json");
                httpPost2.setHeader("Authorization", mToekn);
                resp = httpClient.execute(httpPost2);
                int statusCode2 = resp.getStatusLine().getStatusCode();
                log.info("清洗statusCode2222 = " + statusCode2);
                String bodyAsString2 = EntityUtils.toString(resp.getEntity());
                log.info("清洗机器请求结果222222222 = " + bodyAsString2);
                if (statusCode2 == 200) { //处理成功
                    log.info("清洗机器请求结果 = " + bodyAsString2);

                    //    rmN6tWDTWmkdvda1W25inQ
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
                    log.info("清洗请求参数222222：" + jsonParamsMap3);

                    StringEntity entity3 = new StringEntity(jsonParamsMap3, "utf-8");
                    entity3.setContentType("application/json");
                    httpPost3.setEntity(entity3);
                    httpPost3.setHeader("Accept", "application/json");
                    httpPost3.setHeader("Authorization", mToekn);
                    resp = httpClient.execute(httpPost3);
                    int statusCode3 = resp.getStatusLine().getStatusCode();
                    log.info("清洗statusCode333333 = " + statusCode3);
                    String bodyAsString3 = EntityUtils.toString(resp.getEntity());
                    log.info("清洗机器请求结果333333 = " + bodyAsString3);
                    if (statusCode3 == 200) { //处理成功
                        log.info("清洗机器请求结果222222 = " + bodyAsString3);


                    }

                }

            }
            resp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setTitle("清洗机器");
        interfaceLog.setMethodName("");
        String content = "商户"+shop.getName()+"清洗机器";
        interfaceLog.setContent(content);
        interfaceLog.setShopId(shopId);
        interfaceLog.setTypeStatus(0);
        interfaceLog.setCreateTime(LocalDateTime.now());
        interfaceLog.setUpdateTime(LocalDateTime.now());
        interfaceLogService.save(interfaceLog);
        return R.ok();
    }


    /**
     * 机器排出空气
     */
    @PostMapping("dischargeAir/{adminId}")
    public R dischargeAir(@PathVariable Long adminId, @RequestParam String equipmentId) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1).eq(Shop::getAdminId, adminId);
        Shop shop = shopService.getOne(queryWrapper);
        Long shopId = shop.getId();

        Integer num = 100;
        LambdaQueryWrapper<BatchUse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, shopId);
        List<BatchUse> list = batchUseService.list(wrapper);

        HashMap<Object, Object> arr = new HashMap<>();
        HashMap<Object, Integer> arr1 = new HashMap<>();

        if (null != list && list.size() > 0) {
            list.stream().forEach(item -> {
                arr.put(item.getOutlet() + "T", num);
                arr1.put(item.getOutlet(), 1);
            });
        }


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
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            resp = httpClient.execute(httpPost);
            int statusCode = resp.getStatusLine().getStatusCode();
            log.info("清洗11111 = " + statusCode);
            String bodyAsString = EntityUtils.toString(resp.getEntity());
            if (statusCode == 201) { //处理成功
                log.info("清洗成功，返回结果 = " + bodyAsString);
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
                log.info("清洗请求参数11111：" + jsonParamsMap2);

                StringEntity entity2 = new StringEntity(jsonParamsMap2, "utf-8");
                entity2.setContentType("application/json");
                httpPost2.setEntity(entity2);
                httpPost2.setHeader("Accept", "application/json");
                httpPost2.setHeader("Authorization", mToekn);
                resp = httpClient.execute(httpPost2);
                int statusCode2 = resp.getStatusLine().getStatusCode();
                log.info("清洗statusCode2222 = " + statusCode2);
                String bodyAsString2 = EntityUtils.toString(resp.getEntity());
                log.info("清洗机器请求结果222222222 = " + bodyAsString2);
                if (statusCode2 == 200) { //处理成功
                    log.info("清洗机器请求结果 = " + bodyAsString2);

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
                    log.info("清洗请求参数222222：" + jsonParamsMap3);

                    StringEntity entity3 = new StringEntity(jsonParamsMap3, "utf-8");
                    entity3.setContentType("application/json");
                    httpPost3.setEntity(entity3);
                    httpPost3.setHeader("Accept", "application/json");
                    httpPost3.setHeader("Authorization", mToekn);
                    resp = httpClient.execute(httpPost3);
                    int statusCode3 = resp.getStatusLine().getStatusCode();
                    log.info("清洗statusCode333333 = " + statusCode3);
                    String bodyAsString3 = EntityUtils.toString(resp.getEntity());
                    log.info("清洗机器请求结果333333 = " + bodyAsString3);
                    if (statusCode3 == 200) { //处理成功
                        log.info("清洗机器请求结果222222 = " + bodyAsString3);


                    }

                }

            }
            resp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setTitle("清洗机器");
        interfaceLog.setMethodName("");
        String content = "商户"+shop.getName()+"清洗机器";
        interfaceLog.setContent(content);
        interfaceLog.setShopId(shopId);
        interfaceLog.setTypeStatus(0);
        interfaceLog.setCreateTime(LocalDateTime.now());
        interfaceLog.setUpdateTime(LocalDateTime.now());
        interfaceLogService.save(interfaceLog);
        return R.ok();
    }


}

