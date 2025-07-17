package com.tea.server.controller;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.binarywang.wxpay.bean.profitsharingV3.ProfitSharingReceiver;
import com.github.binarywang.wxpay.bean.profitsharingV3.ProfitSharingRequest;
import com.github.binarywang.wxpay.bean.profitsharingV3.ProfitSharingResult;
import com.github.binarywang.wxpay.service.ProfitSharingV3Service;
import com.google.gson.Gson;
import com.tea.server.config.WxPayConfig;
import com.tea.server.entity.Agents;
import com.tea.server.entity.Orders;
import com.tea.server.entity.ReceiveCoupon;
import com.tea.server.entity.Shop;
import com.tea.server.entity.fz.*;
import com.tea.server.service.*;
import com.tea.server.utils.R;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

/**
 * 支付接口
 */
@Slf4j
@RestController
@RequestMapping("pay")
public class PaymentController {


    @Resource
    private WxPayConfig wxPayConfig;

    @Resource
    private CloseableHttpClient wxPayClient;

    @Resource
    private Verifier verifier;

    @Autowired
    private ShopService shopService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private AgentsService agentsService;

    @Autowired
    private ShopToGoodsService shopToGoodsService;

    @Autowired
    private ReceiveCouponService receiveCouponService;

    @RequestMapping("payment/{shopId}/{orderId}")
    public R doOrder(HttpServletRequest request, @PathVariable("shopId") Long shopId, @PathVariable("orderId") Long orderId, String openid, Integer totalfee, Long couponId) throws Exception {



        //得到价钱（单位是分）

        // int fee = totalfee;//单位是分
        int fee = 1;

                //得到商品的ID（自定义）
        // String goodsid=request.getParameter("goodsid");
        //订单标题（自定义）
        String title = "支付商品";
        //时间戳，
        String times = System.currentTimeMillis() + "";

        //订单编号（自定义 这里以时间戳+随机数）
        Random random = new Random();
        String did = times + random.nextInt(1000);

        log.info("生成订单");
        //调用统一下单API
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        // 请求body参数
        Gson gson = new Gson();
        HashMap<Object, Object> paramsMap = new HashMap<>();
        paramsMap.put("appid", wxPayConfig.getAppid());//appid
        paramsMap.put("mchid", wxPayConfig.getMchId());//商户号
        paramsMap.put("description", title);//商品描述
        paramsMap.put("out_trade_no", did);//商户订单号
        paramsMap.put("notify_url", "https://tea.yinghai-tec.com/tea-server/pay/notify");//通知地址，可随便写，如果不需要通知的话，不影响支付，但是影响后续修改订单状态

        Orders orders = ordersService.getById(orderId);
        Orders orders1 = new Orders();
        BeanUtils.copyProperties(orders, orders1);
        orders1.setCouponId(couponId);
        orders1.setOutTradeNo(did);
        ordersService.updateById(orders1);

        // 查询该订单的商户是否分账
        Shop shop = shopService.getById(shopId);
        if (shop.getDivideAccounts() == 1){
            // 分账
            //订单金额
            HashMap<Object, Object> amountMap = new HashMap<>();
            amountMap.put("profit_sharing", true);
            paramsMap.put("settle_info", amountMap);

        }

        // 扣除优惠券
        if (null != couponId && couponId > 0){
            ReceiveCoupon receiveCoupon = receiveCouponService.getById(couponId);
            ReceiveCoupon receiveCoupon1 = new ReceiveCoupon();
            BeanUtils.copyProperties(receiveCoupon, receiveCoupon1);
            receiveCoupon1.setIsUsed(1);
            receiveCoupon1.setUpdateTime(LocalDateTime.now());
            receiveCouponService.updateById(receiveCoupon1);
        }




        //订单金额
        HashMap<Object, Object> amountMap = new HashMap<>();
        amountMap.put("total", fee);//金额
        amountMap.put("currency", "CNY");//货币类型
        paramsMap.put("amount", amountMap);

        //支付者
        HashMap<Object, Object> playerMap = new HashMap<>();
        playerMap.put("openid", openid);

        paramsMap.put("payer", playerMap);

        //将参数转化未json字符串
        String jsonParamsMap = gson.toJson(paramsMap);
        log.info("请求参数：" + jsonParamsMap);

        StringEntity entity = new StringEntity(jsonParamsMap, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse resp = wxPayClient.execute(httpPost);

        try {
            int statusCode = resp.getStatusLine().getStatusCode();
            String bodyAsString = EntityUtils.toString(resp.getEntity());
            if (statusCode == 200) { //处理成功
                log.info("成功，返回结果 = " + bodyAsString);
            } else if (statusCode == 204) { //处理成功，无返回Body
                log.info("成功");
            } else {
                System.out.println("小程序下单失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }
            //相应结果
            HashMap<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);


            //获取prepay—id
            String prepayId = resultMap.get("prepay_id");


            //获取到perpayid之后需要对数据进行二次封装，前端调起支付必须存在的参数
            HashMap<String, String> payMap = new HashMap<>();
            payMap.put("appid", wxPayConfig.getAppid());//appid
            long currentTimestamp = System.currentTimeMillis();//时间戳，别管那么多，他就是需要
            payMap.put("timeStamp", currentTimestamp + "");
            String nonceStr = UUID.randomUUID().toString()
                    .replaceAll("-", "")
                    .substring(0, 32);
            ;//随机字符串，别管那么多他就是需要，要咱就给
            payMap.put("nonceStr", nonceStr);
            //apiv3只支持这种加密方式
            payMap.put("signType", "RSA");

            payMap.put("package", "prepay_id=" + prepayId);

            //通过appid，timeStamp，nonceStr，signType，package以及商户密钥进行key=value形式进行拼接加密
            //加密方法我会放在这个代码段段下面
            String aPackage = wxPayConfig.buildMessageTwo(wxPayConfig.getAppid(), currentTimestamp, nonceStr, payMap.get("package"));

            //获取对应的签名
            //加密方法我会放在这个代码段段下面
            String paySign = wxPayConfig.sign(aPackage.getBytes("utf-8"));
            // String paySign = wxPayConfig.sign(wxPayConfig.getPrivateKeyPath(),aPackage.getBytes("utf-8"));

            payMap.put("paySign", paySign);

            /**
             *	在这里你可以加入自己的数据库操作，存储一条订单信息，状态为未支付就行了
             *	在这里你可以加入自己的数据库操作，存储一条订单信息，状态为未支付就行了
             *	在这里你可以加入自己的数据库操作，存储一条订单信息，状态为未支付就行了
             */

            log.info("给前端的玩意：" + payMap);//前端会根据这些参数调起支付页面
            //到这里，就已经完成了官网图中的第8步了
            return R.ok().data("paySign", payMap);

        } finally {
            resp.close();
        }
    }

    /**
     *
     * @description: 获取微信退款证书
     * @param mch_id
     * @auther: sean.xu
     * @date: 2019/8/13 15:11
     */
    private KeyStore getCertificate(String mch_id){
        //try-with-resources 关流
        try (FileInputStream inputStream = new FileInputStream(new File(wxPayConfig.getPrivateCert()))) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, mch_id.toCharArray());
            return keyStore;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @RequestMapping("/notify")
    public void wxCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.info("微信支付回调");
        log.info("参数："+request);

        //获取报文
        String body = getRequestBody(request);
        log.info("获取报文："+body);

        //随机串
        String nonce = request.getHeader("Wechatpay-Nonce");

        //微信传递过来的签名
        String signature = request.getHeader("Wechatpay-Signature");

        //证书序列号（微信平台）
        String wechatPaySerial = request.getHeader("Wechatpay-Serial");

        //时间戳
        String timestamp = request.getHeader("Wechatpay-Timestamp");

        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(wxPayConfig.getPrivateKeyPath()));

        // PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(WxPayConfig.privateKey);
        // 私钥签名对象
        PrivateKeySigner keySigner = new PrivateKeySigner(wxPayConfig.getMchSerialNo(), merchantPrivateKey);
        // 身份认证对象
        WechatPay2Credentials wechatPay2Credentials = new WechatPay2Credentials(wxPayConfig.getMchId(), keySigner);

        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息
        certificatesManager.putMerchant(wxPayConfig.getMchId(), wechatPay2Credentials,
                wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        // ... 若有多个商户号，可继续调用putMerchant添加商户信息
        Verifier verifier = certificatesManager.getVerifier(wxPayConfig.getMchId());
        // // 获取证书管理器实例
        // CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // // 向证书管理器增加需要自动更新平台证书的商户信息
        // certificatesManager.putMerchant(wxPayConfig.getMchId(), new WechatPay2Credentials(WxPayUtil.mchId,
        //                 new PrivateKeySigner(WxPayUtil.mchSerialNo, merchantPrivateKey)),
        //         WxPayUtil.apiV3Key.getBytes(StandardCharsets.UTF_8));
        // // 从证书管理器中获取verifier
        // Verifier verifier = certificatesManager.getVerifier(WxPayUtil.mchId);

        // 构建request，传入必要参数
        NotificationRequest notificationRequest = new NotificationRequest.Builder().withSerialNumber(wechatPaySerial)
                .withNonce(nonce)
                .withTimestamp(timestamp)
                .withSignature(signature)
                .withBody(body)
                .build();
        NotificationHandler handler = new NotificationHandler(verifier, wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        // 验签和解析请求体
        Notification notification = handler.parse(notificationRequest);
        Assert.assertNotNull(notification);
        String result = notification.getDecryptData();
        log.info("解密报文："+result);
        JSONObject resultJson = JSON.parseObject(result);
        String trade_state = resultJson.getString("trade_state").trim();
        String out_trade_no = resultJson.getString("out_trade_no").trim();
        String trade_type = resultJson.getString("trade_type").trim();
        String transaction_id = resultJson.getString("transaction_id").trim();
        Amount amount = resultJson.getObject("amount", Amount.class);
        Payer payer = resultJson.getObject("payer", Payer.class);
        log.info("resultJson:"+resultJson);
        log.info("微信支付交易状态码:"+trade_state);
        log.info("微信支付交易订单号:"+out_trade_no);
        log.info("微信支付交易类型:"+trade_type);
        log.info("微信支付交易金额:"+amount.getTotal());

        if("SUCCESS".equals(trade_state)){
            //支付成功，你的业务逻辑
            log.info("支付成功");
            List<FzUser> userList = new ArrayList<>();
            LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Orders :: getStatus, 1).eq(Orders :: getOpenid, payer.getOpenid()).eq(Orders :: getOutTradeNo, out_trade_no);
            Orders one = ordersService.getOne(wrapper);
            Shop shop = shopService.getById(one.getShopId());


             if (null != shop){
                String openid = shop.getOpenid();
                // 添加分帐方
                String addJSon = wxPayConfig.profitSharingAdd(openid, "");
                 long price = amount.getTotal() * shop.getProportion() / 100;
                 FzUser fzUser1 = new FzUser();
                 fzUser1.setOpenid(shop.getOpenid());
                 fzUser1.setTotal(price);
                 userList.add(fzUser1);
             }
            List<Long> ids = new ArrayList<>();
            ids.add(shop.getId());
            LambdaQueryWrapper<Agents> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Agents :: getStatus, 1).in(Agents :: getShopId, ids).eq(Agents :: getIsUse, 1);
            Agents agentsServiceOne = agentsService.getOne(queryWrapper);
             if (null != agentsServiceOne){
                // 添加分帐方
                String addJSon2 = wxPayConfig.profitSharingAdd(agentsServiceOne.getOpenid(), "");
                 long price2 = amount.getTotal() * agentsServiceOne.getProportion() / 100;
                 FzUser fzUser2 = new FzUser();
                 fzUser2.setOpenid(agentsServiceOne.getOpenid());
                 fzUser2.setTotal(price2);
                 userList.add(fzUser2);
             }

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

            Runnable task = () -> {
                // 这里写你需要执行的任务
                // 请求分账
                String proJson = null;

                try {
                    proJson = wxPayConfig.profitSharingApply(transaction_id, out_trade_no, userList);
                    log.info("请求分账22222:"+proJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            };

            // 设置任务在1分钟后执行
            long delay = 1;
            executor.schedule(task, delay, TimeUnit.MINUTES);

        }

    }

    private String getRequestBody(HttpServletRequest request) {

        StringBuffer sb = new StringBuffer();

        try (ServletInputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            log.error("读取数据流异常:{}", e);
        }

        return sb.toString();

    }


// @RequestMapping("payment")
    // public Object getpayment(HttpServletRequest request, String openid, BigDecimal totalfee, String tradeno) throws Exception {
    //     // 获取当前登陆的用户信息
    //0o9pp=-]
    //     //当前就是我们自己配置的支付配置。appid 商户号 key 什么的；
    //     MyPayConfig config = new MyPayConfig();
    //     //当前类是官方为我们封装的一些使用的方法
    //     WXPay wxpay = new WXPay(config);
    //     //获取到 IP
    //     String clientIp = getIpAddress(request);
    //
    //     log.info(clientIp);
    //     //封装请求参数 参数说明看API文档，当前就不进行讲解了
    //     Map<String, String> data = new HashMap<String, String>();
    //     data.put("body", "腾讯充值中心-QQ会员充值");
    //     data.put("out_trade_no", tradeno);
    //     // data.put("device_info", "12345679"); //此处设备或商品编号
    //     data.put("fee_type", "CNY");  // 货币类型  人民币
    //
    //     // 支付中没有小数点，起步以分做为单们，当前为1 分钱，所以自行调整金额 ，这里可以做为传参，
    //     //选取商品金额传到后端来
    //     data.put("total_fee", String.valueOf(totalfee));
    //
    //     data.put("spbill_create_ip", clientIp);
    //     data.put("notify_url", "http://www.example.com/wxpay/notify");
    //     data.put("trade_type", "JSAPI");  // 此处指定JSAPI
    //     // data.put("product_id", "12");
    //     data.put("openid", openid);
    //     //调用统一下单方法
    //     Map<String, String> order = wxpay.unifiedOrder(data);
    //     //获取到需要的参数返回小程序
    //     return R.ok().data("prepay_id", order);
    //
    // }
    //
    // // 获取IP
    // private String getIpAddress(HttpServletRequest request) {
    //     String ip = request.getHeader("x-forwarded-for");
    //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //         ip = request.getHeader("Proxy-Client-IP");
    //     }
    //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //         ip = request.getHeader("WL-Proxy-Client-IP");
    //     }
    //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //         ip = request.getHeader("HTTP_CLIENT_IP");
    //     }
    //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //         ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    //     }
    //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    //         ip = request.getRemoteAddr();
    //     }
    //     return ip;
    //
    // }
}
