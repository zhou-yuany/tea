// package com.tea.server.controller;
//
// import com.alibaba.fastjson.JSONArray;
// import com.alibaba.fastjson.JSONObject;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.google.gson.Gson;
// import com.tea.server.config.WeChatProperties;
// import com.tea.server.entity.*;
// import com.tea.server.entity.ele.ElmOrder;
// import com.tea.server.entity.mt.*;
// import com.tea.server.service.*;
// import com.tea.server.socket.CodeData;
// import com.tea.server.socket.DeviceWebSocket;
// import com.tea.server.socket.PlatformWebSocket;
// import com.tea.server.utils.R;
// import eleme.openapi.sdk.api.entity.order.OGoodsGroup;
// import eleme.openapi.sdk.api.entity.order.OGoodsItem;
// import eleme.openapi.sdk.api.entity.order.OGroupItemAttribute;
// import eleme.openapi.sdk.api.entity.order.OGroupItemSpec;
// import eleme.openapi.sdk.api.entity.product.*;
// import eleme.openapi.sdk.api.entity.shop.OShop;
// import eleme.openapi.sdk.api.enumeration.product.OItemCreateProperty;
// import eleme.openapi.sdk.api.enumeration.product.OItemUpdateProperty;
// import eleme.openapi.sdk.api.service.ProductService;
// import eleme.openapi.sdk.api.service.ShopService;
// import eleme.openapi.sdk.config.Config;
// import eleme.openapi.sdk.oauth.OAuthClient;
// import eleme.openapi.sdk.oauth.response.Token;
// import eleme.openapi.sdk.utils.PropertiesUtils;
// import io.swagger.annotations.ApiOperation;
// import lombok.SneakyThrows;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang.StringUtils;
// import org.apache.http.client.methods.CloseableHttpResponse;
// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.entity.StringEntity;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.apache.http.impl.client.HttpClientBuilder;
// import org.apache.http.util.EntityUtils;
// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.util.ObjectUtils;
// import org.springframework.web.bind.annotation.*;
//
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.*;
// import java.math.BigDecimal;
// import java.math.RoundingMode;
// import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.*;
// import java.util.stream.Collectors;
//
// @Slf4j
// @RestController
// @RequestMapping("mt")
// public class MtController {
//     // 设置是否沙箱环境
//     private static final boolean isSandbox = true;
//
//     private static final String client_id = "";
//     // 设置APPKEY
//     private static final String key = "HTsjSlMBbm";
//     // 设置APPSECRET
//     private static final String client_secret = "";
//     // 初始化OAuthClient
//     private static OAuthClient client = null;
//     private static Map<String, String> tokenMap = new HashMap<String, String>();
//     private static Config config = null;
//
//     static {
//         // 初始化全局配置工具
//         config = new Config(isSandbox, key, client_secret);
//         client = new OAuthClient(config);
//     }
//
//     @Autowired
//     private ShopsService shopsService;
//
//     @Autowired
//     private CategorizeService categorizeService;
//
//     @Autowired
//     private CategorizeAllService categorizeAllService;
//
//     @Autowired
//     private ShopToGoodsService shopToGoodsService;
//
//     @Autowired
//     private ParamService paramService;
//
//     @Autowired
//     private ParamCateService paramCateService;
//
//     @Autowired
//     private GoodsToBatchService goodsToBatchService;
//
//     @Autowired
//     private GoodsAllBatchService goodsAllBatchService;
//
//     @Autowired
//     private BatchingService batchingService;
//
//     @Autowired
//     private WeChatProperties weChatProperties;
//
//     @Autowired
//     private OrdersService ordersService;
//
//     @Autowired
//     private OrderParamService orderParamService;
//
//     @Autowired
//     private RechargeRecordService rechargeRecordService;
//
//     @Autowired
//     private OrdersFlowService ordersFlowService;
//
//     @Autowired
//     private AgentsService agentsService;
//
//     @Autowired
//     private BatchUseService batchUseService;
//
//     @Autowired
//     private InterfaceLogService interfaceLogService;
//
//
//     /**
//      * 根据参数map生成签名
//      *
//      * @param paramMap 参数map
//      * @param secret   密钥，加在头部和尾部
//      * @return 生成后的签名
//      */
//     public static String generateSign(Map<String, String> paramMap, String secret) {
//         // 第一步：检查参数是否已经排序
//         String[] keys = paramMap.keySet().toArray(new String[0]);
//         Arrays.sort(keys);
//
//         // 第二步：把所有参数名和参数值串在一起
//         StringBuilder query = new StringBuilder();
//
//         for (String key : keys) {
//             String value = paramMap.get(key);
//             if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
//                 query.append(key).append(value);
//             }
//         }
//
//         query.append(secret);
//
//         // 第三步：生成签名
//         byte[] bytes = encryptSHA256(query.toString());
//         return byte2hex(bytes);
//     }
//
//     /**
//      * 对字符串进行sha256签名
//      *
//      * @param text 待加密的字符串
//      * @return 加密后的byte数组
//      */
//     private static byte[] encryptSHA256(String text) {
//         MessageDigest sha256;
//
//         try {
//             sha256 = MessageDigest.getInstance("SHA-256");
//         } catch (NoSuchAlgorithmException e) {
//             throw new IllegalArgumentException(e);
//         }
//
//         byte[] infoBytes = text.getBytes(StandardCharsets.UTF_8);
//         sha256.update(infoBytes);
//         return sha256.digest();
//     }
//
//     /**
//      * byte数组转化为hex值
//      *
//      * @param bytes byte数组
//      * @return hex值
//      */
//     private static String byte2hex(byte[] bytes) {
//         StringBuilder stringBuilder = new StringBuilder();
//
//         for (byte b : bytes) {
//             String hex = Integer.toHexString(b & 0xFF);
//
//             if (hex.length() == 1) {
//                 stringBuilder.append("0");
//             }
//
//             stringBuilder.append(hex);
//         }
//
//         return stringBuilder.toString();
//     }
//
//     /**
//      * 授权码(企业)模式获取Token  原
//      */
//     @ApiOperation(value = "授权码(企业)模式获取Token")
//     @GetMapping("callback")
//     private void serverTokenTest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//         String autoCode = request.getParameter("code");
//         log.info("************************************");
//         log.info("request:", request.toString());
//         log.info("request222222222:", request);
//         // 打印所有请求头
//         Enumeration<String> headerNames = request.getHeaderNames();
//         while (headerNames.hasMoreElements()) {
//             String headerName = headerNames.nextElement();
//             String headerValue = request.getHeader(headerName);
//             log.info(headerName + ": " + headerValue);
//         }
//
//         // 打印所有请求参数
//         request.getParameterMap().forEach((key, values) -> {
//             log.info(key + ": ");
//             if (values != null) {
//                 log.info(String.join(", ", values));
//             } else {
//                 log.info("null");
//             }
//         });
//         // String state = mapParams.get("state");
//
//
//         String redirect_uri = "https://tea.yinghai-tec.com/tea-management/mt/callback";
//         // String redirect_uri = request.getParameter("redirect_uri");
//         Token token = client.getTokenByCode(autoCode, redirect_uri);
//         if (token.isSuccess()) {
//             // 设置要跳转到的URL地址
//             String redirectUrl = "https://tea.yinghai-tec.com/shop?access_token=" + token.getAccessToken() + "&openid=" + token.getTokenType() + "&expires_in=" + token.getExpires() + "&refresh_token=" + token.getRefreshToken();
//             // 发送302状态码并指定新的URL地址
//             response.sendRedirect(redirectUrl);
//
//         } else {
//             System.out.println("error_code: " + token.getError());
//             System.out.println("error_desc: " + token.getError_description());
//         }
//     }
//
//     @ApiOperation("推送订单回调get(美团测试接口是否可用)")
//     @GetMapping("push")
//     public JSONObject elmTestGet() {
//         JSONObject jsonObject = new JSONObject();
//
//         jsonObject.put("message", "ok");//必须返回
//         return jsonObject;
//     }
//
//     @ApiOperation(value = "授权码(企业)模式获取Token  原")
//     @PostMapping("getTokenInfo")
//     private R getTokenInfo(@RequestBody MtOrder mtOrder) throws IOException, ServletException {
//         String mToekn = null;
//         // 请求body参数
//         Gson gson = new Gson();
//
//         HttpPost httpPost = new HttpPost("https://openapi.meituan.com/oauth/token");
//         HashMap<Object, Object> paramsMap = new HashMap<>();
//         paramsMap.put("client_id", client_id);
//         paramsMap.put("client_secret", client_secret);
//         paramsMap.put("redirect_uri", "https://tea.yinghai-tec.com/tea-management/mt/callback");
//         paramsMap.put("grant_type", "authorization_code");
//         paramsMap.put("code", mtOrder.getCode());
//         //将参数转化未json字符串
//         String jsonParamsMap = gson.toJson(paramsMap);
//         log.info("请求参数：" + jsonParamsMap);
//
//         StringEntity entity = new StringEntity(jsonParamsMap, "utf-8");
//         entity.setContentType("application/json");
//         httpPost.setEntity(entity);
//         httpPost.setHeader("Accept", "application/json");
//
//         //完成签名并执行请求
//         CloseableHttpResponse resp = null;
//         try {
//             log.info("eeeeeeeeeeeeeee*************:"+httpPost);
//
//             CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//             resp = httpClient.execute(httpPost);
//             int statusCode = resp.getStatusLine().getStatusCode();
//             String bodyAsString = EntityUtils.toString(resp.getEntity());
//             log.info("33333333333***************************************："+resp);
//             log.info("statusCode***************************************："+statusCode);
//             if (statusCode == 201) { //处理成功
//                 log.info("成功，返回结果 = " + bodyAsString);
//                 //相应结果
//                 HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
//                 //获取机器的token
//                 mToekn = resultMap.get("token");
//
//
//             }
//             resp.close();
//
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//
//         return R.ok();
//     }
//
//     /**
//      * 美团 消息推送
//      */
//     @ApiOperation(value = "消息推送")
//     @PostMapping("push")
//     private R push(@RequestBody ElmOrder elemReqDto) throws IOException, ServletException {
//         //这里也可以用Map<String,Object> 先接收看看参数
//         if (null != elemReqDto) {
//             // log.info("当前推送订单状态："+elemReqDto.getType());
//             // log.warn("当前推送订单状态："+elemReqDto.getType());
//             String message = elemReqDto.getMessage();
//             JSONObject orderJson = JSONObject.parseObject(message);
//             String orderId = orderJson.getString("orderId");
//             // log.info("orderId："+orderId);
//             log.info("orderJson：" + orderJson);
//             // log.info("elemReqDto.getType()："+elemReqDto.getType());
//             // log.warn("orderId："+orderId);
//             // log.warn("orderJson："+orderJson);
//             // log.warn("elemReqDto.getType()："+elemReqDto.getType());
//             if (elemReqDto.getType() == 10) {
//                 //订单生效（用户付款即生成）
//
//                 LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
//                 wrapper.eq(Orders :: getStatus, 1).eq(Orders :: getOrderNum, orderId);
//                 Orders ordersServiceOne = ordersService.getOne(wrapper);
//                 if (null == ordersServiceOne) {
//
//                     Orders orders = new Orders();
//                     String description = orderJson.getString("description");
//                     orders.setNotes(description);
//                     // 转list
//                     JSONArray groups = orderJson.getJSONArray("groups");
//                     Integer totalCount = 0;
//                     List<OGoodsGroup> oGoodsGroups = JSONArray.parseArray(groups.toJSONString(), OGoodsGroup.class);
//                     if (null != oGoodsGroups && oGoodsGroups.size() > 0) {
//                         for (int i = 0; i < oGoodsGroups.size(); i++) {
//                             OGoodsGroup oGoodsGroup = oGoodsGroups.get(i);
//                             List<OGoodsItem> items = oGoodsGroup.getItems();
//                             if (null != items && items.size() > 0) {
//                                 for (int j = 0; j < items.size(); j++) {
//                                     totalCount += items.get(j).getQuantity();
//                                 }
//                             }
//                         }
//                     }
//                     Long shopId = orderJson.getLong("shopId");
//                     log.info("******************************shopId:" + shopId);
//                     LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
//                     wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
//                     Shop shop = shopsService.getOne(wrapper2);
//                     log.info("******************************shop:" + shop);
//                     orders.setTotalCount(totalCount);
//                     orders.setOrderNum(orderId);
//                     LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
//                     LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);
//
//                     Integer no = 0;
//
//                     if (null != shop) {
//                         LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//                         queryWrapper.eq(Orders::getStatus, 1).eq(Orders::getShopId, shop.getId()).gt(Orders::getCreateTime, startTime).lt(Orders::getCreateTime, endTime).orderByDesc(Orders::getId).last("limit 1");
//                         Orders obj = ordersService.getOne(queryWrapper);
//                         if (!ObjectUtils.isEmpty(obj)) {
//                             no = obj.getPickupCode() + 1;
//                         }
//                         orders.setShopId(shop.getId());
//                     }
//
//                     // Integer daySn = orderJson.getInteger("daySn"); //店铺当日订单流水号
//                     // orders.setPickupCode(daySn);
//                     orders.setPickupCode(no);
//                     String createdAt = orderJson.getString("createdAt");
//                     DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//                     LocalDateTime dateTime = LocalDateTime.parse(createdAt, formatter);
//                     orders.setCreateTime(dateTime);
//                     orders.setUpdateTime(dateTime);
//                     // Integer orderBusinessType = orderJson.getInteger("orderBusinessType");
//                     orders.setOrderType(1);
//                     orders.setSerialNum(orderId);
//                     orders.setPayStatus(2);
//
//                     orders.setAdminId(shop.getAdminId());
//
//                     JSONArray phoneList = orderJson.getJSONArray("phoneList");
//                     List<String> stringList = JSONArray.parseArray(phoneList.toJSONString(), String.class);
//                     orders.setPhone(stringList.get(0));
//                     orders.setPlatformType(2);
//                     orders.setPayType(1);
//                     Double totalAmount = orderJson.getDouble("totalAmount");
//                     orders.setTotalPrice(BigDecimal.valueOf(totalAmount));
//                     orders.setIsCall(2);
//                     orders.setIsTake(2);
//                     orders.setEleId(shopId);
//                     // orders.setOpenid();
//                     ordersService.save(orders);
//
//                     InterfaceLog interfaceLog = new InterfaceLog();
//                     interfaceLog.setTitle("美团订单支付成功");
//                     interfaceLog.setMethodName("updateOrders");
//                     String content = "用户订单支付成功，订单编号为"+orders.getOrderNum()+"，订单金额为："+BigDecimal.valueOf(totalAmount);
//                     interfaceLog.setContent(content);
//                     interfaceLog.setShopId(orders.getShopId());
//                     interfaceLog.setTypeStatus(0);
//                     interfaceLog.setCreateTime(LocalDateTime.now());
//                     interfaceLog.setUpdateTime(LocalDateTime.now());
//                     interfaceLogService.save(interfaceLog);
//
//                     // 新增分销账单
//                     List<Long> ids = new ArrayList<>();
//                     ids.add(orders.getShopId());
//                     LambdaQueryWrapper<Agents> wrapper10 = new LambdaQueryWrapper<>();
//                     wrapper10.eq(Agents::getStatus, 1).in(Agents::getShopId, ids).eq(Agents::getIsUse, 1);
//                     Agents agentsServiceOne = agentsService.getOne(wrapper10);
//
//                     OrdersFlow ordersFlow = new OrdersFlow();
//                     ordersFlow.setAdminId(orders.getAdminId());
//                     ordersFlow.setShopId(orders.getShopId());
//                     ordersFlow.setOrderId(orders.getId());
//
//                     ordersFlow.setPrice(orders.getTotalPrice());
//                     ordersFlow.setPayType(1);
//
//                     ordersFlow.setPayPlatform(1);
//                     ordersFlow.setNumber(orders.getTotalCount());
//                     ordersFlow.setSerialNum(orders.getOrderNum());
//                     // String goodsName = list.stream().map(item -> item.getName()).collect(Collectors.joining(","));
//                     // ordersFlow.setGoodsName(goodsName);
//                     BigDecimal price = new BigDecimal(0L);
//                     BigDecimal price2 = new BigDecimal(0L);
//
//                     Shop shop2 = new Shop();
//                     BeanUtils.copyProperties(shop, shop2);
//
//                     InterfaceLog interfaceLog2 = new InterfaceLog();
//                     String content2 = "美团分账";
//
//                     if (shop.getDivideAccounts() == 1) {
//                         // 扣除商户余额
//                         Integer proportion = shop.getProportion();
//                         price = BigDecimal.valueOf(totalAmount * proportion).divide(BigDecimal.valueOf(100));
//                         BigDecimal totalP = BigDecimal.valueOf(totalAmount);
//
//                         ordersFlow.setType(2);
//                         ordersFlow.setShopDistributionPrice(totalP.subtract(price));
//                         shop2.setBalance(shop.getBalance().subtract(totalP.subtract(price)));
//                         content2 = content2+"，商户出账金额为"+totalP.subtract(price).setScale(2, RoundingMode.HALF_UP)+"元";
//
//                         if (shop.getBalance().subtract(totalP.subtract(price)).compareTo(BigDecimal.valueOf(500)) < 0) {
//                             DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "余额不足"));
//                         }
//                         if (shop.getBalance().subtract(totalP.subtract(price)).compareTo(BigDecimal.valueOf(0)) < 0) {
//                             Orders orders1 = new Orders();
//                             BeanUtils.copyProperties(orders, orders1);
//                             PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "禁用"));
//
//                         }
//                         if (null != agentsServiceOne) {
//                             ordersFlow.setAgentId(agentsServiceOne.getId());
//                             if (agentsServiceOne.getDivideAccounts() == 1) { // 代理
//                                 BigDecimal aa = BigDecimal.valueOf(agentsServiceOne.getProportion()).divide(BigDecimal.valueOf(100));
//                                 price2 = orders.getTotalPrice().multiply(aa);
//                                 ordersFlow.setDistributionPrice(price2);
//                                 content2 = content2+"，代理入账金额为"+price2.setScale(2, RoundingMode.HALF_UP)+"元";
//
//                             }
//                         }
//                         ordersFlow.setPlatDistributionPrice(orders.getTotalPrice().subtract(price).subtract(price2));
//
//                     } else {
//                         ordersFlow.setType(1);
//                         price = orders.getTotalPrice();
//                         shop2.setBalance(shop.getBalance().add(BigDecimal.valueOf(totalAmount)));
//                         ordersFlow.setShopDistributionPrice(BigDecimal.valueOf(totalAmount));
//                         content2 = content2+"，商户入账金额为"+BigDecimal.valueOf(totalAmount).setScale(2, RoundingMode.HALF_UP)+"元";
//                     }
//
//
//
//                     shopsService.updateById(shop);
//
//                     ordersFlow.setPayStatus(1);
//
//                     ordersFlow.setCreateTime(LocalDateTime.now());
//                     ordersFlow.setUpdateTime(LocalDateTime.now());
//                     ordersFlowService.save(ordersFlow);
//
//
//                     interfaceLog2.setTitle("美团分账");
//                     interfaceLog2.setMethodName("startOrders");
//                     content2 = content2+",平台入账"+orders.getTotalPrice().subtract(price).subtract(price2).setScale(2, RoundingMode.HALF_UP)+"元";
//                     interfaceLog2.setContent(content2);
//                     interfaceLog2.setShopId(orders.getShopId());
//                     interfaceLog2.setTypeStatus(0);
//                     interfaceLog2.setCreateTime(LocalDateTime.now());
//                     interfaceLog2.setUpdateTime(LocalDateTime.now());
//                     interfaceLogService.save(interfaceLog2);
//
//                     List<String> paramList = new ArrayList<>();
//
//
//                     if (null != oGoodsGroups && oGoodsGroups.size() > 0) {
//                         for (int i = 0; i < oGoodsGroups.size(); i++) {
//                             OGoodsGroup oGoodsGroup = oGoodsGroups.get(i);
//                             List<OGoodsItem> items = oGoodsGroup.getItems();
//                             if (null != items && items.size() > 0) {
//                                 for (int j = 0; j < items.size(); j++) {
//                                     for (int k = 0; k < items.get(j).getQuantity(); k++) {
//                                         OrderParam orderParam = new OrderParam();
//                                         // BigDecimal total = BigDecimal.valueOf(items.get(j).getTotal());
//                                         orderParam.setPrice(BigDecimal.valueOf(items.get(j).getPrice()));
//                                         // orderParam.setNumber(items.get(j).getQuantity());
//                                         orderParam.setNumber(1);
//                                         orderParam.setOrderId(orders.getId());
//                                         orderParam.setCreateTime(LocalDateTime.now());
//                                         orderParam.setUpdateTime(LocalDateTime.now());
//                                         String originalName = items.get(j).getOriginalName();
//                                         int index = originalName.indexOf("-");
//                                         String name = originalName.substring(0, index);
//                                         LambdaQueryWrapper<ShopToGoods> wrapper3 = new LambdaQueryWrapper<>();
//                                         wrapper3.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getName, name).eq(ShopToGoods::getShopId, shop.getId());
//                                         ShopToGoods goods = shopToGoodsService.getOne(wrapper3);
//                                         orderParam.setGoodsId(goods.getId());
//                                         orderParam.setName(goods.getName());
//                                         orderParam.setUrl(goods.getUrl());
//                                         String specifications = "";
//                                         List<OGroupItemSpec> newSpecs = items.get(j).getNewSpecs();// 规格
//                                         OGroupItemSpec oGroupItemSpec = newSpecs.get(0);
//                                         String cupSize = oGroupItemSpec.getValue();
//                                         LambdaQueryWrapper<Param> wrapper1 = new LambdaQueryWrapper<>();
//                                         wrapper1.eq(Param::getStatus, 1).eq(Param::getName, cupSize);
//                                         Param sizeObj = paramService.getOne(wrapper1);
//                                         orderParam.setSizeId(sizeObj.getId());
//
//
//                                         List<OGroupItemAttribute> attributes = items.get(j).getAttributes(); // 属性
//                                         if (null != attributes && attributes.size() > 0) {
//                                             for (int m = 0; m < attributes.size(); m++) {
//                                                 LambdaQueryWrapper<Param> wrapper5 = new LambdaQueryWrapper<>();
//                                                 wrapper5.eq(Param::getStatus, 1).eq(Param::getName, attributes.get(m).getValue());
//                                                 Param attrObj = paramService.getOne(wrapper5);
//                                                 if (attributes.get(m).getName().contains("糖") || attributes.get(m).getName().contains("甜")) {
//                                                     orderParam.setSugarId(attrObj.getId());
//                                                 } else {
//                                                     orderParam.setTemperatureId(attrObj.getId());
//                                                 }
//                                                 specifications += attributes.get(m).getValue() + "，";
//                                             }
//                                         }
//                                         StringBuilder sb = new StringBuilder(specifications);
//                                         sb.insert(0, cupSize + "，"); // 在索引0的位置插入字符
//                                         String modifiedString = sb.toString();
//                                         int lastIndex = modifiedString.lastIndexOf("，");
//                                         modifiedString = modifiedString.substring(0, lastIndex);
//                                         orderParam.setSpecifications(modifiedString);
//                                         orderParamService.save(orderParam);
//
//                                         // 查询配料是否充足
//
//                                         LambdaQueryWrapper<GoodsToBatch> goodWrapper = new LambdaQueryWrapper<>();
//                                         goodWrapper.eq(GoodsToBatch::getShopId, orders.getShopId()).eq(GoodsToBatch::getGoodsId, goods.getId()).eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getSizeId, orderParam.getSizeId()).eq(GoodsToBatch::getSugarId, orderParam.getSugarId()).eq(GoodsToBatch::getTemperatureId, orderParam.getTemperatureId());
//                                         List<GoodsToBatch> goodsToBatches = goodsToBatchService.list(goodWrapper);
//                                         if (goodsToBatches.size() > 0) {
//                                             goodsToBatches.stream().forEach(batchInfo -> {
//                                                 LambdaQueryWrapper<BatchUse> batchWrapper = new LambdaQueryWrapper<>();
//                                                 batchWrapper.eq(BatchUse::getStatus, 1).eq(BatchUse::getShopId, orders.getShopId()).eq(BatchUse::getBatchId, batchInfo.getBatchId());
//                                                 BatchUse batchUse = batchUseService.getOne(batchWrapper);
//                                                 if (null != batchUse) {
//                                                     if (batchUse.getTotalCount() < batchInfo.getUseNumber()) {
//                                                         Batching batching = batchingService.getById(batchUse.getBatchId());
//                                                         // count[0] = 1;
//                                                         // paramName[0] += paramName[0] + batching.getName() + "、";
//                                                         paramList.add(batching.getName());
//                                                     }
//
//                                                 }
//
//                                             });
//                                         }
//                                     }
//                                 }
//                             }
//                         }
//                     }
//
//                     String messageInfo = "您有新订单啦";
//                     if (paramList.size() > 0) {
//                         String paramName = paramList.stream() // 将List转换为Stream
//                                 .distinct() // 去除重复元素
//                                 .collect(Collectors.joining(","));
//                         messageInfo = paramName + "不足";
//                         // message = paramName[0].substring(0, paramName[0].length() - 1) + "配料不足";
//                         // Arrays.stream(paramName).distinct().toArray();
//                     }
//                     log.info("messageInfo:" + messageInfo);
//                     DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", messageInfo));
//                     DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "下单"));
//                     PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", messageInfo));
//
//                 }
//             }
//             if (elemReqDto.getType() == 12) {//商户确认接单，更改状态，当商户接单时 需要插入富基中间表数据库
//
//             }
//             if (elemReqDto.getType() == 17) {//一分钟内订单取消，直接取消，更改订单状态
//                 // 根据订单编号删除订单
//
//                 Long shopId = orderJson.getLong("shopId");
//                 LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
//                 wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
//                 Shop shop = shopsService.getOne(wrapper2);
//
//
//                 LambdaQueryWrapper<Orders> ordersWrapper2 = new LambdaQueryWrapper<>();
//                 ordersWrapper2.eq(Orders::getStatus, 1).eq(Orders::getOrderNum, orderId);
//                 Orders one = ordersService.getOne(ordersWrapper2);
//                 if (null != one) {
//                     LambdaQueryWrapper<OrdersFlow> flowWrapper = new LambdaQueryWrapper<>();
//                     flowWrapper.eq(OrdersFlow::getStatus, 1).eq(OrdersFlow::getOrderId, one.getId());
//                     OrdersFlow flowServiceOne = ordersFlowService.getOne(flowWrapper);
//                     ordersFlowService.remove(flowWrapper);
//
//                     if (shop.getDivideAccounts() == 1) {
//                         Shop shop1 = new Shop();
//                         BeanUtils.copyProperties(shop, shop1);
//                         shop1.setBalance(shop.getBalance().add(flowServiceOne.getShopDistributionPrice()));
//                         shopsService.updateById(shop1);
//                     }
//
//                     ordersService.removeById(one.getId());
//
//                     InterfaceLog interfaceLog = new InterfaceLog();
//                     interfaceLog.setTitle("美团订单取消成功");
//                     interfaceLog.setMethodName("updateOrders");
//                     String content = "一分钟内订单取消，订单编号为"+one.getOrderNum();
//                     interfaceLog.setContent(content);
//                     interfaceLog.setShopId(shop.getId());
//                     interfaceLog.setTypeStatus(0);
//                     interfaceLog.setCreateTime(LocalDateTime.now());
//                     interfaceLog.setUpdateTime(LocalDateTime.now());
//                     interfaceLogService.save(interfaceLog);
//
//
//                     DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "美团订单取消"));
//                     PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "美团订单取消"));
//                 }
//
//             }
//             if (elemReqDto.getType() == 14) {//接单前取消订单，更改订单状态
//                 // 根据订单编号删除订单
//                 Long shopId = orderJson.getLong("shopId");
//                 LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
//                 wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
//                 Shop shop = shopsService.getOne(wrapper2);
//
//
//                 LambdaQueryWrapper<Orders> ordersWrapper2 = new LambdaQueryWrapper<>();
//                 ordersWrapper2.eq(Orders::getStatus, 1).eq(Orders::getOrderNum, orderId);
//                 Orders one = ordersService.getOne(ordersWrapper2);
//                 if (null != one) {
//                     LambdaQueryWrapper<OrdersFlow> flowWrapper = new LambdaQueryWrapper<>();
//                     flowWrapper.eq(OrdersFlow::getStatus, 1).eq(OrdersFlow::getOrderId, one.getId());
//                     OrdersFlow flowServiceOne = ordersFlowService.getOne(flowWrapper);
//                     ordersFlowService.remove(flowWrapper);
//
//                     if (shop.getDivideAccounts() == 1) {
//                         Shop shop1 = new Shop();
//                         BeanUtils.copyProperties(shop, shop1);
//                         shop1.setBalance(shop.getBalance().add(flowServiceOne.getShopDistributionPrice()));
//                         shopsService.updateById(shop1);
//                     }
//
//                     ordersService.removeById(one.getId());
//
//                     InterfaceLog interfaceLog = new InterfaceLog();
//                     interfaceLog.setTitle("美团订单取消成功");
//                     interfaceLog.setMethodName("updateOrders");
//                     String content = "接单前取消订单，订单编号为"+one.getOrderNum();
//                     interfaceLog.setContent(content);
//                     interfaceLog.setShopId(shop.getId());
//                     interfaceLog.setTypeStatus(0);
//                     interfaceLog.setCreateTime(LocalDateTime.now());
//                     interfaceLog.setUpdateTime(LocalDateTime.now());
//                     interfaceLogService.save(interfaceLog);
//
//
//                     DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "美团订单取消"));
//                     PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "美团订单取消"));
//                 }
//
//             }
//             if (elemReqDto.getType() == 15) {//一分钟后取消订单（商户同意取消订单），更改订单状态
//                 // 根据订单编号删除订单
//                 Long shopId = orderJson.getLong("shopId");
//                 LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
//                 wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
//                 Shop shop = shopsService.getOne(wrapper2);
//
//
//                 LambdaQueryWrapper<Orders> ordersWrapper2 = new LambdaQueryWrapper<>();
//                 ordersWrapper2.eq(Orders::getStatus, 1).eq(Orders::getOrderNum, orderId);
//                 Orders one = ordersService.getOne(ordersWrapper2);
//                 if (null != one) {
//                     LambdaQueryWrapper<OrdersFlow> flowWrapper = new LambdaQueryWrapper<>();
//                     flowWrapper.eq(OrdersFlow::getStatus, 1).eq(OrdersFlow::getOrderId, one.getId());
//                     OrdersFlow flowServiceOne = ordersFlowService.getOne(flowWrapper);
//                     ordersFlowService.remove(flowWrapper);
//
//                     if (shop.getDivideAccounts() == 1) {
//                         Shop shop1 = new Shop();
//                         BeanUtils.copyProperties(shop, shop1);
//                         shop1.setBalance(shop.getBalance().add(flowServiceOne.getShopDistributionPrice()));
//                         shopsService.updateById(shop1);
//                     }
//
//                     ordersService.removeById(one.getId());
//
//                     InterfaceLog interfaceLog = new InterfaceLog();
//                     interfaceLog.setTitle("美团订单取消成功");
//                     interfaceLog.setMethodName("updateOrders");
//                     String content = "一分钟后取消订单（商户同意取消订单），订单编号为"+one.getOrderNum();
//                     interfaceLog.setContent(content);
//                     interfaceLog.setShopId(shop.getId());
//                     interfaceLog.setTypeStatus(0);
//                     interfaceLog.setCreateTime(LocalDateTime.now());
//                     interfaceLog.setUpdateTime(LocalDateTime.now());
//                     interfaceLogService.save(interfaceLog);
//
//
//                     DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "美团订单取消"));
//                     PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "美团订单取消"));
//                 }
//
//
//             }
//             if (elemReqDto.getType() == 18) {//订单已完成，更改订单状态
//
//             }
//             if (elemReqDto.getType() == 20) {//用户申请退单
//                 // 根据订单编号删除订单
//                 // LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
//                 // wrapper.eq(Orders :: getStatus, 1).eq(Orders :: getOrderNum, orderId);
//                 // ordersService.remove(wrapper);
//             }
//             if (elemReqDto.getType() == 37) {//自动退单成功
//                 // 根据订单编号删除订单
//                 Long shopId = orderJson.getLong("shopId");
//                 LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
//                 wrapper2.eq(Shop::getStatus, 1).eq(Shop::getEleShopId, shopId);
//                 Shop shop = shopsService.getOne(wrapper2);
//
//
//                 LambdaQueryWrapper<Orders> ordersWrapper2 = new LambdaQueryWrapper<>();
//                 ordersWrapper2.eq(Orders::getStatus, 1).eq(Orders::getOrderNum, orderId);
//                 Orders one = ordersService.getOne(ordersWrapper2);
//                 if (null != one) {
//                     LambdaQueryWrapper<OrdersFlow> flowWrapper = new LambdaQueryWrapper<>();
//                     flowWrapper.eq(OrdersFlow::getStatus, 1).eq(OrdersFlow::getOrderId, one.getId());
//                     OrdersFlow flowServiceOne = ordersFlowService.getOne(flowWrapper);
//                     ordersFlowService.remove(flowWrapper);
//
//                     if (shop.getDivideAccounts() == 1) {
//                         Shop shop1 = new Shop();
//                         BeanUtils.copyProperties(shop, shop1);
//                         shop1.setBalance(shop.getBalance().add(flowServiceOne.getShopDistributionPrice()));
//                         shopsService.updateById(shop1);
//                     }
//
//                     ordersService.removeById(one.getId());
//
//                     InterfaceLog interfaceLog = new InterfaceLog();
//                     interfaceLog.setTitle("美团订单取消成功");
//                     interfaceLog.setMethodName("updateOrders");
//                     String content = "自动退单成功，订单编号为"+one.getOrderNum();
//                     interfaceLog.setContent(content);
//                     interfaceLog.setShopId(shop.getId());
//                     interfaceLog.setTypeStatus(0);
//                     interfaceLog.setCreateTime(LocalDateTime.now());
//                     interfaceLog.setUpdateTime(LocalDateTime.now());
//                     interfaceLogService.save(interfaceLog);
//
//
//                     DeviceWebSocket.sendOneMessage(shop.getId(), new CodeData("ok", "美团订单取消"));
//                     PlatformWebSocket.sendOneMessage(shop.getAdminId(), new CodeData("ok", "美团订单取消"));
//                 }
//
//             }
//         }
//
//         JSONObject jsonObject = new JSONObject();
//         jsonObject.put("message", "ok");
//         return R.ok();
//     }
//
//
//     /**
//      * 授权码(企业)模式刷新Token
//      */
//     @ApiOperation(value = "授权码(企业)模式刷新Token")
//     private void serverRefreshTokenTest() {
//         String refreshTokenStr = "XXXXXXXXXXX";
//         Token token = client.getTokenByRefreshToken(getToken().getRefreshToken());
//         if (token.isSuccess()) {
//             setTokenInfo(token);
//             System.out.println(token);
//         } else {
//             System.out.println("error_code: " + token.getError());
//             System.out.println("error_desc: " + token.getError_description());
//         }
//     }
//
//     /**
//      * 已获取Token信息后使用
//      *
//      * @return
//      */
//     @ApiOperation(value = "已获取Token信息后使用")
//     private static Token getToken() {
//         String access_token = PropertiesUtils.getPropValueByKey("access_token");
//         String token_type = PropertiesUtils.getPropValueByKey("token_type");
//         String expires_in = PropertiesUtils.getPropValueByKey("expires_in");
//         String refresh_token = PropertiesUtils.getPropValueByKey("refresh_token");
//         if (access_token.isEmpty()) {
//             System.out.println("access_token is null");
//             return null;
//         }
//         Token token = new Token();
//         token.setAccessToken(access_token);
//         token.setTokenType(token_type);
//         token.setExpires(Long.valueOf(expires_in));
//         token.setRefreshToken(refresh_token);
//         return token;
//     }
//
//     @ApiOperation(value = "设置Token信息")
//     private static void setTokenInfo(Token token) {
//         if (null != token && token.isSuccess()) {
//             tokenMap.put("access_token", token.getAccessToken());
//             tokenMap.put("token_type", token.getTokenType());
//             tokenMap.put("expires_in", String.valueOf(token.getExpires()));
//             tokenMap.put("refresh_token", token.getRefreshToken());
//             PropertiesUtils.setProps(tokenMap);
//         }
//     }
//
//     @SneakyThrows
//     @ApiOperation(value = "获取美团的店铺信息")
//     @GetMapping("getShopInfo")
//     public R getShopInfo(@RequestParam("shopId") Long shopId, @RequestParam("access_token") String access_token, @RequestParam("token_type") String token_type,
//                          @RequestParam("expires_in") String expires_in, @RequestParam("refresh_token") String refresh_token) {
//
//         Token token = new Token();
//         token.setAccessToken(access_token);
//         token.setTokenType(token_type);
//         token.setExpires(Long.valueOf(expires_in));
//         token.setRefreshToken(refresh_token);
//         ShopService shopService = new ShopService(config, token);
//         OShop shop = shopService.getShop(shopId);
//
//         LambdaQueryWrapper<Shop> query = new LambdaQueryWrapper<>();
//         query.eq(Shop::getEleShopId, shopId).eq(Shop::getStatus, 1);
//         Shop one = shopsService.getOne(query);
//         Shop shop1 = new Shop();
//         BeanUtils.copyProperties(one, shop1);
//         shop1.setAccessToken(access_token);  // 商户token信息
//         shop1.setTokenType(token_type);  // 商户token信息
//         shop1.setExpiresIn(Long.valueOf(expires_in));  // 商户token信息
//         shop1.setRefreshToken(refresh_token);  // 商户token信息
//         shop1.setName(shop.getName());
//         // shop1.setBusyLevel(String.valueOf(shop.getBusyLevel()));
//         // shop1.setDeliverSpent(shop.getDeliverSpent());
//         // shop1.setDescription(shop.getDescription());
//         // shop1.setImageUrl(shop.getImageUrl());
//         // shop1.setInvoice(shop.getInvoice());
//         // shop1.setInvoiceMinAmount(BigDecimal.valueOf(shop.getInvoiceMinAmount()));
//         // shop1.setIsBookable(shop.getIsBookable());
//         // shop1.setIsOpen(shop.getIsOpen());
//         // shop1.setIsPhoneHidden(shop.getIsPhoneHidden());
//         // shop1.setIsPremium(shop.getIsPremium());
//         // shop1.setIsTimeEnsure(shop.getIsTimeEnsure());
//         // shop1.setTimeEnsureFullDescription(shop.getTimeEnsureFullDescription());
//         // shop1.setMobile(shop.getMobile());
//         // shop1.setNoAgentFeeTotal(BigDecimal.valueOf(shop.getNoAgentFeeTotal()));
//         // shop1.setOnlinePayment(shop.getOnlinePayment());
//         // if (null != shop.getPhones() && shop.getPhones().size() > 0) {
//         //     String phones = shop.getPhones().stream().collect(Collectors.joining(","));
//         //     shop1.setPhones(phones);
//         // }
//         // shop1.setPromotionInfo(shop.getPromotionInfo());
//         // shop1.setRecentFoodPopularity(shop.getRecentFoodPopularity());
//         // shop1.setRecentFoodPopularityFuzzy(shop.getRecentFoodPopularityFuzzy());
//         // if (null != shop.getServingTime() && shop.getServingTime().size() > 0) {
//         //     String servingTime = shop.getServingTime().stream().collect(Collectors.joining(","));
//         //     shop1.setServingTime(servingTime);
//         // }
//         // if (null != shop.getNumRatings() && shop.getNumRatings().size() > 0) {
//         //     String numRatings = shop.getNumRatings().stream().map(String::valueOf).collect(Collectors.joining(","));
//         //     shop1.setNumRatings(numRatings);
//         // }
//         // shop1.setFlexibleBusinessTime(shop.getFlexibleBusinessTime());
//         // shop1.setSupportOnline(shop.getSupportOnline() ? 1 : 0);
//         // shop1.setPackingFee(BigDecimal.valueOf(shop.getPackingFee()));
//         // shop1.setOpenId(shop.getOpenId());
//         // shop1.setOnlineRefund(shop.getOnlineRefund() ? 1 : 0);
//         shop1.setUpdateTime(LocalDateTime.now());
//         shopsService.updateById(shop1);
//
//
//         // ProductService productService = new ProductService(config, token);
//         // List<OCategory> shopCategories = productService.getShopCategories(one.getShopId());
//         // // 保存分类
//         // if (null != shopCategories && shopCategories.size() > 0){
//         //     List<Long> ids = shopCategories.stream().map(item -> item.getId()).collect(Collectors.toList());
//         //     LambdaQueryWrapper<Categorize> wrapper2 = new LambdaQueryWrapper<>();
//         //     wrapper2.eq(Categorize :: getStatus, 1).eq(Categorize :: getShopId, shopId).notIn(Categorize :: getEleId, ids);
//         //     categorizeService.remove(wrapper2);
//         //     shopCategories.stream().forEach(item -> {
//         //         if (null != item.getDescription() && !item.getDescription().equals("")){
//         //             LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();
//         //             wrapper.eq(Categorize :: getStatus, 1).eq(Categorize :: getEleId, item.getId()).eq(Categorize :: getShopId, shopId);
//         //             Categorize categorize1 = categorizeService.getOne(wrapper);
//         //             if (null == categorize1){
//         //                 Categorize categorize = new Categorize();
//         //                 categorize.setShopId(one.getId());
//         //                 categorize.setName(item.getName());
//         //                 categorize.setRealName(item.getDescription());
//         //                 categorize.setEleId(item.getId());
//         //                 categorize.setType(1);
//         //                 categorize.setCreateTime(LocalDateTime.now());
//         //                 categorize.setUpdateTime(LocalDateTime.now());
//         //                 categorizeService.save(categorize);
//         //             }else {
//         //                 categorize1.setName(item.getName());
//         //                 categorize1.setRealName(item.getDescription());
//         //                 categorize1.setUpdateTime(LocalDateTime.now());
//         //                 categorizeService.updateById(categorize1);
//         //             }
//         //         }
//         //     });
//         // }else {
//         //     LambdaQueryWrapper<Categorize> wrapper3 = new LambdaQueryWrapper<>();
//         //     wrapper3.eq(Categorize :: getStatus, 1).eq(Categorize :: getShopId, shopId);
//         //     categorizeService.remove(wrapper3);
//         // }
//
//         // Map<OShopProperty,Object> properties = new HashMap<OShopProperty,Object>();
//         // properties.put(OShopProperty.isOpen,1);
//         // shopService.updateShop(shopId, properties);
//         return R.ok().data("shopInfo", shop);
//
//     }
//
//
//     @SneakyThrows
//     @ApiOperation(value = "添加美团的店铺商品分类")
//     @PostMapping("createCategory")
//     public R createCategory(@RequestBody Categorize obj) {
//         Token token = new Token();
//         token.setAccessToken(obj.getAccessToken());
//         token.setTokenType(obj.getTokenType());
//         token.setExpires(obj.getExpiresIn());
//         token.setRefreshToken(obj.getRefreshToken());
//
//         Shop shop = shopsService.getById(obj.getShopId());
//
//
//         String mToekn = null;
//         // 请求body参数
//         Gson gson = new Gson();
//         HttpPost httpPost = new HttpPost("https://waimaiopen.meituan.com/api/v1/foodCat/update");
//         HashMap<Object, Object> paramsMap = new HashMap<>();
//         long timestamp = System.currentTimeMillis() / 1000;
//         paramsMap.put("app_id", "");
//         paramsMap.put("timestamp", timestamp);
//         paramsMap.put("sig", "");
//         paramsMap.put("app_poi_code", "");// APP方门店ID
//         paramsMap.put("category_name", ""); // 创建的菜品分类名称
//         paramsMap.put("sequence", ""); // 新建时必须
//         paramsMap.put("category_description", ""); // 新建时必须
//         //将参数转化未json字符串
//         String jsonParamsMap = gson.toJson(paramsMap);
//
//         log.info("请求参数：" + jsonParamsMap);
//
//         StringEntity entity = new StringEntity(jsonParamsMap, "utf-8");
//         entity.setContentType("application/json");
//         httpPost.setEntity(entity);
//         httpPost.setHeader("Accept", "application/json");
//
//         //完成签名并执行请求
//         CloseableHttpResponse resp = null;
//         try {
//             log.info("eeeeeeeeeeeeeee*************:"+httpPost);
//
//             CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//             resp = httpClient.execute(httpPost);
//             int statusCode = resp.getStatusLine().getStatusCode();
//             String bodyAsString = EntityUtils.toString(resp.getEntity());
//             log.info("33333333333***************************************："+resp);
//             log.info("statusCode***************************************："+statusCode);
//             if (statusCode == 201) { //处理成功
//                 log.info("成功，返回结果 = " + bodyAsString);
//                 //相应结果
//                 HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
//                 //获取机器的token
//                 mToekn = resultMap.get("token");
//
//
//             }
//             resp.close();
//
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//
//
//         // 添加美团店铺商品分类
//         ProductService productService = new ProductService(config, token);
//         OCategory category = productService.createCategory(shop.getEleShopId(), obj.getName(), obj.getDescription());
//
//         if (null != category) {
//             long id = category.getId();
//             Categorize categorize = new Categorize();
//             categorize.setName(obj.getName());
//             categorize.setEleId(id);
//             categorize.setType(1);
//             CategorizeAll categorizeAll = categorizeAllService.getById(obj.getCateAllId());
//             categorize.setCateAllId(obj.getCateAllId());
//             categorize.setShopId(obj.getShopId());
//             categorize.setDescription(obj.getDescription());
//             categorize.setMachineNo(obj.getMachineNo());
//             categorize.setRealName(categorizeAll.getName());
//             categorize.setCreateTime(LocalDateTime.now());
//             categorize.setUpdateTime(LocalDateTime.now());
//             boolean save = categorizeService.save(categorize);
//             if (save) {
//                 InterfaceLog interfaceLog = new InterfaceLog();
//                 interfaceLog.setTitle("添加美团分类");
//                 interfaceLog.setMethodName("createCategory");
//                 String content = "商户"+shop.getName()+"添加美团分类，分类名为"+obj.getName();
//                 interfaceLog.setContent(content);
//                 interfaceLog.setShopId(obj.getShopId());
//                 interfaceLog.setTypeStatus(0);
//                 interfaceLog.setCreateTime(LocalDateTime.now());
//                 interfaceLog.setUpdateTime(LocalDateTime.now());
//                 interfaceLogService.save(interfaceLog);
//                 return R.ok();
//             } else {
//                 return R.error();
//             }
//         } else {
//             return R.error();
//         }
//
//     }
//
//     @SneakyThrows
//     @ApiOperation(value = "修改美团的店铺商品分类")
//     @PostMapping("updateCategory")
//     public R updateCategory(@RequestBody Categorize obj) {
//         Token token = new Token();
//         token.setAccessToken(obj.getAccessToken());
//         token.setTokenType(obj.getTokenType());
//         token.setExpires(obj.getExpiresIn());
//         token.setRefreshToken(obj.getRefreshToken());
//
//
//
//
//         // 修改美团店铺商品分类
//         ProductService productService = new ProductService(config, token);
//         OCategory category = productService.updateCategory(obj.getEleId(), obj.getName(), obj.getDescription());
//         if (null != category) {
//             long id = category.getId();
//             Categorize categorize = categorizeService.getById(obj.getId());
//             CategorizeAll categorizeAll = categorizeAllService.getById(obj.getCateAllId());
//             categorize.setCateAllId(obj.getCateAllId());
//             categorize.setName(obj.getName());
//             categorize.setEleId(id);
//             categorize.setDescription(obj.getDescription());
//             categorize.setMachineNo(obj.getMachineNo());
//             categorize.setRealName(categorizeAll.getName());
//             categorize.setUpdateTime(LocalDateTime.now());
//             boolean save = categorizeService.updateById(categorize);
//             if (save) {
//                 String mToekn = null;
//                 // 请求body参数
//                 Gson gson = new Gson();
//                 HttpPost httpPost = new HttpPost("https://waimaiopen.meituan.com/api/v1/foodCat/update");
//                 HashMap<Object, Object> paramsMap = new HashMap<>();
//                 long timestamp = System.currentTimeMillis() / 1000;
//                 paramsMap.put("app_id", "");
//                 paramsMap.put("timestamp", timestamp);
//                 paramsMap.put("sig", "");
//                 paramsMap.put("app_poi_code", "");// APP方门店ID
//                 paramsMap.put("category_name_origin", categorizeAll.getName()); // 原始的菜品分类名称  更新时必须
//                 paramsMap.put("category_name", obj.getName()); // 创建的菜品分类名称
//                 //将参数转化未json字符串
//                 String jsonParamsMap = gson.toJson(paramsMap);
//
//                 log.info("请求参数：" + jsonParamsMap);
//
//                 StringEntity entity = new StringEntity(jsonParamsMap, "utf-8");
//                 entity.setContentType("application/json");
//                 httpPost.setEntity(entity);
//                 httpPost.setHeader("Accept", "application/json");
//
//                 //完成签名并执行请求
//                 CloseableHttpResponse resp = null;
//                 try {
//                     log.info("eeeeeeeeeeeeeee*************:"+httpPost);
//
//                     CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//                     resp = httpClient.execute(httpPost);
//                     int statusCode = resp.getStatusLine().getStatusCode();
//                     String bodyAsString = EntityUtils.toString(resp.getEntity());
//                     log.info("33333333333***************************************："+resp);
//                     log.info("statusCode***************************************："+statusCode);
//                     if (statusCode == 201) { //处理成功
//                         log.info("成功，返回结果 = " + bodyAsString);
//                         //相应结果
//                         HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
//                         //获取机器的token
//                         mToekn = resultMap.get("token");
//
//
//                     }
//                     resp.close();
//
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 }
//
//
//                 Shop shop = shopsService.getById(categorize.getShopId());
//                 InterfaceLog interfaceLog = new InterfaceLog();
//                 interfaceLog.setTitle("修改美团分类");
//                 interfaceLog.setMethodName("updateCategory");
//                 String content = "商户"+shop.getName()+"修改美团分类，分类名为"+obj.getName();
//                 interfaceLog.setContent(content);
//                 interfaceLog.setShopId(categorize.getShopId());
//                 interfaceLog.setTypeStatus(0);
//                 interfaceLog.setCreateTime(LocalDateTime.now());
//                 interfaceLog.setUpdateTime(LocalDateTime.now());
//                 interfaceLogService.save(interfaceLog);
//                 return R.ok();
//             } else {
//                 return R.error();
//             }
//         } else {
//             return R.error();
//         }
//
//     }
//
//     @SneakyThrows
//     @ApiOperation(value = "删除美团的店铺商品分类")
//     @DeleteMapping("{eleId}")
//     public R invalidCategory(@PathVariable("eleId") Long eleId, @RequestParam("access_token") String access_token, @RequestParam("token_type") String token_type,
//                              @RequestParam("expires_in") String expires_in, @RequestParam("refresh_token") String refresh_token) {
//         Token token = new Token();
//         token.setAccessToken(access_token);
//         token.setTokenType(token_type);
//         token.setExpires(Long.valueOf(expires_in));
//         token.setRefreshToken(refresh_token);
//
//         // 删除饿了么店铺商品分类
//         ProductService productService = new ProductService(config, token);
//         productService.invalidCategory(eleId);
//
//
//
//         // 删除库里商品分类
//         LambdaQueryWrapper<Categorize> wrapper = new LambdaQueryWrapper<>();
//         wrapper.eq(Categorize::getStatus, 1).eq(Categorize::getEleId, eleId);
//         Categorize categorize = categorizeService.getOne(wrapper);
//         boolean remove = categorizeService.removeById(categorize.getId());
//         if (remove) {
//             String mToekn = null;
//             // 请求body参数
//             Gson gson = new Gson();
//             HttpPost httpPost = new HttpPost("https://waimaiopen.meituan.com/api/v1/foodCat/delete");
//             HashMap<Object, Object> paramsMap = new HashMap<>();
//             long timestamp = System.currentTimeMillis() / 1000;
//             paramsMap.put("app_id", "");
//             paramsMap.put("timestamp", timestamp);
//             paramsMap.put("sig", "");
//             paramsMap.put("app_poi_code", "");// APP方门店ID
//             paramsMap.put("category_name", categorize.getName()); // 创建的菜品分类名称
//             //将参数转化未json字符串
//             String jsonParamsMap = gson.toJson(paramsMap);
//
//             log.info("请求参数：" + jsonParamsMap);
//
//             StringEntity entity = new StringEntity(jsonParamsMap, "utf-8");
//             entity.setContentType("application/json");
//             httpPost.setEntity(entity);
//             httpPost.setHeader("Accept", "application/json");
//
//             //完成签名并执行请求
//             CloseableHttpResponse resp = null;
//             try {
//                 log.info("eeeeeeeeeeeeeee*************:"+httpPost);
//
//                 CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//                 resp = httpClient.execute(httpPost);
//                 int statusCode = resp.getStatusLine().getStatusCode();
//                 String bodyAsString = EntityUtils.toString(resp.getEntity());
//                 log.info("33333333333***************************************："+resp);
//                 log.info("statusCode***************************************："+statusCode);
//                 if (statusCode == 201) { //处理成功
//                     log.info("成功，返回结果 = " + bodyAsString);
//                     //相应结果
//                     HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
//                     //获取机器的token
//                     mToekn = resultMap.get("token");
//
//
//                 }
//                 resp.close();
//
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//
//             Shop shop = shopsService.getById(categorize.getShopId());
//             InterfaceLog interfaceLog = new InterfaceLog();
//             interfaceLog.setTitle("删除美团分类");
//             interfaceLog.setMethodName("");
//             String content = "商户"+shop.getName()+"删除美团分类，分类名为"+categorize.getName();
//             interfaceLog.setContent(content);
//             interfaceLog.setShopId(categorize.getShopId());
//             interfaceLog.setTypeStatus(0);
//             interfaceLog.setCreateTime(LocalDateTime.now());
//             interfaceLog.setUpdateTime(LocalDateTime.now());
//             interfaceLogService.save(interfaceLog);
//             return R.ok();
//         } else {
//             return R.error();
//         }
//
//
//     }
//
//     public static byte[] fileToByteArray(String filePath) {
//         //1.创建源yu目的地
//         File file = new File(filePath);
//         byte[] ds = null;
//         //选择流
//         InputStream zp = null;
//         ByteArrayOutputStream boos = null;
//         boos = new ByteArrayOutputStream();
//         try {
//             zp = new FileInputStream(file);
//             byte[] frush = new byte[1024];//1024表示1k为一段
//             int len = -1;
//             while ((len = zp.read(frush)) != -1) {
//                 boos.write(frush, 0, len);//写出到字节数组中
//
//             }
//             boos.flush();
//             return boos.toByteArray();
//         } catch (FileNotFoundException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         } catch (IOException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         } finally {
//             if (zp != null) {
//                 try {
//                     zp.close();
//                 } catch (IOException e) {
//                     // TODO Auto-generated catch block
//                     e.printStackTrace();
//                 }
//             }
//
//         }
//         return null;
//
//     }
//
//     @ApiOperation(value = "上传商品图片")
//     public String upload(String images){
//         byte[] bytes = fileToByteArray(weChatProperties.getUploadPath() + images);
//         // 请求body参数
//         Gson gson = new Gson();
//         HttpPost httpPost = new HttpPost("https://waimaiopen.meituan.com/api/v1/image/upload");
//         HashMap<Object, Object> paramsMap = new HashMap<>();
//         long timestamp = System.currentTimeMillis() / 1000;
//         paramsMap.put("app_id", "");
//         paramsMap.put("timestamp", timestamp);
//         paramsMap.put("sig", "");
//         paramsMap.put("app_poi_code", "");// APP方门店ID
//         paramsMap.put("img_data", bytes); // 图片字节流，图片转成 byte 数组的格式
//         paramsMap.put("img_name", timestamp+".jpg"); // 图片
//         //将参数转化未json字符串
//         String jsonParamsMap = gson.toJson(paramsMap);
//
//         log.info("请求参数：" + jsonParamsMap);
//
//         StringEntity entity = new StringEntity(jsonParamsMap, "utf-8");
//         entity.setContentType("application/json");
//         httpPost.setEntity(entity);
//         httpPost.setHeader("Accept", "application/json");
//
//         //完成签名并执行请求
//         CloseableHttpResponse resp = null;
//         try {
//             String mToekn = "";
//             log.info("eeeeeeeeeeeeeee*************:"+httpPost);
//
//             CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//             resp = httpClient.execute(httpPost);
//             int statusCode = resp.getStatusLine().getStatusCode();
//             String bodyAsString = EntityUtils.toString(resp.getEntity());
//             log.info("33333333333***************************************："+resp);
//             log.info("statusCode***************************************："+statusCode);
//             if (statusCode == 201) { //处理成功
//                 log.info("成功，返回结果 = " + bodyAsString);
//                 //相应结果
//                 HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);
//                 //获取机器的token
//                 mToekn = resultMap.get("data");
//                 System.out.println("mToekn");
//
//
//             }
//             resp.close();
//             return mToekn;
//
//         } catch (IOException e) {
//             e.printStackTrace();
//             return null;
//         }
//     }
//
//     @SneakyThrows
//     @ApiOperation(value = "添加美团的店铺商品")
//     @PostMapping("createGoods")
//     public R createGoods(@RequestBody ShopToGoods obj) {
//
//         ShopToGoods shopToGoods = new ShopToGoods();
//         LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
//         wrapper.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getGoodsId, obj.getGoodsId()).eq(ShopToGoods::getShopId, obj.getShopId());
//         List<ShopToGoods> list = shopToGoodsService.list(wrapper);
//         if (null != list && list.size() > 0) {
//             return R.ok().message("该商品已添加");
//         } else {
//             Token token = new Token();
//             token.setAccessToken(obj.getAccessToken());
//             token.setTokenType(obj.getTokenType());
//             token.setExpires(obj.getExpiresIn());
//             token.setRefreshToken(obj.getRefreshToken());
//
//
//
//
//             // 获取商品基本价格
//             // double goodsPrice = obj.getPrice().doubleValue();
//             double goodsPrice = 0.00;
//             ProductService productService = new ProductService(config, token);
//
//             // 上传图片获取hash值
//             String imagePath = weChatProperties.getUploadPath() + obj.getUrl();
//             byte[] fileContent = Files.readAllBytes(new File(imagePath).toPath());
//             String base64String = Base64.getEncoder().encodeToString(fileContent);
//             String imageHash = productService.uploadImage(base64String);
//
//
//             LambdaQueryWrapper<ShopToGoods> wrapper2 = new LambdaQueryWrapper<>();
//             wrapper2.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods :: getCateId, obj.getCateId()).eq(ShopToGoods::getShopId, obj.getShopId());
//             List<ShopToGoods> list2 = shopToGoodsService.list(wrapper2);
//
//             Map<MtGoods, Object> properties = new HashMap<MtGoods, Object>();
//             long timestamp = System.currentTimeMillis()/1000;
//
//
//             properties.put(MtGoods.app_id, obj.getName());
//             properties.put(MtGoods.timestamp, timestamp);
//             properties.put(MtGoods.sig, "");
//
//
//
//             properties.put(MtGoods.price, obj.getPrice());
//             properties.put(MtGoods.min_order_count, 1);
//             properties.put(MtGoods.unit, "杯");
//             properties.put(MtGoods.box_num, 1.0);
//             properties.put(MtGoods.box_price, 0.0);
//             properties.put(MtGoods.is_sold_out, obj.getIsGrounding() == 1 ? 0 : 1);
//             properties.put(MtGoods.sequence, list2.size() > 0 ? list2.size() + 1 : 1);
//             // properties.put(MtGoods.speciality, "");
//             properties.put(MtGoods.is_not_single, 2);
//             properties.put(MtGoods.onlySellInCombo, false);
//
//             // 获取规格参数
//             LambdaQueryWrapper<Param> queryParam = new LambdaQueryWrapper<Param>();
//             queryParam.eq(Param::getStatus, 1).eq(Param::getType, 1);
//             List<Param> paramList = paramService.list(queryParam);
//
//             LambdaQueryWrapper<Param> queryParam2 = new LambdaQueryWrapper<Param>();
//             queryParam2.eq(Param::getStatus, 1).eq(Param::getType, 2);
//             List<Param> sugarList = paramService.list(queryParam2);
//
//             LambdaQueryWrapper<Param> queryParam3 = new LambdaQueryWrapper<Param>();
//             queryParam3.eq(Param::getStatus, 1).eq(Param::getType, 3);
//             List<Param> temperatureList = paramService.list(queryParam3);
//
//             Integer count = (null != sugarList && sugarList.size() > 0 ? 1 : 0) + (null != temperatureList && temperatureList.size() > 0 ? 1 : 0);
//             List<OSpecProperty> specProperties = new ArrayList<>();
//             List<OSpecDetail> specDetails = new ArrayList<>();
//             List<OSpec> oSpecs = new ArrayList<>();
//
//             List<SpuAttr> supAttrs = new ArrayList<>();
//             List<Skus> skusList = new ArrayList<>();
//
//             // 添加商品到库里
//             BeanUtils.copyProperties(obj, shopToGoods);
//             shopToGoods.setCreateTime(LocalDateTime.now());
//             shopToGoods.setUpdateTime(LocalDateTime.now());
//             shopToGoods.setEleId(item.getId());
//             shopToGoods.setImageHash(imageHash);
//             boolean save = shopToGoodsService.save(shopToGoods);
//             if (save) {
//                 properties.put(MtGoods.app_food_code, shopToGoods.getId());
//                 properties.put(MtGoods.picture, upload(shopToGoods.getUrl()));
//                 properties.put(MtGoods.longPictures, upload(shopToGoods.getDetailsUrl()));
//                 properties.put(MtGoods.name, shopToGoods.getName());
//                 properties.put(MtGoods.description, shopToGoods.getIntroduce());
//                 properties.put(MtGoods.category_name, categorize.getRealName());
//
//                 // 添加配料
//                 final Integer[] count2 = {0};
//                 LambdaQueryWrapper<GoodsAllBatch> queryWrapper = new LambdaQueryWrapper<>();
//                 queryWrapper.eq(GoodsAllBatch::getStatus, 1).eq(GoodsAllBatch::getGoodsId, obj.getGoodsId());
//                 List<GoodsAllBatch> allBatches = goodsAllBatchService.list(queryWrapper);
//                 if (null != allBatches && allBatches.size() > 0) {
//                     allBatches.stream().forEach(aa -> {
//                         GoodsToBatch goodsToBatch = new GoodsToBatch();
//                         goodsToBatch.setBatchId(aa.getBatchId());
//                         goodsToBatch.setUseNumber(aa.getUseNumber());
//                         goodsToBatch.setGoodsId(shopToGoods.getId());
//                         goodsToBatch.setTemperatureId(aa.getTemperatureId());
//                         goodsToBatch.setSugarId(aa.getSugarId());
//                         goodsToBatch.setSizeId(aa.getSizeId());
//                         goodsToBatch.setCreateTime(LocalDateTime.now());
//                         goodsToBatch.setUpdateTime(LocalDateTime.now());
//                         goodsToBatch.setShopId(obj.getShopId());
//                         goodsToBatch.setPrice(shopToGoods.getPrice());
//                         goodsToBatch.setCost(shopToGoods.getCost());
//                         boolean b = goodsToBatchService.save(goodsToBatch);
//                         if (b) {
//                             count2[0] = count2[0] + 1;
//                         }
//                     });
//                 }
//
//                 if (count2[0] == allBatches.size()) {
//                     Shop shop = shopsService.getById(obj.getShopId());
//                     properties.put(MtGoods.app_poi_code, shop.getMtShopId());
//
//                     InterfaceLog interfaceLog = new InterfaceLog();
//                     interfaceLog.setTitle("添加美团商品");
//                     interfaceLog.setMethodName("createGoods");
//                     String content = "商户"+shop.getName()+"添加美团商品，商品名为"+obj.getName();
//                     interfaceLog.setContent(content);
//                     interfaceLog.setShopId(obj.getShopId());
//                     interfaceLog.setTypeStatus(0);
//                     interfaceLog.setCreateTime(LocalDateTime.now());
//                     interfaceLog.setUpdateTime(LocalDateTime.now());
//                     interfaceLogService.save(interfaceLog);
//
//                     // 获取参数保存到美团
//                     if (null != paramList && paramList.size() > 0) {
//
//                         paramList.stream().forEach(item -> {
//                             Skus skus = new Skus();
//
//                             OSpecDetail oSpecDetail = new OSpecDetail();
//                             oSpecDetail.setSpecValue(item.getName());
//                             oSpecDetail.setSelectedStatus(0);
//                             specDetails.add(oSpecDetail);
//                             // 随机生成数字字符串
//                             Random random = new Random();
//                             int length = 10; // 生成的数字字符串长度
//                             StringBuilder sb = new StringBuilder(length);
//                             for (int i = 0; i < length; i++) {
//                                 sb.append(random.nextInt(10)); // 生成0到9之间的随机数字并添加到StringBuilder中
//                             }
//                             String randomNumberString = sb.toString();
//                             String randomNumberString2 = "X" + String.valueOf(new Date().getTime());
//
//                             skus.setSku_id(String.valueOf(shopToGoods.getId()));
//                             skus.setSpec(item.getName());
//                             skus.setPrice(String.valueOf(goodsPrice + item.getAddPrice().doubleValue()));
//                             skus.setStock("*");
//                             skus.setWeight(1L);
//                             skus.setWeight_unit("杯");
//                             skusList.add(skus);
//
//
//                             OSpec oSpec = new OSpec();
//                             // oSpec.setSpecId(72970000222L); // 创建不填写
//                             oSpec.setName(item.getName());
//                             oSpec.setPrice(goodsPrice + item.getAddPrice().doubleValue());
//                             oSpec.setPriceType("SUB_PRICE");
//                             oSpec.setPackageType("GROUP_CHOOSE");
//                             oSpec.setStock(900); // 库存量
//                             oSpec.setMaxStock(1000); // 最大库存量
//                             oSpec.setStockStatus(1); // 是否次日自动置满库存 0:不自动置满 或者 1: 次日自动置满
//                             oSpec.setPackingFee(0); // 打包费
//                             oSpec.setOnShelf(1); // 是否上架，1:上架，0:下架，不填写默认0
//                             oSpec.setExtendCode(randomNumberString); // 商品扩展码
//                             oSpec.setBarCode(randomNumberString2); // 商品条形码
//                             oSpec.setActivityLevel(0); // 商品活动信息，1:有活动，0:无活动
//                             OSpecAttribute oSpecAttribute = new OSpecAttribute(); // 规格扩展信息
//                             oSpecAttribute.setUnit("杯");
//                             oSpecAttribute.setWeight("1");
//                             oSpecAttribute.setPackageShare(1);
//                             oSpec.setSpecAttribute(oSpecAttribute); // 规格扩展信息
//                             if (count >= 0) {
//                                 List<OItemAttribute> attributes = new ArrayList<>();
//                                 if (null != sugarList && sugarList.size() > 0) {
//                                     List<String> names = sugarList.stream().map(ee -> ee.getName()).collect(Collectors.toList());
//                                     Long paramCateId = sugarList.get(0).getParamCateId();
//                                     ParamCate paramCate = paramCateService.getById(paramCateId);
//
//                                     OItemAttribute oItemAttribute = new OItemAttribute(); // 规格维度商品属性
//                                     oItemAttribute.setName(paramCate.getName());
//                                     oItemAttribute.setDetails(names);
//                                     attributes.add(oItemAttribute);
//                                     oSpec.setAttributes(attributes); // 规格维度商品属性
//                                 }
//                                 if (null != temperatureList && temperatureList.size() > 0) {
//                                     List<String> names = temperatureList.stream().map(ee -> ee.getName()).collect(Collectors.toList());
//                                     Long paramCateId = temperatureList.get(0).getParamCateId();
//                                     ParamCate paramCate = paramCateService.getById(paramCateId);
//
//                                     OItemAttribute oItemAttribute = new OItemAttribute(); // 规格维度商品属性
//                                     oItemAttribute.setName(paramCate.getName());
//                                     oItemAttribute.setDetails(names);
//                                     attributes.add(oItemAttribute);
//                                     oSpec.setAttributes(attributes); // 规格维度商品属性
//                                 }
//
//                             }
//                             OSpecProperty oSpecProperty = new OSpecProperty();
//                             oSpecProperty.setSpecGroupName("规格");
//                             oSpecProperty.setLinkPhotoStatus(0);
//                             oSpecProperty.setSpecDetails(specDetails);
//                             specProperties.add(oSpecProperty);
//                             oSpecs.add(oSpec);
//
//                         });
//
//                     }
//
//                     // 获取除小料外的规格参数
//                     if (null != sugarList && sugarList.size() > 0) {
//                         List<SpuAttr> spuAttrs = new ArrayList<>();
//                         sugarList.stream().forEach(item -> {
//                             OSpecDetail oSpecDetail = new OSpecDetail();
//                             oSpecDetail.setSpecValue(item.getName());
//                             oSpecDetail.setSelectedStatus(0);
//                             specDetails.add(oSpecDetail);
//
//                         });
//                         OSpecProperty oSpecProperty = new OSpecProperty();
//                         oSpecProperty.setSpecGroupName("糖度");
//                         oSpecProperty.setLinkPhotoStatus(0);
//                         oSpecProperty.setSpecDetails(specDetails);
//                         specProperties.add(oSpecProperty);
//                     }
//                     if (null != temperatureList && temperatureList.size() > 0) {
//                         List<SpuAttr> spuAttrs = new ArrayList<>();
//                         temperatureList.stream().forEach(item -> {
//                             OSpecDetail oSpecDetail = new OSpecDetail();
//                             oSpecDetail.setSpecValue(item.getName());
//                             oSpecDetail.setSelectedStatus(0);
//                             specDetails.add(oSpecDetail);
//
//                         });
//                         OSpecProperty oSpecProperty = new OSpecProperty();
//                         oSpecProperty.setSpecGroupName("温度");
//                         oSpecProperty.setLinkPhotoStatus(0);
//                         oSpecProperty.setSpecDetails(specDetails);
//                         specProperties.add(oSpecProperty);
//                     }
//
//
//                     List<OMaterial> oMaterials = new ArrayList<>();
//                     // 查询配料
//                     LambdaQueryWrapper<GoodsAllBatch> wrapper3 = new LambdaQueryWrapper<>();
//                     wrapper3.eq(GoodsAllBatch::getStatus, 1).eq(GoodsAllBatch::getGoodsId, obj.getId());
//                     List<GoodsAllBatch> allBatchList = goodsAllBatchService.list(wrapper3);
//                     if (null != allBatchList && allBatchList.size() > 0) {
//                         List<Long> batchIds = allBatchList.stream().map(rr -> rr.getBatchId()).collect(Collectors.toList());
//                         LambdaQueryWrapper<Batching> query3 = new LambdaQueryWrapper<>();
//                         query3.eq(Batching::getStatus, 1).in(Batching::getId, batchIds);
//                         List<Batching> batchingList = batchingService.list(query3);
//                         if (null != batchingList && batchingList.size() > 0) {
//                             batchingList.stream().forEach(tt -> {
//                                 OMaterial oMaterial = new OMaterial();
//                                 oMaterial.setName(tt.getName());
//                                 oMaterials.add(oMaterial);
//                             });
//                         }
//                     }
//
//                     properties.put(OItemCreateProperty.mainMaterials, oMaterials); // 原材料
//                     properties.put(OItemCreateProperty.specs, oSpecs); // 规格
//                     Long cateId = obj.getCateId();
//                     Categorize categorize = categorizeService.getById(cateId);
//                     properties.put(OItemCreateProperty.stdCategoryId, categorize.getEleId()); // 类目id
//
//
//
//                     return R.ok();
//                 } else {
//                     return R.error();
//                 }
//
//             } else {
//
//                 return R.error();
//             }
//
//         }
//
//     }
//
//     @SneakyThrows
//     @ApiOperation(value = "修改美团的店铺商品")
//     @PostMapping("updateGoods")
//     public R updateGoods(@RequestBody ShopToGoods obj) {
//
//         ShopToGoods shopToGoods = new ShopToGoods();
//         LambdaQueryWrapper<ShopToGoods> wrapper = new LambdaQueryWrapper<>();
//         wrapper.eq(ShopToGoods::getStatus, 1).eq(ShopToGoods::getGoodsId, obj.getGoodsId()).eq(ShopToGoods::getShopId, obj.getShopId()).ne(ShopToGoods::getId, obj.getId());
//         List<ShopToGoods> list = shopToGoodsService.list(wrapper);
//         if (null != list && list.size() > 0) {
//             return R.ok().message("该商品已添加");
//         } else {
//             Token token = new Token();
//             token.setAccessToken(obj.getAccessToken());
//             token.setTokenType(obj.getTokenType());
//             token.setExpires(obj.getExpiresIn());
//             token.setRefreshToken(obj.getRefreshToken());
//
//             // 获取商品基本价格
//             // double goodsPrice = obj.getPrice().doubleValue();
//             double goodsPrice = 0.00;
//             ProductService productService = new ProductService(config, token);
//             Map<OItemUpdateProperty, Object> properties = new HashMap<OItemUpdateProperty, Object>();
//             properties.put(OItemUpdateProperty.name, obj.getName());
//             properties.put(OItemUpdateProperty.description, obj.getIntroduce());
//             // 上传图片获取hash值
//             String imagePath = weChatProperties.getUploadPath() + obj.getUrl();
//             byte[] fileContent = Files.readAllBytes(new File(imagePath).toPath());
//             String base64String = Base64.getEncoder().encodeToString(fileContent);
//             String imageHash = productService.uploadImage(base64String);
//             properties.put(OItemUpdateProperty.imageHash, imageHash);
//
//
//             // 获取规格参数
//             LambdaQueryWrapper<Param> queryParam = new LambdaQueryWrapper<Param>();
//             queryParam.eq(Param::getStatus, 1).eq(Param::getType, 1);
//             List<Param> paramList = paramService.list(queryParam);
//
//             LambdaQueryWrapper<Param> queryParam2 = new LambdaQueryWrapper<Param>();
//             queryParam2.eq(Param::getStatus, 1).eq(Param::getType, 2);
//             List<Param> sugarList = paramService.list(queryParam2);
//
//             LambdaQueryWrapper<Param> queryParam3 = new LambdaQueryWrapper<Param>();
//             queryParam3.eq(Param::getStatus, 1).eq(Param::getType, 3);
//             List<Param> temperatureList = paramService.list(queryParam3);
//
//             Integer count = (null != sugarList && sugarList.size() > 0 ? 1 : 0) + (null != temperatureList && temperatureList.size() > 0 ? 1 : 0);
//
//             List<OSpecProperty> specProperties = new ArrayList<>();
//             List<OSpecDetail> specDetails = new ArrayList<>();
//             List<OSpec> oSpecs = new ArrayList<>();
//             if (null != paramList && paramList.size() > 0) {
//
//                 paramList.stream().forEach(item -> {
//                     OSpecDetail oSpecDetail = new OSpecDetail();
//                     oSpecDetail.setSpecValue(item.getName());
//                     oSpecDetail.setSelectedStatus(0);
//                     specDetails.add(oSpecDetail);
//                     // 随机生成数字字符串
//                     Random random = new Random();
//                     int length = 10; // 生成的数字字符串长度
//                     StringBuilder sb = new StringBuilder(length);
//                     for (int i = 0; i < length; i++) {
//                         sb.append(random.nextInt(10)); // 生成0到9之间的随机数字并添加到StringBuilder中
//                     }
//                     String randomNumberString = sb.toString();
//                     String randomNumberString2 = "X" + String.valueOf(new Date().getTime());
//                     OSpec oSpec = new OSpec();
//                     // oSpec.setSpecId(72970000222L); // 创建不填写
//                     oSpec.setName(item.getName());
//                     oSpec.setPrice(goodsPrice + item.getAddPrice().doubleValue());
//                     oSpec.setPriceType("SUB_PRICE");
//                     oSpec.setPackageType("GROUP_CHOOSE");
//                     oSpec.setStock(900); // 库存量
//                     oSpec.setMaxStock(1000); // 最大库存量
//                     oSpec.setStockStatus(1); // 是否次日自动置满库存 0:不自动置满 或者 1: 次日自动置满
//                     oSpec.setPackingFee(0); // 打包费
//                     oSpec.setOnShelf(1); // 是否上架，1:上架，0:下架，不填写默认0
//                     oSpec.setExtendCode(randomNumberString); // 商品扩展码
//                     oSpec.setBarCode(randomNumberString2); // 商品条形码
//                     oSpec.setActivityLevel(0); // 商品活动信息，1:有活动，0:无活动
//                     OSpecAttribute oSpecAttribute = new OSpecAttribute(); // 规格扩展信息
//                     oSpecAttribute.setUnit("杯");
//                     oSpecAttribute.setWeight("1");
//                     oSpecAttribute.setPackageShare(1);
//                     oSpec.setSpecAttribute(oSpecAttribute); // 规格扩展信息
//                     if (count >= 0) {
//                         List<OItemAttribute> attributes = new ArrayList<>();
//                         if (null != sugarList && sugarList.size() > 0) {
//                             List<String> names = sugarList.stream().map(ee -> ee.getName()).collect(Collectors.toList());
//                             Long paramCateId = sugarList.get(0).getParamCateId();
//                             ParamCate paramCate = paramCateService.getById(paramCateId);
//
//                             OItemAttribute oItemAttribute = new OItemAttribute(); // 规格维度商品属性
//                             oItemAttribute.setName(paramCate.getName());
//                             oItemAttribute.setDetails(names);
//                             attributes.add(oItemAttribute);
//                             oSpec.setAttributes(attributes); // 规格维度商品属性
//                         }
//                         if (null != temperatureList && temperatureList.size() > 0) {
//                             List<String> names = temperatureList.stream().map(ee -> ee.getName()).collect(Collectors.toList());
//                             Long paramCateId = temperatureList.get(0).getParamCateId();
//                             ParamCate paramCate = paramCateService.getById(paramCateId);
//
//                             OItemAttribute oItemAttribute = new OItemAttribute(); // 规格维度商品属性
//                             oItemAttribute.setName(paramCate.getName());
//                             oItemAttribute.setDetails(names);
//                             attributes.add(oItemAttribute);
//                             oSpec.setAttributes(attributes); // 规格维度商品属性
//                         }
//
//                     }
//                     OSpecProperty oSpecProperty = new OSpecProperty();
//                     oSpecProperty.setSpecGroupName("规格");
//                     oSpecProperty.setLinkPhotoStatus(0);
//                     oSpecProperty.setSpecDetails(specDetails);
//                     specProperties.add(oSpecProperty);
//                     oSpecs.add(oSpec);
//
//                 });
//
//             }
//
//             // 获取除小料外的规格参数
//             if (null != sugarList && sugarList.size() > 0) {
//                 sugarList.stream().forEach(item -> {
//                     OSpecDetail oSpecDetail = new OSpecDetail();
//                     oSpecDetail.setSpecValue(item.getName());
//                     oSpecDetail.setSelectedStatus(0);
//                     specDetails.add(oSpecDetail);
//
//                 });
//                 OSpecProperty oSpecProperty = new OSpecProperty();
//                 oSpecProperty.setSpecGroupName("糖度");
//                 oSpecProperty.setLinkPhotoStatus(0);
//                 oSpecProperty.setSpecDetails(specDetails);
//                 specProperties.add(oSpecProperty);
//             }
//             if (null != temperatureList && temperatureList.size() > 0) {
//                 temperatureList.stream().forEach(item -> {
//                     OSpecDetail oSpecDetail = new OSpecDetail();
//                     oSpecDetail.setSpecValue(item.getName());
//                     oSpecDetail.setSelectedStatus(0);
//                     specDetails.add(oSpecDetail);
//
//                 });
//                 OSpecProperty oSpecProperty = new OSpecProperty();
//                 oSpecProperty.setSpecGroupName("温度");
//                 oSpecProperty.setLinkPhotoStatus(0);
//                 oSpecProperty.setSpecDetails(specDetails);
//                 specProperties.add(oSpecProperty);
//             }
//             List<OMaterial> oMaterials = new ArrayList<>();
//             // 查询配料
//             LambdaQueryWrapper<GoodsAllBatch> wrapper3 = new LambdaQueryWrapper<>();
//             wrapper3.eq(GoodsAllBatch::getStatus, 1).eq(GoodsAllBatch::getGoodsId, obj.getId());
//             List<GoodsAllBatch> allBatchList = goodsAllBatchService.list(wrapper3);
//             if (null != allBatchList && allBatchList.size() > 0) {
//                 List<Long> batchIds = allBatchList.stream().map(rr -> rr.getBatchId()).collect(Collectors.toList());
//                 LambdaQueryWrapper<Batching> query3 = new LambdaQueryWrapper<>();
//                 query3.eq(Batching::getStatus, 1).in(Batching::getId, batchIds);
//                 List<Batching> batchingList = batchingService.list(query3);
//                 if (null != batchingList && batchingList.size() > 0) {
//                     batchingList.stream().forEach(tt -> {
//                         OMaterial oMaterial = new OMaterial();
//                         oMaterial.setName(tt.getName());
//                         oMaterials.add(oMaterial);
//                     });
//                 }
//             }
//
//             properties.put(OItemUpdateProperty.mainMaterials, oMaterials); // 原材料
//             properties.put(OItemUpdateProperty.specs, oSpecs); // 规格
//             Long cateId = obj.getCateId();
//             Categorize categorize = categorizeService.getById(cateId);
//             properties.put(OItemUpdateProperty.stdCategoryId, categorize.getEleId()); // 类目id
//
//             OItem item = productService.updateItem(obj.getEleId(), categorize.getEleId(), properties);
//             if (null != item) {
//                 BeanUtils.copyProperties(obj, shopToGoods);
//                 shopToGoods.setCreateTime(LocalDateTime.now());
//                 shopToGoods.setUpdateTime(LocalDateTime.now());
//                 shopToGoods.setEleId(item.getId());
//                 shopToGoods.setImageHash(imageHash);
//                 boolean save = shopToGoodsService.updateById(shopToGoods);
//                 if (save) {
//                     final Integer[] count2 = {0};
//                     LambdaQueryWrapper<GoodsToBatch> queryWrapper = new LambdaQueryWrapper<>();
//                     queryWrapper.eq(GoodsToBatch::getStatus, 1).eq(GoodsToBatch::getGoodsId, shopToGoods.getGoodsId()).eq(GoodsToBatch::getShopId, shopToGoods.getShopId());
//                     boolean remove = goodsToBatchService.remove(queryWrapper);
//
//                     if (remove) {
//                         LambdaQueryWrapper<GoodsAllBatch> queryWrapper2 = new LambdaQueryWrapper<>();
//                         queryWrapper2.eq(GoodsAllBatch::getStatus, 1).eq(GoodsAllBatch::getGoodsId, obj.getGoodsId());
//                         List<GoodsAllBatch> allBatches = goodsAllBatchService.list(queryWrapper2);
//                         allBatches.stream().forEach(aa -> {
//                             GoodsToBatch goodsToBatch = new GoodsToBatch();
//                             goodsToBatch.setBatchId(aa.getBatchId());
//                             goodsToBatch.setUseNumber(aa.getUseNumber());
//                             goodsToBatch.setGoodsId(shopToGoods.getId());
//                             goodsToBatch.setTemperatureId(aa.getTemperatureId());
//                             goodsToBatch.setSugarId(aa.getSugarId());
//                             goodsToBatch.setSizeId(aa.getSizeId());
//                             goodsToBatch.setCreateTime(LocalDateTime.now());
//                             goodsToBatch.setUpdateTime(LocalDateTime.now());
//                             goodsToBatch.setShopId(obj.getShopId());
//                             goodsToBatch.setPrice(shopToGoods.getPrice());
//                             goodsToBatch.setCost(shopToGoods.getCost());
//                             boolean b = goodsToBatchService.save(goodsToBatch);
//                             if (b) {
//                                 count2[0] = count2[0] + 1;
//                             }
//                         });
//                         // if (count2[0] == allBatches.size()) {
//                         //     return R.ok();
//                         // }else {
//                         //     return R.error();
//                         // }
//
//                     }
//                     Shop shop = shopsService.getById(obj.getShopId());
//                     InterfaceLog interfaceLog = new InterfaceLog();
//                     interfaceLog.setTitle("修改美团商品");
//                     interfaceLog.setMethodName("updateGoods");
//                     String content = "商户"+shop.getName()+"修改美团商品，商品名为"+obj.getName();
//                     interfaceLog.setContent(content);
//                     interfaceLog.setShopId(obj.getShopId());
//                     interfaceLog.setTypeStatus(0);
//                     interfaceLog.setCreateTime(LocalDateTime.now());
//                     interfaceLog.setUpdateTime(LocalDateTime.now());
//                     interfaceLogService.save(interfaceLog);
//                     return R.ok();
//                 } else {
//
//                     return R.error();
//                 }
//             } else {
//                 return R.error();
//             }
//
//
//         }
//
//     }
//
//     @SneakyThrows
//     @ApiOperation(value = "删除美团的店铺商品")
//     @DeleteMapping("deleteEleGoods/{id}")
//     public R invalidGoods(@PathVariable("id") Long id, @RequestParam("access_token") String access_token, @RequestParam("token_type") String token_type,
//                           @RequestParam("expires_in") String expires_in, @RequestParam("refresh_token") String refresh_token) {
//         ShopToGoods byId = shopToGoodsService.getById(id);
//         if (null != byId.getEleId()) {
//             Token token = new Token();
//             token.setAccessToken(access_token);
//             token.setTokenType(token_type);
//             token.setExpires(Long.valueOf(expires_in));
//             token.setRefreshToken(refresh_token);
//
//             // 删除饿了么店铺商品
//             ProductService productService = new ProductService(config, token);
//             productService.invalidItem(byId.getEleId());
//         }
//
//
//         // 删除库里商品
//
//         boolean remove = shopToGoodsService.removeById(id);
//         if (remove) {
//             Shop shop = shopsService.getById(byId.getShopId());
//             InterfaceLog interfaceLog = new InterfaceLog();
//             interfaceLog.setTitle("删除美团商品");
//             interfaceLog.setMethodName("deleteEleGoods");
//             String content = "商户"+shop.getName()+"删除美团商品，商品名为"+byId.getName();
//             interfaceLog.setContent(content);
//             interfaceLog.setShopId(byId.getShopId());
//             interfaceLog.setTypeStatus(0);
//             interfaceLog.setCreateTime(LocalDateTime.now());
//             interfaceLog.setUpdateTime(LocalDateTime.now());
//             interfaceLogService.save(interfaceLog);
//             return R.ok();
//         } else {
//             return R.error();
//         }
//
//
//     }
//
// }
