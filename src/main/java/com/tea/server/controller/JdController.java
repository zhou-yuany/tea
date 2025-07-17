package com.tea.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tea.server.config.WeChatProperties;
import com.tea.server.entity.*;
import com.tea.server.entity.ele.ElmOrder;
import com.tea.server.entity.jd.*;
import com.tea.server.entity.json.JsonConverter;
import com.tea.server.service.*;
import com.tea.server.socket.CodeData;
import com.tea.server.socket.DeviceWebSocket;
import com.tea.server.socket.PlatformWebSocket;
import com.tea.server.utils.R;
import eleme.openapi.sdk.api.entity.order.OGoodsGroup;
import eleme.openapi.sdk.api.entity.order.OGoodsItem;
import eleme.openapi.sdk.api.entity.order.OGroupItemAttribute;
import eleme.openapi.sdk.api.entity.order.OGroupItemSpec;
import eleme.openapi.sdk.api.entity.product.*;
import eleme.openapi.sdk.api.entity.shop.OShop;
import eleme.openapi.sdk.api.enumeration.product.OItemCreateProperty;
import eleme.openapi.sdk.api.enumeration.product.OItemUpdateProperty;
import eleme.openapi.sdk.api.service.ProductService;
import eleme.openapi.sdk.api.service.ShopService;
import eleme.openapi.sdk.config.Config;
import eleme.openapi.sdk.oauth.OAuthClient;
import eleme.openapi.sdk.oauth.response.Token;
import eleme.openapi.sdk.utils.PropertiesUtils;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import o2o.openplatform.sdk.SDKTests;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("jd")
public class JdController {


    // 设置是否沙箱环境
    private static final boolean isSandbox = true;
    // 设置APPKEY
    private static final String appKey = "HTsjSlMBbm";
    // 设置APPSECRET
    private static final String appSecret = "bb5041114807043c51a59b208609ac11d97c9136";

    private static final String ACCESS_TOKEN = "your_access_token";
    // 初始化OAuthClient
    private static OAuthClient client = null;
    private static Map<String, String> tokenMap = new HashMap<String, String>();
    private static Config config = null;

    static {
        // 初始化全局配置工具
        config = new Config(isSandbox, appKey, appSecret);
        client = new OAuthClient(config);
    }

    @Autowired
    private ShopsService shopsService;

    @Autowired
    private CategorizeService categorizeService;

    @Autowired
    private CategorizeAllService categorizeAllService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private ParamCateService paramCateService;

    @Autowired
    private GoodsToBatchService goodsToBatchService;

    @Autowired
    private GoodsAllBatchService goodsAllBatchService;

    @Autowired
    private BatchingService batchingService;

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderParamService orderParamService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private OrdersFlowService ordersFlowService;

    @Autowired
    private AgentsService agentsService;

    @Autowired
    private BatchUseService batchUseService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    @Autowired
    private JsonConverter jsonConverter;



    /**
     * 发送POST请求
     */
    private static String sendPostRequest(String urlStr, Map<String, String> params) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        // 构建参数字符串
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(param.getKey())
                    .append('=')
                    .append(java.net.URLEncoder.encode(param.getValue(), "UTF-8"));
        }

        // 发送请求
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = postData.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 读取响应
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }


    // @ApiOperation("京东授权更新token")
    // @GetMapping("updateToken/{shopId}")
    // public void updateToken(@PathVariable Long shopId) throws Exception {
    //     Shop shop = shopsService.getById(shopId);
    //     Map<String, String> businessParams = new HashMap<>();
    //     businessParams.put("oldToken", shop.getJdAccessToken()); // 店内分类编号
    //     // TODO  获取新token
    //     businessParams.put("newToken", ""); // 店内分类名称
    //
    //     businessParams.put("appKey", appKey); // 分类类型
    //     // 调用接口
    //     String paramJson = new ObjectMapper().writeValueAsString(businessParams);
    //
    //     // 调用API
    //     String response = executeApi("/djapi/ApplicationService/verificationUpdateToken", paramJson);
    //
    //     System.out.println("API Response: " + response);
    //     JSONObject json = JSON.parseObject(response);
    //     JSONObject data = json.getJSONObject("data");
    //     Integer code = json.getInteger("code");
    //     if (code == 0 && data != null && json.getInteger("code") == 0) {
    //         // TODO  保存获取的新token
    //         shop.setJdAccessToken("");
    //         shopsService.updateById(shop);
    //     }
    // }


    public static String executeApi(String apiPath, String paramJson) throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("jd_param_json", paramJson);
        params.put("app_key", appKey);
        params.put("token", ACCESS_TOKEN);
        params.put("timestamp", getCurrentTime());
        params.put("format", "json");
        params.put("v", "1.0");


        // 生成签名
        String sign = generateSign(params);

        // 构建完整URL
        String fullUrl = buildGetUrl(apiPath, params, sign);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            if (fullUrl.length() < 1024) {
                // GET请求
                HttpGet httpGet = new HttpGet(fullUrl);
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    return EntityUtils.toString(response.getEntity());
                }
            } else {
                // POST请求
                HttpPost httpPost = new HttpPost("https://openapi.jddj.com" + apiPath);

                List<NameValuePair> formParams = new ArrayList<>();
                params.forEach((k, v) -> formParams.add(new BasicNameValuePair(k, v)));
                formParams.add(new BasicNameValuePair("sign", sign));

                httpPost.setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));

                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    return EntityUtils.toString(response.getEntity());
                }
            }
        }
    }
    private static String buildGetUrl(String apiPath, Map<String, String> params, String sign) {
        StringBuilder urlBuilder = new StringBuilder(apiPath).append("?");

        params.forEach((key, value) -> {
            try {
                urlBuilder.append(key).append("=")
                        .append(URLEncoder.encode(value, String.valueOf(StandardCharsets.UTF_8)))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        urlBuilder.append("sign=").append(sign);
        return urlBuilder.toString();
    }

    /**
     * 生成签名(MD5)
     */
    private static String generateSign(Map<String, String> params) {
        // 按照key升序排列
        StringBuilder signStr = new StringBuilder(appSecret);

        params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    signStr.append(entry.getKey()).append(entry.getValue());
                });

        signStr.append(appSecret);

        // MD5加密并转为大写
        return DigestUtils.md5Hex(signStr.toString()).toUpperCase();
    }

    /**
     * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(new Date());
    }


    /**
     * 授权码(企业)模式获取Token
     */
    @ApiOperation(value = "授权码(企业)模式获取Token")
    @GetMapping("callback")
    private void serverTokenTest(@RequestBody String jsonData, HttpServletResponse res) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JdToken token = mapper.readValue(jsonData, JdToken.class);

        LambdaQueryWrapper<Shop> shopWrapper = new LambdaQueryWrapper<>();
        shopWrapper.eq(Shop :: getStatus, 1).eq(Shop :: getJdShopId, token.getVenderId());
        Shop shop = shopsService.getOne(shopWrapper);
        if (null != shop){
            Map<String, String> businessParams = new HashMap<>();
            businessParams.put("oldToken", shop.getJdAccessToken());
            // 获取新token
            businessParams.put("newToken", token.getToken());

            businessParams.put("appKey", appKey);
            // 调用接口
            String paramJson = new ObjectMapper().writeValueAsString(businessParams);

            // 调用API
            String response = executeApi("/djapi/ApplicationService/verificationUpdateToken", paramJson);

            System.out.println("API Response: " + response);
            JSONObject json = JSON.parseObject(response);
            JSONObject data = json.getJSONObject("data");
            Integer code = json.getInteger("code");
            if (code == 0 && data != null && json.getInteger("code") == 0) { // TODO
                // 保存获取的新token
                shop.setJdAccessToken(token.getToken());
                shopsService.updateById(shop);
                // 设置要跳转到的URL地址
                // 返回状态码给京东接口
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("code", "0");
                responseBody.put("msg", "success");
                responseBody.put("data", "");

                // 设置 Content-Type 并返回 JSON
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(mapper.writeValueAsString(responseBody));
            }else{
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("code", "1");
                responseBody.put("msg", "error");
                responseBody.put("data", "");
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(mapper.writeValueAsString(responseBody));
            }

        }else{
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("code", "1");
            responseBody.put("msg", "error");
            responseBody.put("data", "");
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write(mapper.writeValueAsString(responseBody));
        }

    }

    // @ApiOperation("推送订单回调get(饿了么测试接口是否可用)")
    // @GetMapping("push")
    // public JSONObject elmTestGet() {
    //     JSONObject jsonObject = new JSONObject();
    //
    //     jsonObject.put("message", "ok");//必须返回
    //     return jsonObject;
    // }

    /**
     * 饿了么 消息推送
     */
    @ApiOperation(value = "消息推送")
    @PostMapping("push")
    private R push(@RequestBody ElmOrder elemReqDto) throws IOException, ServletException {
        //这里也可以用Map<String,Object> 先接收看看参数
        if (null != elemReqDto) {
            // log.info("当前推送订单状态："+elemReqDto.getType());
            // log.warn("当前推送订单状态："+elemReqDto.getType());
            String message = elemReqDto.getMessage();
            JSONObject orderJson = JSONObject.parseObject(message);
            String orderId = orderJson.getString("orderId");
            // log.info("orderId："+orderId);
            log.info("orderJson：" + orderJson);
            // log.info("elemReqDto.getType()："+elemReqDto.getType());
            // log.warn("orderId："+orderId);
            // log.warn("orderJson："+orderJson);
            // log.warn("elemReqDto.getType()："+elemReqDto.getType());
            if (elemReqDto.getType() == 10) {
                //订单生效（用户付款即生成）

                LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Orders :: getStatus, 1).eq(Orders :: getOrderNum, orderId);
                Orders ordersServiceOne = ordersService.getOne(wrapper);
                if (null == ordersServiceOne) {

                    Orders orders = new Orders();
                    String description = orderJson.getString("description");
                    orders.setNotes(description);
                    // 转list
                    JSONArray groups = orderJson.getJSONArray("groups");
                    Integer totalCount = 0;
                    List<OGoodsGroup> oGoodsGroups = JSONArray.parseArray(groups.toJSONString(), OGoodsGroup.class);
                    if (null != oGoodsGroups && oGoodsGroups.size() > 0) {
                        for (int i = 0; i < oGoodsGroups.size(); i++) {
                            OGoodsGroup oGoodsGroup = oGoodsGroups.get(i);
                            List<OGoodsItem> items = oGoodsGroup.getItems();
                            if (null != items && items.size() > 0) {
                                for (int j = 0; j < items.size(); j++) {
                                    totalCount += items.get(j).getQuantity();
                                }
                            }
                        }
                    }
                    Long shopId = orderJson.getLong("shopId");
                    log.info("******************************shopId:" + shopId);
                    LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
                    wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
                    Shop shop = shopsService.getOne(wrapper2);
                    log.info("******************************shop:" + shop);
                    orders.setTotalCount(totalCount);
                    orders.setOrderNum(orderId);
                    LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
                    LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);

                    Integer no = 0;

                    if (null != shop) {
                        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(Orders::getStatus, 1).eq(Orders::getShopId, shop.getId()).gt(Orders::getCreateTime, startTime).lt(Orders::getCreateTime, endTime).orderByDesc(Orders::getId).last("limit 1");
                        Orders obj = ordersService.getOne(queryWrapper);
                        if (!ObjectUtils.isEmpty(obj)) {
                            no = obj.getPickupCode() + 1;
                        }
                        orders.setShopId(shop.getId());
                    }

                    // Integer daySn = orderJson.getInteger("daySn"); //店铺当日订单流水号
                    // orders.setPickupCode(daySn);
                    orders.setPickupCode(no);
                    String createdAt = orderJson.getString("createdAt");
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    LocalDateTime dateTime = LocalDateTime.parse(createdAt, formatter);
                    orders.setCreateTime(dateTime);
                    orders.setUpdateTime(dateTime);
                    // Integer orderBusinessType = orderJson.getInteger("orderBusinessType");
                    orders.setOrderType(1);
                    orders.setSerialNum(orderId);
                    orders.setPayStatus(2);

                    orders.setAdminId(shop.getAdminId());

                    JSONArray phoneList = orderJson.getJSONArray("phoneList");
                    List<String> stringList = JSONArray.parseArray(phoneList.toJSONString(), String.class);
                    orders.setPhone(stringList.get(0));
                    orders.setPlatformType(2);
                    orders.setPayType(1);
                    Double totalAmount = orderJson.getDouble("totalAmount");
                    orders.setTotalPrice(BigDecimal.valueOf(totalAmount));
                    orders.setIsCall(2);
                    orders.setIsTake(2);
                    orders.setEleId(shopId);
                    // orders.setOpenid();
                    ordersService.save(orders);

                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("饿了么订单支付成功");
                    interfaceLog.setMethodName("updateOrders");
                    String content = "用户订单支付成功，订单编号为"+orders.getOrderNum()+"，订单金额为："+BigDecimal.valueOf(totalAmount);
                    interfaceLog.setContent(content);
                    interfaceLog.setShopId(orders.getShopId());
                    interfaceLog.setTypeStatus(0);
                    interfaceLog.setCreateTime(LocalDateTime.now());
                    interfaceLog.setUpdateTime(LocalDateTime.now());
                    interfaceLogService.save(interfaceLog);

                    // 新增分销账单
                    List<Long> ids = new ArrayList<>();
                    ids.add(orders.getShopId());
                    LambdaQueryWrapper<Agents> wrapper10 = new LambdaQueryWrapper<>();
                    wrapper10.eq(Agents::getStatus, 1).in(Agents::getShopId, ids).eq(Agents::getIsUse, 1);
                    Agents agentsServiceOne = agentsService.getOne(wrapper10);

                    OrdersFlow ordersFlow = new OrdersFlow();
                    ordersFlow.setAdminId(orders.getAdminId());
                    ordersFlow.setShopId(orders.getShopId());
                    ordersFlow.setOrderId(orders.getId());

                    ordersFlow.setPrice(orders.getTotalPrice());
                    ordersFlow.setPayType(1);

                    ordersFlow.setPayPlatform(1);
                    ordersFlow.setNumber(orders.getTotalCount());
                    ordersFlow.setSerialNum(orders.getOrderNum());
                    // String goodsName = list.stream().map(item -> item.getName()).collect(Collectors.joining(","));
                    // ordersFlow.setGoodsName(goodsName);
                    BigDecimal price = new BigDecimal(0L);
                    BigDecimal price2 = new BigDecimal(0L);

                    Shop shop2 = new Shop();
                    BeanUtils.copyProperties(shop, shop2);

                    InterfaceLog interfaceLog2 = new InterfaceLog();
                    String content2 = "饿了么分账";

                    if (shop.getDivideAccounts() == 1) {
                        // 扣除商户余额
                        Integer proportion = shop.getProportion();
                        price = BigDecimal.valueOf(totalAmount * proportion).divide(BigDecimal.valueOf(100));
                        BigDecimal totalP = BigDecimal.valueOf(totalAmount);

                        ordersFlow.setType(2);
                        ordersFlow.setShopDistributionPrice(totalP.subtract(price));
                        shop2.setBalance(shop.getBalance().subtract(totalP.subtract(price)));
                        content2 = content2+"，商户出账金额为"+totalP.subtract(price).setScale(2, RoundingMode.HALF_UP)+"元";

                        if (shop.getBalance().subtract(totalP.subtract(price)).compareTo(BigDecimal.valueOf(500)) < 0) {
                            DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "余额不足"));
                        }
                        if (shop.getBalance().subtract(totalP.subtract(price)).compareTo(BigDecimal.valueOf(0)) < 0) {
                            Orders orders1 = new Orders();
                            BeanUtils.copyProperties(orders, orders1);
                            PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "禁用"));

                        }
                        if (null != agentsServiceOne) {
                            ordersFlow.setAgentId(agentsServiceOne.getId());
                            if (agentsServiceOne.getDivideAccounts() == 1) { // 代理
                                BigDecimal aa = BigDecimal.valueOf(agentsServiceOne.getProportion()).divide(BigDecimal.valueOf(100));
                                price2 = orders.getTotalPrice().multiply(aa);
                                ordersFlow.setDistributionPrice(price2);
                                content2 = content2+"，代理入账金额为"+price2.setScale(2, RoundingMode.HALF_UP)+"元";

                            }
                        }
                        ordersFlow.setPlatDistributionPrice(orders.getTotalPrice().subtract(price).subtract(price2));

                    } else {
                        ordersFlow.setType(1);
                        price = orders.getTotalPrice();
                        shop2.setBalance(shop.getBalance().add(BigDecimal.valueOf(totalAmount)));
                        ordersFlow.setShopDistributionPrice(BigDecimal.valueOf(totalAmount));
                        content2 = content2+"，商户入账金额为"+BigDecimal.valueOf(totalAmount).setScale(2, RoundingMode.HALF_UP)+"元";
                    }



                    shopsService.updateById(shop);

                    ordersFlow.setPayStatus(1);

                    ordersFlow.setCreateTime(LocalDateTime.now());
                    ordersFlow.setUpdateTime(LocalDateTime.now());
                    ordersFlowService.save(ordersFlow);


                    interfaceLog2.setTitle("饿了么分账");
                    interfaceLog2.setMethodName("startOrders");
                    content2 = content2+",平台入账"+orders.getTotalPrice().subtract(price).subtract(price2).setScale(2, RoundingMode.HALF_UP)+"元";
                    interfaceLog2.setContent(content2);
                    interfaceLog2.setShopId(orders.getShopId());
                    interfaceLog2.setTypeStatus(0);
                    interfaceLog2.setCreateTime(LocalDateTime.now());
                    interfaceLog2.setUpdateTime(LocalDateTime.now());
                    interfaceLogService.save(interfaceLog2);

                    List<String> paramList = new ArrayList<>();


                    if (null != oGoodsGroups && oGoodsGroups.size() > 0) {
                        for (int i = 0; i < oGoodsGroups.size(); i++) {
                            OGoodsGroup oGoodsGroup = oGoodsGroups.get(i);
                            List<OGoodsItem> items = oGoodsGroup.getItems();
                            if (null != items && items.size() > 0) {
                                for (int j = 0; j < items.size(); j++) {
                                    for (int k = 0; k < items.get(j).getQuantity(); k++) {
                                        OrderParam orderParam = new OrderParam();
                                        // BigDecimal total = BigDecimal.valueOf(items.get(j).getTotal());
                                        orderParam.setPrice(BigDecimal.valueOf(items.get(j).getPrice()));
                                        // orderParam.setNumber(items.get(j).getQuantity());
                                        orderParam.setNumber(1);
                                        orderParam.setOrderId(orders.getId());
                                        orderParam.setCreateTime(LocalDateTime.now());
                                        orderParam.setUpdateTime(LocalDateTime.now());
                                        String originalName = items.get(j).getOriginalName();
                                        int index = originalName.indexOf("-");
                                        String name = originalName.substring(0, index);
                                        LambdaQueryWrapper<ShopToGoods> wrapper3 = new LambdaQueryWrapper<>();
                                        wrapper3.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getName, name).eq(ShopToGoods::getShopId, shop.getId());
                                        ShopToGoods goods = shopToGoodsService.getOne(wrapper3);
                                        orderParam.setGoodsId(goods.getId());
                                        orderParam.setName(goods.getName());
                                        orderParam.setUrl(goods.getUrl());
                                        String specifications = "";
                                        List<OGroupItemSpec> newSpecs = items.get(j).getNewSpecs();// 规格
                                        OGroupItemSpec oGroupItemSpec = newSpecs.get(0);
                                        String cupSize = oGroupItemSpec.getValue();
                                        LambdaQueryWrapper<Param> wrapper1 = new LambdaQueryWrapper<>();
                                        wrapper1.eq(Param::getStatus, 1).eq(Param::getName, cupSize);
                                        Param sizeObj = paramService.getOne(wrapper1);
                                        orderParam.setSizeId(sizeObj.getId());


                                        List<OGroupItemAttribute> attributes = items.get(j).getAttributes(); // 属性
                                        if (null != attributes && attributes.size() > 0) {
                                            for (int m = 0; m < attributes.size(); m++) {
                                                LambdaQueryWrapper<Param> wrapper5 = new LambdaQueryWrapper<>();
                                                wrapper5.eq(Param::getStatus, 1).eq(Param::getName, attributes.get(m).getValue());
                                                Param attrObj = paramService.getOne(wrapper5);
                                                if (attributes.get(m).getName().contains("糖") || attributes.get(m).getName().contains("甜")) {
                                                    orderParam.setSugarId(attrObj.getId());
                                                } else {
                                                    orderParam.setTemperatureId(attrObj.getId());
                                                }
                                                specifications += attributes.get(m).getValue() + "，";
                                            }
                                        }
                                        StringBuilder sb = new StringBuilder(specifications);
                                        sb.insert(0, cupSize + "，"); // 在索引0的位置插入字符
                                        String modifiedString = sb.toString();
                                        int lastIndex = modifiedString.lastIndexOf("，");
                                        modifiedString = modifiedString.substring(0, lastIndex);
                                        orderParam.setSpecifications(modifiedString);
                                        orderParamService.save(orderParam);

                                        // 查询配料是否充足

                                        LambdaQueryWrapper<GoodsToBatch> goodWrapper = new LambdaQueryWrapper<>();
                                        goodWrapper.eq(GoodsToBatch::getShopId, orders.getShopId()).eq(GoodsToBatch::getGoodsId, goods.getId()).eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getSizeId, orderParam.getSizeId()).eq(GoodsToBatch::getSugarId, orderParam.getSugarId()).eq(GoodsToBatch::getTemperatureId, orderParam.getTemperatureId());
                                        List<GoodsToBatch> goodsToBatches = goodsToBatchService.list(goodWrapper);
                                        if (goodsToBatches.size() > 0) {
                                            goodsToBatches.stream().forEach(batchInfo -> {
                                                LambdaQueryWrapper<BatchUse> batchWrapper = new LambdaQueryWrapper<>();
                                                batchWrapper.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batchInfo.getBatchId());
                                                BatchUse batchUse = batchUseService.getOne(batchWrapper);
                                                if (null != batchUse) {
                                                    if (batchUse.getTotalCount() < batchInfo.getUseNumber()) {
                                                        Batching batching = batchingService.getById(batchUse.getBatchId());
                                                        // count[0] = 1;
                                                        // paramName[0] += paramName[0] + batching.getName() + "、";
                                                        paramList.add(batching.getName());
                                                    }

                                                }

                                            });
                                        }
                                    }
                                }
                            }
                        }
                    }

                    String messageInfo = "您有新订单啦";
                    if (paramList.size() > 0) {
                        String paramName = paramList.stream() // 将List转换为Stream
                                .distinct() // 去除重复元素
                                .collect(Collectors.joining(","));
                        messageInfo = paramName + "不足";
                        // message = paramName[0].substring(0, paramName[0].length() - 1) + "配料不足";
                        // Arrays.stream(paramName).distinct().toArray();
                    }
                    log.info("messageInfo:" + messageInfo);
                    DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", messageInfo));
                    DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "下单"));
                    PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", messageInfo));

                }
            }
            if (elemReqDto.getType() == 12) {//商户确认接单，更改状态，当商户接单时 需要插入富基中间表数据库

            }
            if (elemReqDto.getType() == 17) {//一分钟内订单取消，直接取消，更改订单状态
                // 根据订单编号删除订单

                Long shopId = orderJson.getLong("shopId");
                LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
                Shop shop = shopsService.getOne(wrapper2);


                LambdaQueryWrapper<Orders> ordersWrapper2 = new LambdaQueryWrapper<>();
                ordersWrapper2.eq(Orders::getStatus, 1).eq(Orders::getOrderNum, orderId);
                Orders one = ordersService.getOne(ordersWrapper2);
                if (null != one) {
                    LambdaQueryWrapper<OrdersFlow> flowWrapper = new LambdaQueryWrapper<>();
                    flowWrapper.eq(OrdersFlow::getStatus, 1).eq(OrdersFlow::getOrderId, one.getId());
                    OrdersFlow flowServiceOne = ordersFlowService.getOne(flowWrapper);
                    ordersFlowService.remove(flowWrapper);

                    if (shop.getDivideAccounts() == 1) {
                        Shop shop1 = new Shop();
                        BeanUtils.copyProperties(shop, shop1);
                        shop1.setBalance(shop.getBalance().add(flowServiceOne.getShopDistributionPrice()));
                        shopsService.updateById(shop1);
                    }

                    ordersService.removeById(one.getId());

                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("饿了么订单取消成功");
                    interfaceLog.setMethodName("updateOrders");
                    String content = "一分钟内订单取消，订单编号为"+one.getOrderNum();
                    interfaceLog.setContent(content);
                    interfaceLog.setShopId(shop.getId());
                    interfaceLog.setTypeStatus(0);
                    interfaceLog.setCreateTime(LocalDateTime.now());
                    interfaceLog.setUpdateTime(LocalDateTime.now());
                    interfaceLogService.save(interfaceLog);


                    DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "饿了么订单取消"));
                    PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "饿了么订单取消"));
                }

            }
            if (elemReqDto.getType() == 14) {//接单前取消订单，更改订单状态
                // 根据订单编号删除订单
                Long shopId = orderJson.getLong("shopId");
                LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
                Shop shop = shopsService.getOne(wrapper2);


                LambdaQueryWrapper<Orders> ordersWrapper2 = new LambdaQueryWrapper<>();
                ordersWrapper2.eq(Orders::getStatus, 1).eq(Orders::getOrderNum, orderId);
                Orders one = ordersService.getOne(ordersWrapper2);
                if (null != one) {
                    LambdaQueryWrapper<OrdersFlow> flowWrapper = new LambdaQueryWrapper<>();
                    flowWrapper.eq(OrdersFlow::getStatus, 1).eq(OrdersFlow::getOrderId, one.getId());
                    OrdersFlow flowServiceOne = ordersFlowService.getOne(flowWrapper);
                    ordersFlowService.remove(flowWrapper);

                    if (shop.getDivideAccounts() == 1) {
                        Shop shop1 = new Shop();
                        BeanUtils.copyProperties(shop, shop1);
                        shop1.setBalance(shop.getBalance().add(flowServiceOne.getShopDistributionPrice()));
                        shopsService.updateById(shop1);
                    }

                    ordersService.removeById(one.getId());

                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("饿了么订单取消成功");
                    interfaceLog.setMethodName("updateOrders");
                    String content = "接单前取消订单，订单编号为"+one.getOrderNum();
                    interfaceLog.setContent(content);
                    interfaceLog.setShopId(shop.getId());
                    interfaceLog.setTypeStatus(0);
                    interfaceLog.setCreateTime(LocalDateTime.now());
                    interfaceLog.setUpdateTime(LocalDateTime.now());
                    interfaceLogService.save(interfaceLog);


                    DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "饿了么订单取消"));
                    PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "饿了么订单取消"));
                }

            }
            if (elemReqDto.getType() == 15) {//一分钟后取消订单（商户同意取消订单），更改订单状态
                // 根据订单编号删除订单
                Long shopId = orderJson.getLong("shopId");
                LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
                Shop shop = shopsService.getOne(wrapper2);


                LambdaQueryWrapper<Orders> ordersWrapper2 = new LambdaQueryWrapper<>();
                ordersWrapper2.eq(Orders::getStatus, 1).eq(Orders::getOrderNum, orderId);
                Orders one = ordersService.getOne(ordersWrapper2);
                if (null != one) {
                    LambdaQueryWrapper<OrdersFlow> flowWrapper = new LambdaQueryWrapper<>();
                    flowWrapper.eq(OrdersFlow::getStatus, 1).eq(OrdersFlow::getOrderId, one.getId());
                    OrdersFlow flowServiceOne = ordersFlowService.getOne(flowWrapper);
                    ordersFlowService.remove(flowWrapper);

                    if (shop.getDivideAccounts() == 1) {
                        Shop shop1 = new Shop();
                        BeanUtils.copyProperties(shop, shop1);
                        shop1.setBalance(shop.getBalance().add(flowServiceOne.getShopDistributionPrice()));
                        shopsService.updateById(shop1);
                    }

                    ordersService.removeById(one.getId());

                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("饿了么订单取消成功");
                    interfaceLog.setMethodName("updateOrders");
                    String content = "一分钟后取消订单（商户同意取消订单），订单编号为"+one.getOrderNum();
                    interfaceLog.setContent(content);
                    interfaceLog.setShopId(shop.getId());
                    interfaceLog.setTypeStatus(0);
                    interfaceLog.setCreateTime(LocalDateTime.now());
                    interfaceLog.setUpdateTime(LocalDateTime.now());
                    interfaceLogService.save(interfaceLog);


                    DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "饿了么订单取消"));
                    PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "饿了么订单取消"));
                }


            }
            if (elemReqDto.getType() == 18) {//订单已完成，更改订单状态

            }
            if (elemReqDto.getType() == 20) {//用户申请退单
                // 根据订单编号删除订单
                // LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
                // wrapper.eq(Orders :: getStatus, 1).eq(Orders :: getOrderNum, orderId);
                // ordersService.remove(wrapper);
            }
            if (elemReqDto.getType() == 37) {//自动退单成功
                // 根据订单编号删除订单
                Long shopId = orderJson.getLong("shopId");
                LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
                Shop shop = shopsService.getOne(wrapper2);


                LambdaQueryWrapper<Orders> ordersWrapper2 = new LambdaQueryWrapper<>();
                ordersWrapper2.eq(Orders::getStatus, 1).eq(Orders::getOrderNum, orderId);
                Orders one = ordersService.getOne(ordersWrapper2);
                if (null != one) {
                    LambdaQueryWrapper<OrdersFlow> flowWrapper = new LambdaQueryWrapper<>();
                    flowWrapper.eq(OrdersFlow::getStatus, 1).eq(OrdersFlow::getOrderId, one.getId());
                    OrdersFlow flowServiceOne = ordersFlowService.getOne(flowWrapper);
                    ordersFlowService.remove(flowWrapper);

                    if (shop.getDivideAccounts() == 1) {
                        Shop shop1 = new Shop();
                        BeanUtils.copyProperties(shop, shop1);
                        shop1.setBalance(shop.getBalance().add(flowServiceOne.getShopDistributionPrice()));
                        shopsService.updateById(shop1);
                    }

                    ordersService.removeById(one.getId());

                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("饿了么订单取消成功");
                    interfaceLog.setMethodName("updateOrders");
                    String content = "自动退单成功，订单编号为"+one.getOrderNum();
                    interfaceLog.setContent(content);
                    interfaceLog.setShopId(shop.getId());
                    interfaceLog.setTypeStatus(0);
                    interfaceLog.setCreateTime(LocalDateTime.now());
                    interfaceLog.setUpdateTime(LocalDateTime.now());
                    interfaceLogService.save(interfaceLog);


                    DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "饿了么订单取消"));
                    PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "饿了么订单取消"));
                }

            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "ok");
        return R.ok();
    }


    /**
     * 授权码(企业)模式刷新Token
     */
    @ApiOperation(value = "授权码(企业)模式刷新Token")
    private void serverRefreshTokenTest() {
        String refreshTokenStr = "XXXXXXXXXXX";
        Token token = client.getTokenByRefreshToken(getToken().getRefreshToken());
        if (token.isSuccess()) {
            setTokenInfo(token);
            System.out.println(token);
        } else {
            System.out.println("error_code: " + token.getError());
            System.out.println("error_desc: " + token.getError_description());
        }
    }

    /**
     * 已获取Token信息后使用
     *
     * @return
     */
    @ApiOperation(value = "已获取Token信息后使用")
    private static Token getToken() {
        String access_token = PropertiesUtils.getPropValueByKey("access_token");
        String token_type = PropertiesUtils.getPropValueByKey("token_type");
        String expires_in = PropertiesUtils.getPropValueByKey("expires_in");
        String refresh_token = PropertiesUtils.getPropValueByKey("refresh_token");
        if (access_token.isEmpty()) {
            System.out.println("access_token is null");
            return null;
        }
        Token token = new Token();
        token.setAccessToken(access_token);
        token.setTokenType(token_type);
        token.setExpires(Long.valueOf(expires_in));
        token.setRefreshToken(refresh_token);
        return token;
    }

    @ApiOperation(value = "设置Token信息")
    private static void setTokenInfo(Token token) {
        if (null != token && token.isSuccess()) {
            tokenMap.put("access_token", token.getAccessToken());
            tokenMap.put("token_type", token.getTokenType());
            tokenMap.put("expires_in", String.valueOf(token.getExpires()));
            tokenMap.put("refresh_token", token.getRefreshToken());
            PropertiesUtils.setProps(tokenMap);
        }
    }

    @SneakyThrows
    @ApiOperation(value = "获取饿了么的店铺信息")
    @GetMapping("getShopInfo")
    public R getShopInfo(@RequestParam("shopId") Long shopId, @RequestParam("access_token") String access_token, @RequestParam("token_type") String token_type,
                         @RequestParam("expires_in") String expires_in, @RequestParam("refresh_token") String refresh_token) {

        Token token = new Token();
        token.setAccessToken(access_token);
        token.setTokenType(token_type);
        token.setExpires(Long.valueOf(expires_in));
        token.setRefreshToken(refresh_token);
        ShopService shopService = new ShopService(config, token);
        OShop shop = shopService.getShop(shopId);

        LambdaQueryWrapper<Shop> query = new LambdaQueryWrapper<>();
        query.eq(Shop::getEleShopId, shopId).eq(Shop::getStatus, 1);
        Shop one = shopsService.getOne(query);
        Shop shop1 = new Shop();
        BeanUtils.copyProperties(one, shop1);
        shop1.setAccessToken(access_token);  // 商户token信息
        shop1.setTokenType(token_type);  // 商户token信息
        shop1.setExpiresIn(Long.valueOf(expires_in));  // 商户token信息
        shop1.setRefreshToken(refresh_token);  // 商户token信息
        shop1.setName(shop.getName());
        // shop1.setBusyLevel(String.valueOf(shop.getBusyLevel()));
        // shop1.setDeliverSpent(shop.getDeliverSpent());
        // shop1.setDescription(shop.getDescription());
        // shop1.setImageUrl(shop.getImageUrl());
        // shop1.setInvoice(shop.getInvoice());
        // shop1.setInvoiceMinAmount(BigDecimal.valueOf(shop.getInvoiceMinAmount()));
        // shop1.setIsBookable(shop.getIsBookable());
        // shop1.setIsOpen(shop.getIsOpen());
        // shop1.setIsPhoneHidden(shop.getIsPhoneHidden());
        // shop1.setIsPremium(shop.getIsPremium());
        // shop1.setIsTimeEnsure(shop.getIsTimeEnsure());
        // shop1.setTimeEnsureFullDescription(shop.getTimeEnsureFullDescription());
        // shop1.setMobile(shop.getMobile());
        // shop1.setNoAgentFeeTotal(BigDecimal.valueOf(shop.getNoAgentFeeTotal()));
        // shop1.setOnlinePayment(shop.getOnlinePayment());
        // if (null != shop.getPhones() && shop.getPhones().size() > 0) {
        //     String phones = shop.getPhones().stream().collect(Collectors.joining(","));
        //     shop1.setPhones(phones);
        // }
        // shop1.setPromotionInfo(shop.getPromotionInfo());
        // shop1.setRecentFoodPopularity(shop.getRecentFoodPopularity());
        // shop1.setRecentFoodPopularityFuzzy(shop.getRecentFoodPopularityFuzzy());
        // if (null != shop.getServingTime() && shop.getServingTime().size() > 0) {
        //     String servingTime = shop.getServingTime().stream().collect(Collectors.joining(","));
        //     shop1.setServingTime(servingTime);
        // }
        // if (null != shop.getNumRatings() && shop.getNumRatings().size() > 0) {
        //     String numRatings = shop.getNumRatings().stream().map(String::valueOf).collect(Collectors.joining(","));
        //     shop1.setNumRatings(numRatings);
        // }
        // shop1.setFlexibleBusinessTime(shop.getFlexibleBusinessTime());
        // shop1.setSupportOnline(shop.getSupportOnline() ? 1 : 0);
        // shop1.setPackingFee(BigDecimal.valueOf(shop.getPackingFee()));
        // shop1.setOpenId(shop.getOpenId());
        // shop1.setOnlineRefund(shop.getOnlineRefund() ? 1 : 0);
        shop1.setUpdateTime(LocalDateTime.now());
        shopsService.updateById(shop1);


        // ProductService productService = new ProductService(config, token);
        // List<OCategory> shopCategories = productService.getShopCategories(one.getShopId());
        // // 保存分类
        // if (null != shopCategories && shopCategories.size() > 0){
        //     List<Long> ids = shopCategories.stream().map(item -> item.getId()).collect(Collectors.toList());
        //     LambdaQueryWrapper<Categorize> wrapper2 = new LambdaQueryWrapper<>();
        //     wrapper2.eq(Categorize :: getStatus, 1).eq(Categorize :: getShopId, shopId).notIn(Categorize :: getEleId, ids);
        //     categorizeService.remove(wrapper2);
        //     shopCategories.stream().forEach(item -> {
        //         if (null != item.getDescription() && !item.getDescription().equals("")){
        //             LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();
        //             wrapper.eq(Categorize :: getStatus, 1).eq(Categorize :: getEleId, item.getId()).eq(Categorize :: getShopId, shopId);
        //             Categorize categorize1 = categorizeService.getOne(wrapper);
        //             if (null == categorize1){
        //                 Categorize categorize = new Categorize();
        //                 categorize.setShopId(one.getId());
        //                 categorize.setName(item.getName());
        //                 categorize.setRealName(item.getDescription());
        //                 categorize.setEleId(item.getId());
        //                 categorize.setType(1);
        //                 categorize.setCreateTime(LocalDateTime.now());
        //                 categorize.setUpdateTime(LocalDateTime.now());
        //                 categorizeService.save(categorize);
        //             }else {
        //                 categorize1.setName(item.getName());
        //                 categorize1.setRealName(item.getDescription());
        //                 categorize1.setUpdateTime(LocalDateTime.now());
        //                 categorizeService.updateById(categorize1);
        //             }
        //         }
        //     });
        // }else {
        //     LambdaQueryWrapper<Categorize> wrapper3 = new LambdaQueryWrapper<>();
        //     wrapper3.eq(Categorize :: getStatus, 1).eq(Categorize :: getShopId, shopId);
        //     categorizeService.remove(wrapper3);
        // }

        // Map<OShopProperty,Object> properties = new HashMap<OShopProperty,Object>();
        // properties.put(OShopProperty.isOpen,1);
        // shopService.updateShop(shopId, properties);
        return R.ok().data("shopInfo", shop);

    }


    @SneakyThrows
    @ApiOperation(value = "新增京东商家店内分类信息接口")
    @PostMapping("createCategory")
    public R createCategory(@RequestBody Categorize obj) {
        // Token token = new Token();
        // token.setAccessToken(obj.getAccessToken());
        // token.setTokenType(obj.getTokenType());
        // token.setExpires(obj.getExpiresIn());
        // token.setRefreshToken(obj.getRefreshToken());
        //
        // Shop shop = shopsService.getById(obj.getShopId());
        // // 添加饿了么店铺商品分类
        // ProductService productService = new ProductService(config, token);
        // OCategory category = productService.createCategory(shop.getEleShopId(), obj.getName(), obj.getDescription());
        try {
            // 准备业务参数
            // ShopCategoryRequest shopCategoryRequest = new ShopCategoryRequest();
            // shopCategoryRequest.setShopCategoryName(obj.getName()); // 分类名称
            // shopCategoryRequest.setCategoryType(0); // 普通分类
            // shopCategoryRequest.setPid(0L); // 父分类ID，0表示一级分类
            // shopCategoryRequest.setShopCategoryLevel(1); // 分类等级

            // String jsonString = jsonConverter.toJson(shopCategoryRequest);

            Map<String, String> businessParams = new HashMap<>();
            businessParams.put("pid", "0L"); // 店内分类编号
            businessParams.put("shopCategoryName", obj.getName()); // 店内分类名称
            businessParams.put("categoryType", "0"); // 分类类型
            businessParams.put("shopCategoryLevel", "1"); // 分类等级
            // bizParams.put("optType", 0); // 可选，操作类型
            // bizParams.put("outStationNo", ""); // 可选，外部门店编号
            // 调用接口
            String response = addShopCategory(businessParams);
            System.out.println("API Response: " + response);
            JSONObject json = JSON.parseObject(response);
            JSONObject data = json.getJSONObject("data");
            Integer code = json.getInteger("code");
            if (code == 0 && data != null) {
                JSONObject result = data.getJSONObject("result");
                Long jdId = result != null ? Long.parseLong(result.getString("id")) : -1L;

                Categorize categorize = new Categorize();
                categorize.setName(obj.getName());
                categorize.setJdId(jdId);
                categorize.setType(1);
                CategorizeAll categorizeAll = categorizeAllService.getById(obj.getCateAllId());
                categorize.setCateAllId(obj.getCateAllId());
                categorize.setShopId(obj.getShopId());
                categorize.setDescription(obj.getDescription());
                categorize.setMachineNo(obj.getMachineNo());
                categorize.setRealName(categorizeAll.getName());
                categorize.setCreateTime(LocalDateTime.now());
                categorize.setUpdateTime(LocalDateTime.now());
                boolean save = categorizeService.save(categorize);
                if (save) {
                    Shop shop = shopsService.getById(obj.getShopId());
                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("添加京东秒送分类");
                    interfaceLog.setMethodName("createCategory");
                    String content = "商户"+shop.getName()+"添加京东秒送分类，分类名为"+obj.getName();
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
            } else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }


    }
    /**
     * 新增商家店内分类
     */
    public static String addShopCategory(Map<String, String> businessParams) throws Exception {


        String paramJson = new ObjectMapper().writeValueAsString(businessParams);

        // 调用API
        return executeApi("/djapi/pms/addShopCategory", paramJson);
    }


    @SneakyThrows
    @ApiOperation(value = "修改京东的店铺商品分类")
    @PostMapping("updateCategory")
    public R updateCategory(@RequestBody Categorize obj) {
        // Token token = new Token();
        // token.setAccessToken(obj.getAccessToken());
        // token.setTokenType(obj.getTokenType());
        // token.setExpires(obj.getExpiresIn());
        // token.setRefreshToken(obj.getRefreshToken());
        //
        //
        // // 修改饿了么店铺商品分类
        // ProductService productService = new ProductService(config, token);
        // OCategory category = productService.updateCategory(obj.getEleId(), obj.getName(), obj.getDescription());

        try {
            // 准备业务参数
            Map<String, String> businessParams = new HashMap<>();
            businessParams.put("id", String.valueOf(obj.getJdId())); // 店内分类编号
            businessParams.put("shopCategoryName", obj.getName()); // 店内分类名称
            businessParams.put("categoryType", "1"); // 分类类型
            // bizParams.put("optType", 0); // 可选，操作类型
            // bizParams.put("outStationNo", ""); // 可选，外部门店编号

            // 调用接口
            String response = updateShopCategory(businessParams);
            System.out.println("API Response: " + response);
            JSONObject json = JSON.parseObject(response);
            JSONObject data = json.getJSONObject("data");
            Integer code = json.getInteger("code");
        if (code == 0 && data != null) {
            Categorize categorize = categorizeService.getById(obj.getId());
            CategorizeAll categorizeAll = categorizeAllService.getById(obj.getCateAllId());
            categorize.setCateAllId(obj.getCateAllId());
            categorize.setName(obj.getName());
            categorize.setDescription(obj.getDescription());
            categorize.setMachineNo(obj.getMachineNo());
            categorize.setRealName(categorizeAll.getName());
            categorize.setUpdateTime(LocalDateTime.now());
            boolean save = categorizeService.updateById(categorize);
            if (save) {
                Shop shop = shopsService.getById(categorize.getShopId());
                InterfaceLog interfaceLog = new InterfaceLog();
                interfaceLog.setTitle("修改京东秒送分类");
                interfaceLog.setMethodName("updateCategory");
                String content = "商户"+shop.getName()+"修改京东秒送分类，分类名为"+obj.getName();
                interfaceLog.setContent(content);
                interfaceLog.setShopId(categorize.getShopId());
                interfaceLog.setTypeStatus(0);
                interfaceLog.setCreateTime(LocalDateTime.now());
                interfaceLog.setUpdateTime(LocalDateTime.now());
                interfaceLogService.save(interfaceLog);
                return R.ok();
            } else {
                return R.error();
            }
        } else {
            return R.error();
        }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }

    }

    /**
     * 修改商家店内分类
     */
    public static String updateShopCategory(Map<String, String> businessParams) throws Exception {
        // // 1. 准备系统参数
        // Map<String, String> systemParams = new HashMap<>();
        // systemParams.put("app_key", key);
        // systemParams.put("token", ACCESS_TOKEN);
        // systemParams.put("timestamp", getCurrentTime());
        // systemParams.put("format", "json");
        // systemParams.put("v", "1.0");
        //
        // String API_URL = "https://openapi.jddj.com/djapi/pms/updateShopCategory";
        // // 2. 合并所有参数
        // Map<String, String> allParams = new HashMap<>();
        // allParams.putAll(systemParams);
        // allParams.putAll(businessParams);
        //
        // // 3. 生成签名
        // String sign = generateSign(allParams, secret);
        // allParams.put("sign", sign);
        //
        // // 4. 发送POST请求
        // return sendPostRequest(API_URL, allParams);

        String paramJson = new ObjectMapper().writeValueAsString(businessParams);

        // 调用API
        return executeApi("/djapi/pms/updateShopCategory", paramJson);
    }

    @SneakyThrows
    @ApiOperation(value = "删除京东的店铺商品分类")
    @DeleteMapping("{jdId}")
    public R invalidCategory(@PathVariable("jdId") Long jdId, @RequestParam("access_token") String access_token, @RequestParam("token_type") String token_type,
                             @RequestParam("expires_in") String expires_in, @RequestParam("refresh_token") String refresh_token) {
        try {
            // 准备业务参数
            Map<String, String> businessParams = new HashMap<>();
            businessParams.put("id", String.valueOf(jdId)); // 店内分类编号


            // 调用接口
            String response = deleteShopCategory(businessParams);
            System.out.println("API Response: " + response);
            JSONObject json = JSON.parseObject(response);
            JSONObject data = json.getJSONObject("data");
            Integer code = json.getInteger("code");
            if(code == 0 && data != null){

            // 删除库里商品分类
            LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Categorize::getStatus, 1).eq(Categorize::getJdId, jdId);
            Categorize categorize = categorizeService.getOne(wrapper);
            boolean remove = categorizeService.removeById(categorize.getId());
            if (remove) {
                Shop shop = shopsService.getById(categorize.getShopId());
                InterfaceLog interfaceLog = new InterfaceLog();
                interfaceLog.setTitle("删除京东秒送分类");
                interfaceLog.setMethodName("");
                String content = "商户" + shop.getName() + "删除京东秒送分类，分类名为" + categorize.getName();
                interfaceLog.setContent(content);
                interfaceLog.setShopId(categorize.getShopId());
                interfaceLog.setTypeStatus(0);
                interfaceLog.setCreateTime(LocalDateTime.now());
                interfaceLog.setUpdateTime(LocalDateTime.now());
                interfaceLogService.save(interfaceLog);
                return R.ok();
            } else {
                return R.error();
            }
            }else {
                return R.error();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }


    }


    /**
     * 删除商家店内分类
     */
    public static String deleteShopCategory(Map<String, String> businessParams) throws Exception {
        // // 1. 准备系统参数
        // Map<String, String> systemParams = new HashMap<>();
        // systemParams.put("app_key", key);
        // systemParams.put("token", ACCESS_TOKEN);
        // systemParams.put("timestamp", getCurrentTime());
        // systemParams.put("format", "json");
        // systemParams.put("v", "1.0");
        //
        // String API_URL = "https://openapi.jddj.com/djapi/pms/delShopCategory";
        // // 2. 合并所有参数
        // Map<String, String> allParams = new HashMap<>();
        // allParams.putAll(systemParams);
        // allParams.putAll(businessParams);
        //
        // // 3. 生成签名
        // String sign = generateSign(allParams, secret);
        // allParams.put("sign", sign);
        //
        // // 4. 发送POST请求
        // return sendPostRequest(API_URL, allParams);

        String paramJson = new ObjectMapper().writeValueAsString(businessParams);

        // 调用API
        return executeApi("/djapi/pms/delShopCategory", paramJson);
    }

    @SneakyThrows
    @ApiOperation(value = "添加京东秒送的店铺商品")
    @PostMapping("createGoods")
    public R createGoods(@RequestBody ShopToGoods obj) {
        Shop shop = shopsService.getById(obj.getShopId());
        ShopToGoods shopToGoods = new ShopToGoods();
        LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getGoodsId, obj.getGoodsId()).eq(ShopToGoods::getShopId, obj.getShopId());
        List<ShopToGoods> list = shopToGoodsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该商品已添加");
        } else {

            // Token token = new Token();
            // token.setAccessToken(obj.getAccessToken());
            // token.setTokenType(obj.getTokenType());
            // token.setExpires(obj.getExpiresIn());
            // token.setRefreshToken(obj.getRefreshToken());


            // 获取商品基本价格
            // double goodsPrice = obj.getPrice().doubleValue();
            BigDecimal goodsPrice = BigDecimal.ZERO;

            Long cateId = obj.getCateId();
            Categorize categorize = categorizeService.getById(cateId);
            long timestamp = Instant.now().toEpochMilli();
            String outSpuId = shop.getJdShopId() + "" + timestamp;
            obj.setJdId(outSpuId);

            SaveSpuRequest spuRequest = new SaveSpuRequest();
            spuRequest.setOutSpuId(outSpuId);
            spuRequest.setSpuName(obj.getName());
            spuRequest.setShopCategories(Collections.singletonList(categorize.getJdId()));
            spuRequest.setCategoryId(categorize.getJdId());
            spuRequest.setBrandId(shop.getBrandId());
            spuRequest.setFixedStatus(1);
            spuRequest.setPackageFee(BigDecimal.ZERO); // 餐盒费


            // 上传图片
            List<String> images = new ArrayList<>();
            String imagePath = weChatProperties.getUploadPath() + obj.getUrl();
            // byte[] fileContent = Files.readAllBytes(new File(imagePath).toPath());
            // String base64String = Base64.getEncoder().encodeToString(fileContent);
            // String imageHash = productService.uploadImage(base64String);
            images.add(imagePath);
            spuRequest.setImages(images);
            spuRequest.setProductDesc(obj.getIntroduce());
            spuRequest.setIfViewDesc(0);

            // Map<OItemCreateProperty, Object> properties = new HashMap<OItemCreateProperty, Object>();
            // properties.put(OItemCreateProperty.name, obj.getName());
            // properties.put(OItemCreateProperty.description, obj.getIntroduce());

            //
            // BeanUtils.copyProperties(obj, shopToGoods);
            // shopToGoods.setCreateTime(LocalDateTime.now());
            // shopToGoods.setUpdateTime(LocalDateTime.now());
            // shopToGoods.setImageHash(obj.getImageHash());
            // shopToGoodsService.save(shopToGoods);


            // properties.put(OItemCreateProperty.imageHash, imageHash);

            // 获取规格参数
            LambdaQueryWrapper<Param> queryParam = new LambdaQueryWrapper<Param>();
            queryParam.eq(Param::getStatus, 1).eq(Param::getType, 1);
            List<Param> paramList = paramService.list(queryParam);

            LambdaQueryWrapper<Param> queryParam2 = new LambdaQueryWrapper<Param>();
            queryParam2.eq(Param::getStatus, 1).eq(Param::getType, 2);
            List<Param> sugarList = paramService.list(queryParam2);

            LambdaQueryWrapper<Param> queryParam3 = new LambdaQueryWrapper<Param>();
            queryParam3.eq(Param::getStatus, 1).eq(Param::getType, 3);
            List<Param> temperatureList = paramService.list(queryParam3);

            Integer count = (null != sugarList && sugarList.size() > 0 ? 1 : 0) + (null != temperatureList && temperatureList.size() > 0 ? 1 : 0);
            // List<OSpecProperty> specProperties = new ArrayList<>();
            // List<OSpecDetail> specDetails = new ArrayList<>();
            // List<OSpec> oSpecs = new ArrayList<>();

            List<SaleAttrRelationInfo> saleAttrRelationInfoList = new ArrayList<>(); // 决定价格库存的销售属性List，最多支持5组属性
            List<LightFoodSkuInfoRequest> skuMainInfoList = new ArrayList<>(); // saleAttrRelationInfoList传值时spu下所有sku信息全部必填
            if (null != paramList && paramList.size() > 0) {
                    SaleAttrRelationInfo saleAttrRelationInfo = new SaleAttrRelationInfo();
                    saleAttrRelationInfo.setAttrName("规格");
                    List<String> saleAttrValueNameList = new ArrayList<>();


                List<String> outSkuIdList = new ArrayList<>();
                List<SaleAttrValueRelationInfo> saleAttrValues = new ArrayList<>(); // 需要和SPU的销售属性的顺序保持一致。【新增时必填；销售属性信息有变化时必填]
                paramList.stream().forEach(item -> {
                    LightFoodSkuInfoRequest skuMainInfo = new LightFoodSkuInfoRequest();
                    long timestamp2 = Instant.now().toEpochMilli();
                    String outSkuId = shop.getJdShopId() + "" + timestamp2;
                    skuMainInfo.setOutSkuId(outSkuId); // 外部商品编码如需修改outskuid，需要传saleAttrRelationInfoList。【新增、修改sku信息时必填】

                    skuMainInfo.setFixedStatus(1); // sku上下架状态 1上架、2下架、4删除 上架的商品不能直接删除，需要先设置为下架，然后再删除。【新增时必填，修改时非必填】
                    outSkuIdList.add(outSkuId);

                    SaleAttrValueRelationInfo saleAttrValueRelationInfo = new SaleAttrValueRelationInfo();
                    saleAttrValueRelationInfo.setSaleAttrName("规格");
                    saleAttrValueRelationInfo.setSaleAttrValueName(item.getName());
                    saleAttrValues.add(saleAttrValueRelationInfo);

                    skuMainInfo.setSaleAttrValues(saleAttrValues);
                    skuMainInfo.setSkuPrice((goodsPrice.add(item.getAddPrice())).multiply(BigDecimal.valueOf(100))); // 商品初始化价格（单位：分），该价格会同步到所有门店 非必要不传价（新增时必填，需修改时非必填skuPrice字段请单独填加配置才可生效）
                    skuMainInfo.setWeight(BigDecimal.valueOf(0.5)); // 重量（单位：千克）【新增时必填，修改时非必填]
                    skuMainInfoList.add(skuMainInfo);

                    saleAttrValueNameList.add(item.getName());

                    // OSpecDetail oSpecDetail = new OSpecDetail();
                    // oSpecDetail.setSpecValue(item.getName());
                    // oSpecDetail.setSelectedStatus(0);
                    // specDetails.add(oSpecDetail);
                    // // 随机生成数字字符串
                    // Random random = new Random();
                    // int length = 10; // 生成的数字字符串长度
                    // StringBuilder sb = new StringBuilder(length);
                    // for (int i = 0; i < length; i++) {
                    //     sb.append(random.nextInt(10)); // 生成0到9之间的随机数字并添加到StringBuilder中
                    // }
                    // String randomNumberString = sb.toString();
                    // String randomNumberString2 = "X" + String.valueOf(new Date().getTime());
                    // OSpec oSpec = new OSpec();
                    // // oSpec.setSpecId(72970000222L); // 创建不填写
                    // oSpec.setName(item.getName());
                    // oSpec.setPrice(goodsPrice + item.getAddPrice().doubleValue());
                    // oSpec.setPriceType("SUB_PRICE");
                    // oSpec.setPackageType("GROUP_CHOOSE");
                    // oSpec.setStock(900); // 库存量
                    // oSpec.setMaxStock(1000); // 最大库存量
                    // oSpec.setStockStatus(1); // 是否次日自动置满库存 0:不自动置满 或者 1: 次日自动置满
                    // oSpec.setPackingFee(0); // 打包费
                    // oSpec.setOnShelf(1); // 是否上架，1:上架，0:下架，不填写默认0
                    // oSpec.setExtendCode(randomNumberString); // 商品扩展码
                    // oSpec.setBarCode(randomNumberString2); // 商品条形码
                    // oSpec.setActivityLevel(0); // 商品活动信息，1:有活动，0:无活动
                    // OSpecAttribute oSpecAttribute = new OSpecAttribute(); // 规格扩展信息
                    // oSpecAttribute.setUnit("杯");
                    // oSpecAttribute.setWeight("1");
                    // oSpecAttribute.setPackageShare(1);
                    // oSpec.setSpecAttribute(oSpecAttribute); // 规格扩展信息
                    // if (count >= 0) {
                    //     List<OItemAttribute> attributes = new ArrayList<>();
                    //     if (null != sugarList && sugarList.size() > 0) {
                    //         List<String> names = sugarList.stream().map(ee -> ee.getName()).collect(Collectors.toList());
                    //         Long paramCateId = sugarList.get(0).getParamCateId();
                    //         ParamCate paramCate = paramCateService.getById(paramCateId);
                    //
                    //         OItemAttribute oItemAttribute = new OItemAttribute(); // 规格维度商品属性
                    //         oItemAttribute.setName(paramCate.getName());
                    //         oItemAttribute.setDetails(names);
                    //         attributes.add(oItemAttribute);
                    //         oSpec.setAttributes(attributes); // 规格维度商品属性
                    //     }
                    //     if (null != temperatureList && temperatureList.size() > 0) {
                    //         List<String> names = temperatureList.stream().map(ee -> ee.getName()).collect(Collectors.toList());
                    //         Long paramCateId = temperatureList.get(0).getParamCateId();
                    //         ParamCate paramCate = paramCateService.getById(paramCateId);
                    //
                    //         OItemAttribute oItemAttribute = new OItemAttribute(); // 规格维度商品属性
                    //         oItemAttribute.setName(paramCate.getName());
                    //         oItemAttribute.setDetails(names);
                    //         attributes.add(oItemAttribute);
                    //         oSpec.setAttributes(attributes); // 规格维度商品属性
                    //     }
                    //
                    // }
                    // OSpecProperty oSpecProperty = new OSpecProperty();
                    // oSpecProperty.setSpecGroupName("规格");
                    // oSpecProperty.setLinkPhotoStatus(0);
                    // oSpecProperty.setSpecDetails(specDetails);
                    // specProperties.add(oSpecProperty);
                    // oSpecs.add(oSpec);

                });
                saleAttrRelationInfo.setAttrValues(saleAttrValueNameList);
                saleAttrRelationInfoList.add(saleAttrRelationInfo);
                spuRequest.setSaleAttrRelationInfoList(saleAttrRelationInfoList);
                spuRequest.setSkuMainInfoList(skuMainInfoList);
                if(outSkuIdList.size() > 0){
                    String join = String.join(",", outSkuIdList);
                    obj.setOutSkuId(join);
                }


            }

            List<CustomAttrInfo> customAttrList = new ArrayList<>(); // 普通销售属性List，该属性不参与sku创建，最多支持6组属性 清空时传空集合，不改传null（自定义属性）


            // 获取除小料外的规格参数
            if (null != sugarList && sugarList.size() > 0) {
                CustomAttrInfo customAttrInfo = new CustomAttrInfo();
                customAttrInfo.setCustomAttrName("糖度");
                List<String> customAttrValueNameList = new ArrayList<>();
                sugarList.stream().forEach(item -> {
                    // OSpecDetail oSpecDetail = new OSpecDetail();
                    // oSpecDetail.setSpecValue(item.getName());
                    // oSpecDetail.setSelectedStatus(0);
                    // specDetails.add(oSpecDetail);
                    customAttrValueNameList.add(item.getName());

                });
                // OSpecProperty oSpecProperty = new OSpecProperty();
                // oSpecProperty.setSpecGroupName("糖度");
                // oSpecProperty.setLinkPhotoStatus(0);
                // oSpecProperty.setSpecDetails(specDetails);
                // specProperties.add(oSpecProperty);
                customAttrInfo.setCustomAttrValueNameList(customAttrValueNameList);
                customAttrList.add(customAttrInfo);
                spuRequest.setCustomAttrList(customAttrList);
            }
            if (null != temperatureList && temperatureList.size() > 0) {
                CustomAttrInfo customAttrInfo = new CustomAttrInfo();
                customAttrInfo.setCustomAttrName("温度");
                List<String> customAttrValueNameList = new ArrayList<>();
                temperatureList.stream().forEach(item -> {
                    // OSpecDetail oSpecDetail = new OSpecDetail();
                    // oSpecDetail.setSpecValue(item.getName());
                    // oSpecDetail.setSelectedStatus(0);
                    // specDetails.add(oSpecDetail);
                    customAttrValueNameList.add(item.getName());

                });
                // OSpecProperty oSpecProperty = new OSpecProperty();
                // oSpecProperty.setSpecGroupName("温度");
                // oSpecProperty.setLinkPhotoStatus(0);
                // oSpecProperty.setSpecDetails(specDetails);
                // specProperties.add(oSpecProperty);
                customAttrInfo.setCustomAttrValueNameList(customAttrValueNameList);
                customAttrList.add(customAttrInfo);
                spuRequest.setCustomAttrList(customAttrList);
            }


            // List<OMaterial> oMaterials = new ArrayList<>();
            // // 查询配料
            // LambdaQueryWrapper<GoodsAllBatch> wrapper3 = new LambdaQueryWrapper<>();
            // wrapper3.eq(GoodsAllBatch::getStatus, 1).eq(GoodsAllBatch::getGoodsId, obj.getId());
            // List<GoodsAllBatch> allBatchList = goodsAllBatchService.list(wrapper3);
            // if (null != allBatchList && allBatchList.size() > 0) {
            //     List<Long> batchIds = allBatchList.stream().map(rr -> rr.getBatchId()).collect(Collectors.toList());
            //     LambdaQueryWrapper<Batching> query3 = new LambdaQueryWrapper<>();
            //     query3.eq(Batching::getStatus, 1).in(Batching::getId, batchIds);
            //     List<Batching> batchingList = batchingService.list(query3);
            //     if (null != batchingList && batchingList.size() > 0) {
            //         batchingList.stream().forEach(tt -> {
            //             OMaterial oMaterial = new OMaterial();
            //             oMaterial.setName(tt.getName());
            //             oMaterials.add(oMaterial);
            //         });
            //     }
            // }

            // properties.put(OItemCreateProperty.mainMaterials, oMaterials); // 原材料
            // properties.put(OItemCreateProperty.specs, oSpecs); // 规格
            String spuRequestJson = jsonConverter.toSaveSpuRequestJson(spuRequest);
            String response = saveSpu(spuRequestJson);
            System.out.println("API Response: " + response);
            JSONObject json = JSON.parseObject(response);
            JSONObject data = json.getJSONObject("data");
            Integer code = json.getInteger("code");

            // properties.put(OItemCreateProperty.stdCategoryId, categorize.getEleId()); // 类目id
            //
            // OItem item = productService.createItem(categorize.getEleId(), properties);
            if (code == 0 && data != null) {
                BeanUtils.copyProperties(obj, shopToGoods);
                shopToGoods.setCreateTime(LocalDateTime.now());
                shopToGoods.setUpdateTime(LocalDateTime.now());
                shopToGoods.setUrl(imagePath);
                boolean save = shopToGoodsService.save(shopToGoods);
                if (save) {
                    // 添加配料
                    final Integer[] count2 = {0};
                    LambdaQueryWrapper<GoodsAllBatch> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(GoodsAllBatch::getStatus, 1).eq(GoodsAllBatch::getGoodsId, obj.getGoodsId());
                    List<GoodsAllBatch> allBatches = goodsAllBatchService.list(queryWrapper);
                    if (null != allBatches && allBatches.size() > 0) {
                        allBatches.stream().forEach(aa -> {
                            GoodsToBatch goodsToBatch = new GoodsToBatch();
                            goodsToBatch.setBatchId(aa.getBatchId());
                            goodsToBatch.setUseNumber(aa.getUseNumber());
                            goodsToBatch.setGoodsId(shopToGoods.getId());
                            goodsToBatch.setTemperatureId(aa.getTemperatureId());
                            goodsToBatch.setSugarId(aa.getSugarId());
                            goodsToBatch.setSizeId(aa.getSizeId());
                            goodsToBatch.setCreateTime(LocalDateTime.now());
                            goodsToBatch.setUpdateTime(LocalDateTime.now());
                            goodsToBatch.setShopId(obj.getShopId());
                            goodsToBatch.setPrice(shopToGoods.getPrice());
                            goodsToBatch.setCost(shopToGoods.getCost());
                            boolean b = goodsToBatchService.save(goodsToBatch);
                            if (b) {
                                count2[0] = count2[0] + 1;
                            }
                        });
                    }

                    if (count2[0] == allBatches.size()) {

                        InterfaceLog interfaceLog = new InterfaceLog();
                        interfaceLog.setTitle("添加京东秒送商品");
                        interfaceLog.setMethodName("createGoods");
                        String content = "商户"+shop.getName()+"添加京东秒送商品，商品名为"+obj.getName();
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

                } else {

                    return R.error();
                }
            } else {
                return R.error();
            }


        }

    }


    /**
     * 新增、修改SPU信息接口
     */
    public static String saveSpu(String paramJson) throws Exception {
        // // 1. 准备系统参数
        // Map<String, String> systemParams = new HashMap<>();
        // systemParams.put("app_key", key);
        // systemParams.put("token", ACCESS_TOKEN);
        // systemParams.put("timestamp", getCurrentTime());
        // systemParams.put("format", "json");
        // systemParams.put("v", "1.0");
        //
        // String API_URL = "https://openapi.jddj.com/djapi/PmsLightFoodSpuService/saveSpu";
        // // 2. 合并所有参数
        // Map<String, String> allParams = new HashMap<>();
        // allParams.putAll(systemParams);
        // allParams.putAll(businessParams);
        //
        // // 3. 生成签名
        // String sign = generateSign(allParams, secret);
        // allParams.put("sign", sign);
        //
        // // 4. 发送POST请求
        // return sendPostRequest(API_URL, allParams);
        //
        // String paramJson = new ObjectMapper().writeValueAsString(businessParams);

        // 调用API
        return executeApi("/djapi/pms/delShopCategory", paramJson);
    }

    @SneakyThrows
    @ApiOperation(value = "修改京东秒送的店铺商品")
    @PostMapping("updateGoods")
    public R updateGoods(@RequestBody ShopToGoods obj) {
        Shop shop = shopsService.getById(obj.getShopId());
        ShopToGoods shopToGoods = new ShopToGoods();
        LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getGoodsId, obj.getGoodsId()).eq(ShopToGoods::getShopId, obj.getShopId()).ne(ShopToGoods::getId, obj.getId());
        List<ShopToGoods> list = shopToGoodsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该商品已添加");
        } else {
            // Token token = new Token();
            // token.setAccessToken(obj.getAccessToken());
            // token.setTokenType(obj.getTokenType());
            // token.setExpires(obj.getExpiresIn());
            // token.setRefreshToken(obj.getRefreshToken());

            // 获取商品基本价格
            // double goodsPrice = obj.getPrice().doubleValue();
            BigDecimal goodsPrice = BigDecimal.ZERO;
            Long cateId = obj.getCateId();
            Categorize categorize = categorizeService.getById(cateId);
            // ProductService productService = new ProductService(config, token);
            // Map<OItemUpdateProperty, Object> properties = new HashMap<OItemUpdateProperty, Object>();
            // properties.put(OItemUpdateProperty.name, obj.getName());
            // properties.put(OItemUpdateProperty.description, obj.getIntroduce());
            SaveSpuRequest spuRequest = new SaveSpuRequest();
            spuRequest.setOutSpuId(obj.getJdId());
            spuRequest.setSpuName(obj.getName());
            spuRequest.setShopCategories(Collections.singletonList(categorize.getJdId()));
            spuRequest.setCategoryId(categorize.getJdId());
            spuRequest.setBrandId(shop.getBrandId());
            spuRequest.setFixedStatus(1);
            spuRequest.setPackageFee(BigDecimal.ZERO); // 餐盒费


            // 上传图片
            List<String> images = new ArrayList<>();
            String imagePath = weChatProperties.getUploadPath() + obj.getUrl();
            // byte[] fileContent = Files.readAllBytes(new File(imagePath).toPath());
            // String base64String = Base64.getEncoder().encodeToString(fileContent);
            // String imageHash = productService.uploadImage(base64String);
            images.add(imagePath);
            spuRequest.setImages(images);
            spuRequest.setProductDesc(obj.getIntroduce());
            spuRequest.setIfViewDesc(0);
            // // 上传图片获取hash值
            // String imagePath = weChatProperties.getUploadPath() + obj.getUrl();
            // byte[] fileContent = Files.readAllBytes(new File(imagePath).toPath());
            // String base64String = Base64.getEncoder().encodeToString(fileContent);
            // String imageHash = productService.uploadImage(base64String);
            // properties.put(OItemUpdateProperty.imageHash, imageHash);


            // 获取规格参数
            LambdaQueryWrapper<Param> queryParam = new LambdaQueryWrapper<Param>();
            queryParam.eq(Param::getStatus, 1).eq(Param::getType, 1);
            List<Param> paramList = paramService.list(queryParam);

            LambdaQueryWrapper<Param> queryParam2 = new LambdaQueryWrapper<Param>();
            queryParam2.eq(Param::getStatus, 1).eq(Param::getType, 2);
            List<Param> sugarList = paramService.list(queryParam2);

            LambdaQueryWrapper<Param> queryParam3 = new LambdaQueryWrapper<Param>();
            queryParam3.eq(Param::getStatus, 1).eq(Param::getType, 3);
            List<Param> temperatureList = paramService.list(queryParam3);

            Integer count = (null != sugarList && sugarList.size() > 0 ? 1 : 0) + (null != temperatureList && temperatureList.size() > 0 ? 1 : 0);

            // List<OSpecProperty> specProperties = new ArrayList<>();
            // List<OSpecDetail> specDetails = new ArrayList<>();
            // List<OSpec> oSpecs = new ArrayList<>();

            List<SaleAttrRelationInfo> saleAttrRelationInfoList = new ArrayList<>(); // 决定价格库存的销售属性List，最多支持5组属性
            List<LightFoodSkuInfoRequest> skuMainInfoList = new ArrayList<>(); // saleAttrRelationInfoList传值时spu下所有sku信息全部必填

            if (null != paramList && paramList.size() > 0) {
                SaleAttrRelationInfo saleAttrRelationInfo = new SaleAttrRelationInfo();
                saleAttrRelationInfo.setAttrName("规格");
                List<String> saleAttrValueNameList = new ArrayList<>();


                List<String> outSkuIdList = new ArrayList<>();
                List<SaleAttrValueRelationInfo> saleAttrValues = new ArrayList<>(); // 需要和SPU的销售属性的顺序保持一致。【新增时必填；销售属性信息有变化时必填]
                int ii = 0;
                List<String> stringList = new ArrayList<>();
                if(null != obj.getOutSkuId() && obj.getOutSkuId().length() > 0){
                    stringList = Arrays.asList(obj.getOutSkuId().split(","));
                }
                List<String> finalStringList = stringList;
                paramList.stream().forEach(item -> {
                    LightFoodSkuInfoRequest skuMainInfo = new LightFoodSkuInfoRequest();
                    if (finalStringList.size() > 0 && ii <= finalStringList.size() - 1){
                        skuMainInfo.setOutSkuId(finalStringList.get(ii));
                        outSkuIdList.add(finalStringList.get(ii));
                    }else{
                        long timestamp2 = Instant.now().toEpochMilli();
                        String outSkuId = shop.getJdShopId() + "" + timestamp2;
                        skuMainInfo.setOutSkuId(outSkuId); // 外部商品编码如需修改outskuid，需要传saleAttrRelationInfoList。【新增、修改sku信息时必填】
                        outSkuIdList.add(outSkuId);
                    }


                    skuMainInfo.setFixedStatus(1); // sku上下架状态 1上架、2下架、4删除 上架的商品不能直接删除，需要先设置为下架，然后再删除。【新增时必填，修改时非必填】

                    SaleAttrValueRelationInfo saleAttrValueRelationInfo = new SaleAttrValueRelationInfo();
                    saleAttrValueRelationInfo.setSaleAttrName("规格");
                    saleAttrValueRelationInfo.setSaleAttrValueName(item.getName());
                    saleAttrValues.add(saleAttrValueRelationInfo);

                    skuMainInfo.setSaleAttrValues(saleAttrValues);
                    skuMainInfo.setSkuPrice((goodsPrice.add(item.getAddPrice())).multiply(BigDecimal.valueOf(100))); // 商品初始化价格（单位：分），该价格会同步到所有门店 非必要不传价（新增时必填，需修改时非必填skuPrice字段请单独填加配置才可生效）
                    skuMainInfo.setWeight(BigDecimal.valueOf(0.5)); // 重量（单位：千克）【新增时必填，修改时非必填]
                    skuMainInfoList.add(skuMainInfo);

                    saleAttrValueNameList.add(item.getName());

                    // OSpecDetail oSpecDetail = new OSpecDetail();
                    // oSpecDetail.setSpecValue(item.getName());
                    // oSpecDetail.setSelectedStatus(0);
                    // specDetails.add(oSpecDetail);
                    // // 随机生成数字字符串
                    // Random random = new Random();
                    // int length = 10; // 生成的数字字符串长度
                    // StringBuilder sb = new StringBuilder(length);
                    // for (int i = 0; i < length; i++) {
                    //     sb.append(random.nextInt(10)); // 生成0到9之间的随机数字并添加到StringBuilder中
                    // }
                    // String randomNumberString = sb.toString();
                    // String randomNumberString2 = "X" + String.valueOf(new Date().getTime());
                    // OSpec oSpec = new OSpec();
                    // // oSpec.setSpecId(72970000222L); // 创建不填写
                    // oSpec.setName(item.getName());
                    // oSpec.setPrice(goodsPrice + item.getAddPrice().doubleValue());
                    // oSpec.setPriceType("SUB_PRICE");
                    // oSpec.setPackageType("GROUP_CHOOSE");
                    // oSpec.setStock(900); // 库存量
                    // oSpec.setMaxStock(1000); // 最大库存量
                    // oSpec.setStockStatus(1); // 是否次日自动置满库存 0:不自动置满 或者 1: 次日自动置满
                    // oSpec.setPackingFee(0); // 打包费
                    // oSpec.setOnShelf(1); // 是否上架，1:上架，0:下架，不填写默认0
                    // oSpec.setExtendCode(randomNumberString); // 商品扩展码
                    // oSpec.setBarCode(randomNumberString2); // 商品条形码
                    // oSpec.setActivityLevel(0); // 商品活动信息，1:有活动，0:无活动
                    // OSpecAttribute oSpecAttribute = new OSpecAttribute(); // 规格扩展信息
                    // oSpecAttribute.setUnit("杯");
                    // oSpecAttribute.setWeight("1");
                    // oSpecAttribute.setPackageShare(1);
                    // oSpec.setSpecAttribute(oSpecAttribute); // 规格扩展信息
                    // if (count >= 0) {
                    //     List<OItemAttribute> attributes = new ArrayList<>();
                    //     if (null != sugarList && sugarList.size() > 0) {
                    //         List<String> names = sugarList.stream().map(ee -> ee.getName()).collect(Collectors.toList());
                    //         Long paramCateId = sugarList.get(0).getParamCateId();
                    //         ParamCate paramCate = paramCateService.getById(paramCateId);
                    //
                    //         OItemAttribute oItemAttribute = new OItemAttribute(); // 规格维度商品属性
                    //         oItemAttribute.setName(paramCate.getName());
                    //         oItemAttribute.setDetails(names);
                    //         attributes.add(oItemAttribute);
                    //         oSpec.setAttributes(attributes); // 规格维度商品属性
                    //     }
                    //     if (null != temperatureList && temperatureList.size() > 0) {
                    //         List<String> names = temperatureList.stream().map(ee -> ee.getName()).collect(Collectors.toList());
                    //         Long paramCateId = temperatureList.get(0).getParamCateId();
                    //         ParamCate paramCate = paramCateService.getById(paramCateId);
                    //
                    //         OItemAttribute oItemAttribute = new OItemAttribute(); // 规格维度商品属性
                    //         oItemAttribute.setName(paramCate.getName());
                    //         oItemAttribute.setDetails(names);
                    //         attributes.add(oItemAttribute);
                    //         oSpec.setAttributes(attributes); // 规格维度商品属性
                    //     }
                    //
                    // }
                    // OSpecProperty oSpecProperty = new OSpecProperty();
                    // oSpecProperty.setSpecGroupName("规格");
                    // oSpecProperty.setLinkPhotoStatus(0);
                    // oSpecProperty.setSpecDetails(specDetails);
                    // specProperties.add(oSpecProperty);
                    // oSpecs.add(oSpec);

                });

                saleAttrRelationInfo.setAttrValues(saleAttrValueNameList);
                saleAttrRelationInfoList.add(saleAttrRelationInfo);
                spuRequest.setSaleAttrRelationInfoList(saleAttrRelationInfoList);
                spuRequest.setSkuMainInfoList(skuMainInfoList);
                if(outSkuIdList.size() > 0){
                    String join = String.join(",", outSkuIdList);
                    obj.setOutSkuId(join);
                }
            }

            List<CustomAttrInfo> customAttrList = new ArrayList<>(); // 普通销售属性List，该属性不参与sku创建，最多支持6组属性 清空时传空集合，不改传null（自定义属性）
            // 获取除小料外的规格参数
            if (null != sugarList && sugarList.size() > 0) {
                CustomAttrInfo customAttrInfo = new CustomAttrInfo();
                customAttrInfo.setCustomAttrName("糖度");
                List<String> customAttrValueNameList = new ArrayList<>();
                sugarList.stream().forEach(item -> {
                    // OSpecDetail oSpecDetail = new OSpecDetail();
                    // oSpecDetail.setSpecValue(item.getName());
                    // oSpecDetail.setSelectedStatus(0);
                    // specDetails.add(oSpecDetail);
                    customAttrValueNameList.add(item.getName());

                });
                // OSpecProperty oSpecProperty = new OSpecProperty();
                // oSpecProperty.setSpecGroupName("糖度");
                // oSpecProperty.setLinkPhotoStatus(0);
                // oSpecProperty.setSpecDetails(specDetails);
                // specProperties.add(oSpecProperty);
                customAttrInfo.setCustomAttrValueNameList(customAttrValueNameList);
                customAttrList.add(customAttrInfo);
                spuRequest.setCustomAttrList(customAttrList);
            }
            if (null != temperatureList && temperatureList.size() > 0) {
                CustomAttrInfo customAttrInfo = new CustomAttrInfo();
                customAttrInfo.setCustomAttrName("温度");
                List<String> customAttrValueNameList = new ArrayList<>();
                temperatureList.stream().forEach(item -> {
                    // OSpecDetail oSpecDetail = new OSpecDetail();
                    // oSpecDetail.setSpecValue(item.getName());
                    // oSpecDetail.setSelectedStatus(0);
                    // specDetails.add(oSpecDetail);
                    customAttrValueNameList.add(item.getName());

                });
                // OSpecProperty oSpecProperty = new OSpecProperty();
                // oSpecProperty.setSpecGroupName("温度");
                // oSpecProperty.setLinkPhotoStatus(0);
                // oSpecProperty.setSpecDetails(specDetails);
                // specProperties.add(oSpecProperty);
                customAttrInfo.setCustomAttrValueNameList(customAttrValueNameList);
                customAttrList.add(customAttrInfo);
                spuRequest.setCustomAttrList(customAttrList);

            }
            List<OMaterial> oMaterials = new ArrayList<>();
            // 查询配料
            LambdaQueryWrapper<GoodsAllBatch> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.eq(GoodsAllBatch::getStatus, 1).eq(GoodsAllBatch::getGoodsId, obj.getId());
            List<GoodsAllBatch> allBatchList = goodsAllBatchService.list(wrapper3);
            if (null != allBatchList && allBatchList.size() > 0) {
                List<Long> batchIds = allBatchList.stream().map(rr -> rr.getBatchId()).collect(Collectors.toList());
                LambdaQueryWrapper<Batching> query3 = new LambdaQueryWrapper<>();
                query3.eq(Batching::getStatus, 1).in(Batching::getId, batchIds);
                List<Batching> batchingList = batchingService.list(query3);
                if (null != batchingList && batchingList.size() > 0) {
                    batchingList.stream().forEach(tt -> {
                        OMaterial oMaterial = new OMaterial();
                        oMaterial.setName(tt.getName());
                        oMaterials.add(oMaterial);
                    });
                }
            }

            // properties.put(OItemUpdateProperty.mainMaterials, oMaterials); // 原材料
            // properties.put(OItemUpdateProperty.specs, oSpecs); // 规格
            // Long cateId = obj.getCateId();
            // Categorize categorize = categorizeService.getById(cateId);
            // properties.put(OItemUpdateProperty.stdCategoryId, categorize.getEleId()); // 类目id
            //
            // OItem item = productService.updateItem(obj.getEleId(), categorize.getEleId(), properties);
            String spuRequestJson = jsonConverter.toSaveSpuRequestJson(spuRequest);
            String response = saveSpu(spuRequestJson);
            System.out.println("API Response: " + response);
            JSONObject json = JSON.parseObject(response);
            JSONObject data = json.getJSONObject("data");
            Integer code = json.getInteger("code");
            if (code == 0 && data != null) {
                BeanUtils.copyProperties(obj, shopToGoods);
                shopToGoods.setUpdateTime(LocalDateTime.now());
                shopToGoods.setUrl(imagePath);
                boolean save = shopToGoodsService.updateById(shopToGoods);
                if (save) {
                    final Integer[] count2 = {0};
                    LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, shopToGoods.getGoodsId()).eq(GoodsToBatch::getShopId, shopToGoods.getShopId());
                    boolean remove = goodsToBatchService.remove(queryWrapper);

                    if (remove) {
                        LambdaQueryWrapper<GoodsAllBatch> queryWrapper2 = new LambdaQueryWrapper<>();
                        queryWrapper2.eq(GoodsAllBatch::getStatus, 1).eq(GoodsAllBatch::getGoodsId, obj.getGoodsId());
                        List<GoodsAllBatch> allBatches = goodsAllBatchService.list(queryWrapper2);
                        allBatches.stream().forEach(aa -> {
                            GoodsToBatch goodsToBatch = new GoodsToBatch();
                            goodsToBatch.setBatchId(aa.getBatchId());
                            goodsToBatch.setUseNumber(aa.getUseNumber());
                            goodsToBatch.setGoodsId(shopToGoods.getId());
                            goodsToBatch.setTemperatureId(aa.getTemperatureId());
                            goodsToBatch.setSugarId(aa.getSugarId());
                            goodsToBatch.setSizeId(aa.getSizeId());
                            goodsToBatch.setCreateTime(LocalDateTime.now());
                            goodsToBatch.setUpdateTime(LocalDateTime.now());
                            goodsToBatch.setShopId(obj.getShopId());
                            goodsToBatch.setPrice(shopToGoods.getPrice());
                            goodsToBatch.setCost(shopToGoods.getCost());
                            boolean b = goodsToBatchService.save(goodsToBatch);
                            if (b) {
                                count2[0] = count2[0] + 1;
                            }
                        });
                        // if (count2[0] == allBatches.size()) {
                        //     return R.ok();
                        // }else {
                        //     return R.error();
                        // }

                    }

                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("修改饿了么商品");
                    interfaceLog.setMethodName("updateGoods");
                    String content = "商户"+shop.getName()+"修改饿了么商品，商品名为"+obj.getName();
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
            } else {
                return R.error();
            }


        }

    }

    @SneakyThrows
    @ApiOperation(value = "删除饿了么的店铺商品")
    @DeleteMapping("deleteEleGoods/{id}")
    public R invalidGoods(@PathVariable("id") Long id, @RequestParam("access_token") String access_token, @RequestParam("token_type") String token_type,
                          @RequestParam("expires_in") String expires_in, @RequestParam("refresh_token") String refresh_token) {
        ShopToGoods byId = shopToGoodsService.getById(id);
        if (null != byId.getEleId()) {
            Token token = new Token();
            token.setAccessToken(access_token);
            token.setTokenType(token_type);
            token.setExpires(Long.valueOf(expires_in));
            token.setRefreshToken(refresh_token);

            // 删除饿了么店铺商品
            ProductService productService = new ProductService(config, token);
            productService.invalidItem(byId.getEleId());
        }


        // 删除库里商品

        boolean remove = shopToGoodsService.removeById(id);
        if (remove) {
            Shop shop = shopsService.getById(byId.getShopId());
            InterfaceLog interfaceLog = new InterfaceLog();
            interfaceLog.setTitle("删除饿了么商品");
            interfaceLog.setMethodName("deleteEleGoods");
            String content = "商户"+shop.getName()+"删除饿了么商品，商品名为"+byId.getName();
            interfaceLog.setContent(content);
            interfaceLog.setShopId(byId.getShopId());
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
