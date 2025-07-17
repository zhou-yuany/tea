// package com.tea.server.controller;
//
// import com.alibaba.fastjson.JSON;
// import com.alibaba.fastjson.JSONObject;
//
// import com.tea.server.config.WxPayUtil;
// import com.tea.server.utils.R;
// import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
// import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
// import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
// import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
// import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
// import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
// import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
// import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
// import lombok.extern.slf4j.Slf4j;
//
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.springframework.util.Assert;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import javax.annotation.Resource;
// import javax.servlet.ServletInputStream;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.BufferedReader;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.nio.charset.StandardCharsets;
// import java.security.PrivateKey;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Random;
// import java.util.UUID;
//
// /**
//  * 支付接口
//  */
// @Slf4j
// @RestController
// @RequestMapping("pay")
// public class PaymentController111111 {
//
//
//
//     @Resource
//     private WxPayUtil wxPayUtil;
//
//     @RequestMapping("payment")
//     public R doOrder(HttpServletRequest request, Integer totalfee) throws Exception{
//
//         //得到价钱（单位是分）
//
//         int fee = totalfee;//单位是分
//
//         //得到商品的ID（自定义）
//         // String goodsid=request.getParameter("goodsid");
//         //订单标题（自定义）
//         String title = "支付商品";
//         //时间戳，
//         String times = System.currentTimeMillis() + "";
//
//         //订单编号（自定义 这里以时间戳+随机数）
//         Random random = new Random();
//         String did = times+random.nextInt(1000);
//
//
//
//         JSONObject amountJson = new JSONObject();
//         amountJson.put("total",fee);
//         amountJson.put("currency","CNY");
//
//         //基础信息json
//         JSONObject json = new JSONObject();
//         //  json.put("appid","wx043e87691cbad09f");
//         json.put("appid", wxPayUtil.getAppid());
//         json.put("mchid",wxPayUtil.getMchId());
//         json.put("description",title);
//         json.put("out_trade_no",did);
//         json.put("notify_url","https://tea.yinghai-tec.com/pay");
//         json.put("amount",amountJson);
//         log.info("上述正确。。。。。。。。。。。。。。。。。");
//         JSONObject jsonObject1 = wxPayUtil.doPostWexinV3("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi", json.toJSONString());
//         log.info("结果："+jsonObject1.toJSONString());
//         String prepay_id = jsonObject1.getString("prepay_id");
//
//         //时间戳
//         Long timestamp = System.currentTimeMillis()/1000;
//         //随机串
//         String nonceStr = UUID.randomUUID().toString().replace("-","");
//
//         String sign = wxPayUtil.getSign(wxPayUtil.getAppid(),timestamp,nonceStr,prepay_id);
//         log.info("签名:"+sign);
//
//         Map payMap = new HashMap();
//         payMap.put("prepayid",prepay_id);
//         payMap.put("timestamp",timestamp+"");
//         payMap.put("noncestr",nonceStr);
//         payMap.put("sign",sign);
//         payMap.put("appid",wxPayUtil.getAppid());
//         payMap.put("package","Sign=WXPay");
//         payMap.put("extData","sign");
//         payMap.put("partnerid",wxPayUtil.getMchId());
//
//         return R.ok().data("payMap", payMap);
//
//
//     }
//
//
//     public void wxCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//         log.info("微信支付回调");
//
//         //获取报文
//         String body = getRequestBody(request);
//
//         //随机串
//         String nonce = request.getHeader("Wechatpay-Nonce");
//
//         //微信传递过来的签名
//         String signature = request.getHeader("Wechatpay-Signature");
//
//         //证书序列号（微信平台）
//         String wechatPaySerial = request.getHeader("Wechatpay-Serial");
//
//         //时间戳
//         String timestamp = request.getHeader("Wechatpay-Timestamp");
//
//         PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(wxPayUtil.getPrivateKeyPath()));
//         // 获取证书管理器实例
//         CertificatesManager certificatesManager = CertificatesManager.getInstance();
//         // 向证书管理器增加需要自动更新平台证书的商户信息
//         certificatesManager.putMerchant(wxPayUtil.getMchId(), new WechatPay2Credentials(wxPayUtil.getMchId(),
//                         new PrivateKeySigner(wxPayUtil.getMchSerialNo(), merchantPrivateKey)),
//                 wxPayUtil.getApiV3Key().getBytes(StandardCharsets.UTF_8));
//         // 从证书管理器中获取verifier
//         Verifier verifier = certificatesManager.getVerifier(wxPayUtil.getMchId());
//
//         // 构建request，传入必要参数
//         NotificationRequest notificationRequest = new NotificationRequest.Builder().withSerialNumber(wechatPaySerial)
//                 .withNonce(nonce)
//                 .withTimestamp(timestamp)
//                 .withSignature(signature)
//                 .withBody(body)
//                 .build();
//         NotificationHandler handler = new NotificationHandler(verifier, wxPayUtil.getApiV3Key().getBytes(StandardCharsets.UTF_8));
//         // 验签和解析请求体
//         Notification notification = handler.parse(notificationRequest);
//         Assert.notNull(notification, "空");
//         String result = notification.getDecryptData();
//         log.info("解密报文："+result);
//         com.alibaba.fastjson.JSONObject resultJson = JSON.parseObject(result);
//         String trade_state = resultJson.getString("trade_state").trim();
//         String out_trade_no = resultJson.getString("out_trade_no").trim();
//         String trade_type = resultJson.getString("trade_type").trim();
//         log.info("微信支付交易状态码:"+trade_state);
//         log.info("微信支付交易订单号:"+out_trade_no);
//         log.info("微信支付交易类型:"+trade_type);
//
//         if("SUCCESS".equals(trade_state)){
//             //支付成功，你的业务逻辑
//
//         }
//
//     }
//
//
//     private String getRequestBody(HttpServletRequest request) {
//
//         StringBuffer sb = new StringBuffer();
//
//         try (ServletInputStream inputStream = request.getInputStream();
//              BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//         ) {
//             String line;
//
//             while ((line = reader.readLine()) != null) {
//                 sb.append(line);
//             }
//
//         } catch (IOException e) {
//             log.error("读取数据流异常:{}", e);
//         }
//
//         return sb.toString();
//
//     }
//
//
//
//
// // @RequestMapping("payment")
//     // public Object getpayment(HttpServletRequest request, String openid, BigDecimal totalfee, String tradeno) throws Exception {
//     //     // 获取当前登陆的用户信息
//     //0o9pp=-]
//     //     //当前就是我们自己配置的支付配置。appid 商户号 key 什么的；
//     //     MyPayConfig config = new MyPayConfig();
//     //     //当前类是官方为我们封装的一些使用的方法
//     //     WXPay wxpay = new WXPay(config);
//     //     //获取到 IP
//     //     String clientIp = getIpAddress(request);
//     //
//     //     log.info(clientIp);
//     //     //封装请求参数 参数说明看API文档，当前就不进行讲解了
//     //     Map<String, String> data = new HashMap<String, String>();
//     //     data.put("body", "腾讯充值中心-QQ会员充值");
//     //     data.put("out_trade_no", tradeno);
//     //     // data.put("device_info", "12345679"); //此处设备或商品编号
//     //     data.put("fee_type", "CNY");  // 货币类型  人民币
//     //
//     //     // 支付中没有小数点，起步以分做为单们，当前为1 分钱，所以自行调整金额 ，这里可以做为传参，
//     //     //选取商品金额传到后端来
//     //     data.put("total_fee", String.valueOf(totalfee));
//     //
//     //     data.put("spbill_create_ip", clientIp);
//     //     data.put("notify_url", "http://www.example.com/wxpay/notify");
//     //     data.put("trade_type", "JSAPI");  // 此处指定JSAPI
//     //     // data.put("product_id", "12");
//     //     data.put("openid", openid);
//     //     //调用统一下单方法
//     //     Map<String, String> order = wxpay.unifiedOrder(data);
//     //     //获取到需要的参数返回小程序
//     //     return R.ok().data("prepay_id", order);
//     //
//     // }
//     //
//     // // 获取IP
//     // private String getIpAddress(HttpServletRequest request) {
//     //     String ip = request.getHeader("x-forwarded-for");
//     //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//     //         ip = request.getHeader("Proxy-Client-IP");
//     //     }
//     //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//     //         ip = request.getHeader("WL-Proxy-Client-IP");
//     //     }
//     //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//     //         ip = request.getHeader("HTTP_CLIENT_IP");
//     //     }
//     //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//     //         ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//     //     }
//     //     if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//     //         ip = request.getRemoteAddr();
//     //     }
//     //     return ip;
//     //
//     // }
// }
