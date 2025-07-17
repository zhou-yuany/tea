package com.tea.server.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.ecommerce.SignatureHeader;
import com.github.binarywang.wxpay.bean.profitsharingV3.*;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.ProfitSharingV3Service;
import com.google.gson.Gson;
import com.google.zxing.NotFoundException;
import com.tea.server.entity.fz.FzUser;
import com.tea.server.entity.fz.Receivers;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.*;

@Configuration
@PropertySource("classpath:wxpay.properties") //读取配置文件
@ConfigurationProperties(prefix="wxpay") //读取wxpay节点
@Data //使用set方法将wxpay节点中的值填充到当前类的属性中
@Slf4j
public class WxPayConfig {

    // 商户号
    private String mchId;

    // 商户API证书序列号
    private String mchSerialNo;

    // 证书文件
    private String privateCert;

    // 商户私钥文件
    private String privateKeyPath;

    // APIv3密钥
    private String apiV3Key;

    // APPID
    private String appid;

    // 微信服务器地址，这个字段没有在，本文中使用到可以不用管
    private String domain;

    // APIv2密钥
    // private String partnerKey;

    //小程序密匙
    private String appSecret;


    public static String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQD26mSczrtphroe\n" +
            "udzjR+XYY3zP/ZwDBzM0EC7r34DymNI0L4Q0BnuxcU90cT/wjmqO4avtwf2/+Eg+\n" +
            "3NIhAb37jv865M/nC5k4wia3I1iQRdCapKyltNjaoROz3ibnxBn1j+bjDZoWn1YV\n" +
            "QQYxIG+IiM4yUFEMc31LB45FT6ViyZmT7p/yBWDtXtEOX1LY2GNHWivV/NHyrg3i\n" +
            "gR1jAtwHqJKn7IkzKDpOtkDbWBJCYbCRayK/Uote2UUumyhkIVvHLr63kWvAc2Df\n" +
            "p1K/oNnEtEa1/Q1lcWTwQELiRGUkV38pORLNq3H6ceLgHsfdwA0Lzy0cQtlqQ5HC\n" +
            "nOo7xvq7AgMBAAECggEBAN/QFUUiCzUbP2gMl5GZIVmkzIUH4DvKcfpWVQHPhWXh\n" +
            "amCNVauG2OY9zITDBu85WJONMbgBKM6y8mGw7fOnTZtRwj6TG3TVyZxuOIO20Vdt\n" +
            "QSp77FcD97EtYtuemetpkRJKz0Gumaa55WC8oS00toSvZSCsNtsvcDnrbmWbTVGd\n" +
            "nfU5Wk04JG3sOdpclSdhnrLTmYIb+NxHALNXSM5le/Aq1WaKdPd2S0reKlg1AgZ1\n" +
            "0rWT2how3CyHui1UjZq9maxSsC1DZULpmO9UfITX4K6MKDbVhykIxHucDabOtRnf\n" +
            "vsBeyjCt78PIQUh0JU9YvBFTAQl9yBcbJqTzuF2GMOkCgYEA/zaUOsV2f3O3pTgo\n" +
            "avWyjMd3ZAIfMrNa10d3f3A8U2mze1Kf9MxAbATzlZggDp4b8yHL7FMk+oTgek7f\n" +
            "0maAHKSjzmYgor0svPQRfBqzwKXX75DaxQ2SlAcsunz+PDyPGeCktI5CzX+fYdGw\n" +
            "5682IR+gOChRX86623UlyWG+i+0CgYEA961D617j2LAkf4tbuR4Viy/AUjd3gsut\n" +
            "tN6eH2VA5uiBt6d9GNjz4tj7Pe1ZIG0taIsb8xi7HGErzvWCIzLpEdOC1KJ+XthV\n" +
            "VyCFKoD3gMkibNwz5SfXp+nanyiQ47cn+tLU2gQX9truM0tiQ0za7qZyyQeDVk9K\n" +
            "guQYN3mZXEcCgYAEOaYYlv5QO901egmJQ8oR9Jdwa2ySzU9asw8yZ+xHwT88cnC9\n" +
            "f8gtQaO6vUsCkvGJg+y5W+MH1r1UqQuhkxrZ3ovrtdsidplQA4ZqlJKXROefSrmk\n" +
            "e4YKt1exOrKG0sXXutVfO9+1QUClQPB51XfC8qAXbety95w2V+kpPVoHPQKBgQDi\n" +
            "7N9D8gb0bD6klXyOmZ/HjRK6wZNARXl8NFPQFCdo6TKgoFafw3eBKqjXTnXQnfdy\n" +
            "x7HuA4S8NdO7pXl84pJlANPoGpGKOM/IKkWYcd1TI/Xko8Twepa+hCcImFRYL2H0\n" +
            "D+NxdTq3tFN2r+TyIKKe8eZNhhKxq6hrUK1XdpQq1QKBgQClUDDROhYcbI9/loHx\n" +
            "y9SP3DKRbyLEryHVm3uwURohqNMVrgUhdRwb4nZPWHFsSZVoZqWqyi2fZHuUPdDF\n" +
            "2sdsibbT3SLY5pT+Si4oh+NuqEE3FwUBY1nRhKjDHrob49mmW2dFreWvvAmUzPIy\n" +
            "47ziKAGOKQ6lT2LP1HGMSDFAUA==\n" +
            "-----END PRIVATE KEY-----\n";


    /**
     * 获取商户的私钥文件
     * @param filename
     * @return
     */
    private PrivateKey getPrivateKey(String filename){

        try {
            return PemUtil.loadPrivateKey(privateKey);
        } catch (Exception e) {
            throw new RuntimeException("私钥文件不存在", e);
        }
    }

    /**
     * 获取签名验证器
     * @return
     */
    @Bean
    public Verifier getVerifier() throws Exception {
        log.info("获取签名验证器");
        //获取商户私钥

        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        // 私钥签名对象
        PrivateKeySigner keySigner = new PrivateKeySigner(mchSerialNo, privateKey);
        // 身份认证对象
        WechatPay2Credentials wechatPay2Credentials = new WechatPay2Credentials(mchId, keySigner);

        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息
        certificatesManager.putMerchant(mchId, wechatPay2Credentials,
                apiV3Key.getBytes(StandardCharsets.UTF_8));
        // ... 若有多个商户号，可继续调用putMerchant添加商户信息
        Verifier verifier = certificatesManager.getVerifier(mchId);

        return verifier;
    }

    /**
     * 获取http请求对象
     * @param verifier
     * @return
     */
    @Bean("wxPayClient")
    public CloseableHttpClient getWxPayClient(Verifier verifier){

        log.info("获取httpclient");

        //获取商户私钥
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        // 从证书管理器中获取verifier
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, privateKey)
                .withValidator(new WechatPay2Validator(verifier));
        // ... 接下来，你仍然可以通过builder设置各种参数，来配置你的HttpClient

        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
        CloseableHttpClient httpClient = builder.build();
        return httpClient;
    }

    /**
     * 获取HttpClient，无需进行应答签名验证，跳过验签的流程
     */
    @Bean(name = "wxPayNoSignClient")
    public CloseableHttpClient getWxPayNoSignClient(){

        //获取商户私钥
        PrivateKey privateKey = getPrivateKey(privateKeyPath);

        //用于构造HttpClient
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                //设置商户信息
                .withMerchant(mchId, mchSerialNo, privateKey)
                //无需进行签名验证、通过withValidator((response) -> true)实现
                .withValidator((response) -> true);

        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
        CloseableHttpClient httpClient = builder.build();

        log.info("== getWxPayNoSignClient END ==");
        return httpClient;
    }


    public String decryptFromResource(Map<String,Object> bodyMap) throws GeneralSecurityException {
        log.info("秘文解密");

        //通知数据
        Map<String,String > resourceMap =(Map<String, String>) bodyMap.get("resource");
        //数据秘文
        String ciphertext = resourceMap.get("ciphertext");
        //获取随机串
        String nonce = resourceMap.get("nonce");
        String associated_data = resourceMap.get("associated_data");

        log.info("秘文===》{}",ciphertext);
        AesUtil aesUtil = new AesUtil(getApiV3Key().getBytes(StandardCharsets.UTF_8));
        //获取明文（解密后的数据）
        String plainText = aesUtil.decryptToString(associated_data.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);

        log.info("明文====》{}",plainText);

        return plainText;
    }


    /**
     * 拼接五个参数
     * @param appId
     * @param timestamp
     * @param nonceStr
     * @param packag
     * @return
     */
    public  String buildMessageTwo(String appId, long timestamp, String nonceStr, String packag) {
        return appId + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + packag + "\n";
    }

    /**
     * 进行二次封装
     * @param message
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidKeyException
     */
    public  String sign(byte[] message) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, InvalidKeyException {
        Signature sign = Signature.getInstance("SHA256withRSA"); //SHA256withRSA
        sign.initSign(PemUtil.loadPrivateKey(privateKey)); // 微信证书私钥
        sign.update(message);
        return Base64.getEncoder().encodeToString(sign.sign());
    }
    // public  String sign(String wxCertPath,byte[] message) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, java.security.InvalidKeyException {
    //     Signature sign = Signature.getInstance("SHA256withRSA"); //SHA256withRSA
    //     sign.initSign(PemUtil.loadPrivateKey(new FileInputStream(wxCertPath))); // 微信证书私钥
    //     sign.update(message);
    //     return Base64.getEncoder().encodeToString(sign.sign());
    // }
    /**
     * 获取32位随机字符串
     * @return
     */
    public  String getNonceStr(){
        return UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, 32);
    }
    /**
     * 获取当前时间戳，单位秒
     * @return
     */
    public long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    /**
     * 微信v3 post请求
     * @param url
     * @param body
     * @return
     * @throws Exception
     */
    public String v3Post(String url, String body) throws Exception {
        log.info("微信v3 post请求参数:" + body);
        CloseableHttpClient httpClient = getClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(ACCEPT, APPLICATION_JSON.toString());
        httpPost.addHeader(CONTENT_TYPE, APPLICATION_JSON.toString());
        httpPost.addHeader("Wechatpay-Serial", mchSerialNo);
        httpPost.setEntity(new StringEntity(body, "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            log.info("微信v3 post请求返回信息：" + bodyAsString);
            return bodyAsString;
        } finally {
            httpClient.close();
            response.close();
        }
    }

    /**
     * 微信通讯client
     * @return CloseableHttpClient
     */
    @SneakyThrows
    public CloseableHttpClient getClient() throws NotFoundException, GeneralSecurityException, IOException, HttpCodeException {

        /**第一种方式读取：配置本地文件私钥证书读取*/
//        Resource resource = new ClassPathResource(apiclientKey);
//        InputStream apiclientKeyInputStream = resource.getInputStream();

        /**第二种直接读取：商户证书私钥文件*/
//        InputStream apiclientKeyFile = ClassUtils
//                .getDefaultClassLoader()
//                .getResourceAsStream(apiclientKey);
//        Reader reader = new InputStreamReader(apiclientKeyFile, "UTF-8");

        //通过生成的jar包获取路径
        InputStream apiclientKeyFile = Files.newInputStream(Paths.get(privateKeyPath));

        /** 获取jar包下面的路径--------------------*/
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(apiclientKeyFile);

        /**微信支付平台证书文件*/
        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息 同望
        certificatesManager.putMerchant(mchId, new WechatPay2Credentials(mchId,
                new PrivateKeySigner(mchSerialNo, merchantPrivateKey)), apiV3Key.getBytes(StandardCharsets.UTF_8));

        // ... 若有多个商户号，可继续调用putMerchant添加商户信息 华帝
//        certificatesManager.putMerchant(mchId, new WechatPay2Credentials(mchId,
//                new PrivateKeySigner(wechatPaySerial, merchantPrivateKey)), apiV3Key.getBytes(StandardCharsets.UTF_8));

        // 从证书管理器中获取verifier
        Verifier verifier = certificatesManager.getVerifier(mchId);

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier));
        CloseableHttpClient httpClient = builder.build();

        return httpClient;
    }


    /**
     * 添加分账用户
     * @param openid      个人用户openId
     * @throws Exception
     */
    public String profitSharingAdd(String openid,String name) throws Exception {
        JSONObject profitSharingJSON = new JSONObject();
        profitSharingJSON.put("appid", appid);
        profitSharingJSON.put("type","PERSONAL_OPENID");
        profitSharingJSON.put("account",openid);
//        profitSharingJSON.put("name",);
        profitSharingJSON.put("relation_type","USER");
        // profitSharingJSON.put("custom_relation","工程师");
        String body = profitSharingJSON.toJSONString();
        log.info("微信添加分账profitSharingAdd===="+body);
        return v3Post("https://api.mch.weixin.qq.com/v3/profitsharing/receivers/add", body);
    }


    /**
     * 支付成功后请求分账
     * @param transactionId       微信订单号
     * @param outOrderNo       商户分账单号
     * @throws Exception
     */
//     public String profitSharingApply(String transactionId, String outOrderNo, String openid, long total) throws Exception {
//         JSONObject profitSharingJSON = new JSONObject();
//         profitSharingJSON.put("appid", appid);
//         profitSharingJSON.put("transaction_id",transactionId);
//         profitSharingJSON.put("out_order_no",outOrderNo);
//         JSONArray jsonArray = new JSONArray();
//         JSONObject receiversJSON = new JSONObject();
//         receiversJSON.put("type","PERSONAL_OPENID");
//         receiversJSON.put("account",openid);
// //        receiversJSON.put("name","");
//         receiversJSON.put("amount",total);
//         receiversJSON.put("description","分润");
//         jsonArray.add(receiversJSON);
//         profitSharingJSON.put("receivers",jsonArray);
//         profitSharingJSON.put("unfreeze_unsplit",true);
//         String body = profitSharingJSON.toJSONString();
//         log.info("微信请求分账profitSharingApply===="+body);
//         return v3Post("https://api.mch.weixin.qq.com/v3/profitsharing/orders", body);
//     }
    public String profitSharingApply(String transactionId, String outOrderNo, List<FzUser> receivers) throws Exception {
        JSONObject profitSharingJSON = new JSONObject();
        profitSharingJSON.put("appid", appid);
        profitSharingJSON.put("transaction_id",transactionId);
        profitSharingJSON.put("out_order_no",outOrderNo);
        JSONArray jsonArray = new JSONArray();
        receivers.stream().forEach(item ->{
            JSONObject receiversJSON = new JSONObject();
            receiversJSON.put("type","PERSONAL_OPENID");
            receiversJSON.put("account",item.getOpenid());
//        receiversJSON.put("name","");
            receiversJSON.put("amount",item.getTotal());
            receiversJSON.put("description","分润");
            jsonArray.add(receiversJSON);
        });
        profitSharingJSON.put("receivers",jsonArray);
        profitSharingJSON.put("unfreeze_unsplit",true);
        String body = profitSharingJSON.toJSONString();
        log.info("微信请求分账profitSharingApply===="+body);
        return v3Post("https://api.mch.weixin.qq.com/v3/profitsharing/orders", body);
    }

    /**
     * 退款成功后分账退回
     * @param orderId       微信分账单号
     * @param outOrderNo       商户分账单号
     * @param outReturnNo       分润单号
     * @param amount       回退金额
     * @throws Exception
     */
    public String domesticBenefits(String orderId,String outOrderNo,String outReturnNo,BigDecimal amount) throws Exception {
        JSONObject profitSharingJSON = new JSONObject();
        profitSharingJSON.put("return_mchid", mchId);
        profitSharingJSON.put("order_id",orderId);
        profitSharingJSON.put("out_order_no",outOrderNo);
        profitSharingJSON.put("out_return_no",outReturnNo);
        profitSharingJSON.put("amount",Integer.valueOf(amount.intValue()));
        profitSharingJSON.put("description","用户退款");
        String body = profitSharingJSON.toJSONString();
        log.info("微信分账退回profitSharingBody===="+body);
        return v3Post("https://api.mch.weixin.qq.com/v3/profitsharing/return-orders", body);
    }



}
