// package com.tea.server.config;
//
// import com.alibaba.fastjson.JSONObject;
// import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
// import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
// import lombok.Data;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.http.HttpEntity;
// import org.apache.http.HttpResponse;
// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.entity.StringEntity;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.apache.http.util.EntityUtils;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.PropertySource;
//
// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
// import java.security.PrivateKey;
// import java.security.Signature;
// import java.security.cert.X509Certificate;
// import java.util.ArrayList;
// import java.util.Base64;
//
// @Configuration
// @PropertySource("classpath:wxpay.properties") //读取配置文件
// @ConfigurationProperties(prefix="wxpay") //读取wxpay节点
// @Data //使用set方法将wxpay节点中的值填充到当前类的属性中
// @Slf4j
// public class WxPayUtil {
//     // 商户号
//     private String mchId;
//     // 商户API证书序列号
//     private String mchSerialNo;
//
//     // 商户私钥文件
//     private String privateKeyPath;
//
//     // APIv3密钥
//     private String apiV3Key;
//
//     //平台证书路径
//     private String payCertificateUrl;
//
//     // APPID
//     private String appid;
//
//     // 微信服务器地址，这个字段没有在，本文中使用到可以不用管
//     private String domain;
//
//     //小程序密匙
//     private String appSecret;
//
//     private CloseableHttpClient httpClient;
//
//     public void setup()  {
//         //   PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(privateKey);
//         PrivateKey merchantPrivateKey = null;
//         X509Certificate wechatPayCertificate = null;
//
//         try {
//             merchantPrivateKey = PemUtil.loadPrivateKey(
//                     new FileInputStream(privateKeyPath));
//             wechatPayCertificate = PemUtil.loadCertificate(
//                     new FileInputStream(payCertificateUrl));
//
//         } catch (FileNotFoundException e) {
//             e.printStackTrace();
//         }
//
//         ArrayList<X509Certificate> listCertificates = new ArrayList<>();
//         listCertificates.add(wechatPayCertificate);
//
//         WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
//                 .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
//                 .withWechatPay(listCertificates);
//         httpClient = builder.build();
//     }
//
//     /**
//      * wxMchid商户号
//      * wxCertno证书编号
//      * wxCertPath证书地址
//      * wxPaternerKey   v3秘钥
//      * url 下单地址
//      * body 构造好的消息体
//      */
//     public JSONObject doPostWexinV3(String url, String body) {
//         if (httpClient == null) {
//             setup();
//         }
//
//         HttpPost httpPost = new HttpPost(url);
//         httpPost.addHeader("Content-Type", "application/json;chartset=utf-8");
//         httpPost.addHeader("Accept", "application/json");
//         try {
//             if (body == null) {
//                 throw new IllegalArgumentException("data参数不能为空");
//             }
//             StringEntity stringEntity = new StringEntity(body, "utf-8");
//             httpPost.setEntity(stringEntity);
//             // 直接执行execute方法，官方会自动处理签名和验签，并进行证书自动更新
//             HttpResponse httpResponse = httpClient.execute(httpPost);
//             HttpEntity httpEntity = httpResponse.getEntity();
//
//             if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                 String jsonResult = EntityUtils.toString(httpEntity);
//                 return JSONObject.parseObject(jsonResult);
//             } else {
//                 System.err.println("微信支付错误信息" + EntityUtils.toString(httpEntity));
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return null;
//
//     }
//
//     //获取签名
//     public String getSign(String appId, long timestamp, String nonceStr, String pack){
//         String message = buildMessage(appId, timestamp, nonceStr, pack);
//         String paySign= null;
//         try {
//             paySign = sign(message.getBytes("utf-8"));
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return paySign;
//     }
//
//     private String buildMessage(String appId, long timestamp, String nonceStr, String pack) {
//         return appId + "\n"
//                 + timestamp + "\n"
//                 + nonceStr + "\n"
//                 + pack + "\n";
//     }
//     private String sign(byte[] message) throws Exception{
//         PrivateKey merchantPrivateKey = null;
//         X509Certificate wechatPayCertificate = null;
//
//         try {
//             merchantPrivateKey = PemUtil.loadPrivateKey(
//                     new FileInputStream(privateKeyPath));
//             wechatPayCertificate = PemUtil.loadCertificate(
//                     new FileInputStream(payCertificateUrl));
//
//         } catch (FileNotFoundException e) {
//             e.printStackTrace();
//         }
//         Signature sign = Signature.getInstance("SHA256withRSA");
//         //这里需要一个PrivateKey类型的参数，就是商户的私钥。
//         sign.initSign(merchantPrivateKey);
//         sign.update(message);
//         return Base64.getEncoder().encodeToString(sign.sign());
//     }
//
// }
