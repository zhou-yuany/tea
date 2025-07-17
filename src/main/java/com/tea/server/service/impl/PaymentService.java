package com.tea.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.config.WeChatProperties;
import com.tea.server.core.OrderCast;
import com.tea.server.entity.*;
import com.tea.server.exception.RemoteException;
import com.tea.server.mapper.InterfaceLogMapper;
import com.tea.server.mapper.OrdersFlowMapper;
import com.tea.server.mapper.OrdersMapper;
import com.tea.server.mapper.RechargeRecordMapper;
import com.tea.server.service.OrdersFlowService;
import com.tea.server.service.RechargeRecordService;
import com.tea.server.service.ShopsService;
import com.tea.server.socket.CodeData;
import com.tea.server.socket.DeviceWebSocket;
import com.tea.server.socket.PlatformWebSocket;
import com.tea.server.utils.CoreUtil;
import eleme.openapi.sdk.api.service.ShopService;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class PaymentService implements OrderCast {
    @Autowired
    private WeChatProperties weChatProperties;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private RechargeRecordMapper rechargeRecordMapper;

    @Resource
    private ShopsService shopService;

    @Resource
    private OrdersFlowService ordersFlowService;

    @Resource
    private InterfaceLogMapper interfaceLogMapper;

    public PaymentResponse prePayment(Long orderId, Long shopId, String did) {
        Orders orders = ordersMapper.selectById(orderId);
        BigDecimal totalPrice = new BigDecimal(0L);
        if (null != shopId && shopId > 0){
            // Shop shop = shopService.getById(shopId);
            totalPrice = BigDecimal.valueOf(1);
        }else {
            totalPrice = orders.getTotalPrice();
        }

        PaymentRequest request = new PaymentRequest(totalPrice);
        try (HttpResponse response = HttpUtil.createPost(PRETRADE_API)
                .body(XmlUtil.toStr(XmlUtil.mapToXml(BeanUtil.beanToMap(request)
                        , "xml")), "application/xml")
                .execute()) {
            String body = response.body();
            log.info("6666666666666666666666666666:"+body);
            CoreUtil.validateWechatSign(XmlUtil.xmlToMap(body), weChatProperties.getMchKey());
            PaymentResponse paymentResponse = BeanUtil.mapToBean(XmlUtil.xmlToMap(body)
                    , PaymentResponse.class, false, CopyOptions.create());
            // if(!"SUCCESS".equals(paymentResponse.getReturn_code())){
            //     log.error("下单失败: {}", body);
            //     throw new RemoteException("微信接口请求失败!");
            // }

            if (null != orderId && orderId > 0){
                Orders orders1 = new Orders();
                BeanUtils.copyProperties(orders, orders1);
                orders1.setOutTradeNo(request.getOut_trade_no());
                ordersMapper.updateById(orders1);

            }
            if (null != shopId && shopId > 0){
                // RechargeRecord rechargeRecord = new RechargeRecord();
                // rechargeRecord.setType(1);
                // rechargeRecord.setPrice(BigDecimal.valueOf(2000));
                // rechargeRecord.setOrderNo(did);
                // rechargeRecord.setShopId(shopId);
                // rechargeRecord.setOutTradeNo(request.getOut_trade_no());
                // rechargeRecord.setCreateTime(LocalDateTime.now());
                // rechargeRecord.setUpdateTime(LocalDateTime.now());
                // rechargeRecordMapper.insert(rechargeRecord);

                OrdersFlow ordersFlow = new OrdersFlow();
                ordersFlow.setOutTradeNo(request.getOut_trade_no());
                ordersFlow.setType(1);
                ordersFlow.setPrice(BigDecimal.valueOf(2000));
                ordersFlow.setPayPlatform(4);
                ordersFlow.setShopDistributionPrice(BigDecimal.valueOf(2000));
                ordersFlow.setShopId(shopId);
                ordersFlow.setCreateTime(LocalDateTime.now());
                ordersFlow.setUpdateTime(LocalDateTime.now());
                ordersFlow.setSerialNum(did);
                ordersFlowService.save(ordersFlow);

            }

            return paymentResponse;

        }
    }

    public void notify(String body) {
        CoreUtil.validateWechatSign(XmlUtil.xmlToMap(body), weChatProperties.getMchKey());
        NotifyRequest notifyRequest = BeanUtil.mapToBean(XmlUtil.xmlToMap(body)
                , NotifyRequest.class, false, CopyOptions.create());
        log.info("支付回调:"+notifyRequest.getResult_code());
        if("SUCCESS".equals(notifyRequest.getResult_code())){
            // 支付成功 订单
            LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Orders :: getStatus, 1).eq(Orders :: getOutTradeNo, notifyRequest.getOut_trade_no());
            Orders orders = ordersMapper.selectOne(queryWrapper);
            if (null != orders){
                Orders orders1 = new Orders();
                BeanUtils.copyProperties(orders, orders1);
                log.error("order", orders.toString());
                orders1.setPayStatus(2);
                orders1.setUpdateTime(LocalDateTime.now());
                ordersMapper.updateById(orders1);
            }

            // 支付成功 商户充值
            LambdaQueryWrapper<OrdersFlow> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getOutTradeNo, notifyRequest.getOut_trade_no()).eq(OrdersFlow :: getPayStatus, 0);
            OrdersFlow one = ordersFlowService.getOne(wrapper);

                if (null != one){
                    OrdersFlow ordersFlow = new OrdersFlow();
                    BeanUtils.copyProperties(one, ordersFlow);
                    ordersFlow.setPayStatus(1);
                    ordersFlow.setUpdateTime(LocalDateTime.now());
                    ordersFlowService.updateById(ordersFlow);

                    Shop shop = shopService.getById(one.getShopId());
                    Shop shop2 = new Shop();
                    BeanUtils.copyProperties(shop, shop2);
                    shop2.setBalance(shop.getBalance().add(one.getShopDistributionPrice()));
                    shopService.updateById(shop2);
                    DeviceWebSocket.sendOneMessage(one.getShopId(), new CodeData("ok", "您有新订单啦"));

                    InterfaceLog interfaceLog = new InterfaceLog();
                    interfaceLog.setTitle("商户充值成功");
                    interfaceLog.setMethodName("notify");
                    String content = "商户"+shop.getName()+"充值成功，"+"充值金额为：2000元";
                    interfaceLog.setContent(content);
                    interfaceLog.setShopId(one.getShopId());
                    interfaceLog.setTypeStatus(0);
                    interfaceLog.setCreateTime(LocalDateTime.now());
                    interfaceLog.setUpdateTime(LocalDateTime.now());
                    interfaceLogMapper.insert(interfaceLog);
                }




        }

    }





    @Data
    public class NotifyRequest{

        private String result_code;

        private String out_trade_no;

    }

    @Data
    @Accessors(chain = true)
    public class PaymentRequest {

        private String appid;
        private String mch_id;
        private String nonce_str;
        private String body;
        private String out_trade_no;
        private String total_fee;
        private String trade_type = "NATIVE";
        private String notify_url;
        private String time_expire;
        private String sign;

        public PaymentRequest(BigDecimal total) {
            this.appid = weChatProperties.getAppId();
            this.mch_id = weChatProperties.getMchId();
            this.nonce_str = CoreUtil.getUUID();
            this.body = weChatProperties.getPaymentTitle();
            this.out_trade_no = CoreUtil.getOrderCode();
            // this.total_fee = total.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
            this.total_fee = String.valueOf(total);
            this.notify_url = weChatProperties.getNotifyUrl();
            this.time_expire = LocalDateTime.now().plusSeconds(weChatProperties.getExpireTime())
                    .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            setSign(CoreUtil.getWechatSignByKey(BeanUtil.beanToMap(this), weChatProperties.getMchKey()));
        }

    }

    @Data
    @Accessors(chain = true)
    public static class PaymentResponse {
        private String return_code;
        private String prepay_id;
        private String code_url;
    }


    public static void main(String[] args) {
        Map<String, Object> map = XmlUtil.xmlToMap("<xml><return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<return_msg><![CDATA[OK]]></return_msg>\n" +
                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "<mch_id><![CDATA[1494035982]]></mch_id>\n" +
                "<appid><![CDATA[wxa3713fc3b1778283]]></appid>\n" +
                "<nonce_str><![CDATA[pDhw1Orrc4jANRDJ]]></nonce_str>\n" +
                "<sign><![CDATA[3679260D67C8DB33AB83EDA2374EA727]]></sign>\n" +
                "<prepay_id><![CDATA[wx30231928130469d13074d4f48f03f10000]]></prepay_id>\n" +
                "<trade_type><![CDATA[NATIVE]]></trade_type>\n" +
                "<code_url><![CDATA[weixin://wxpay/bizpayurl?pr=DkXTRkDzz]]></code_url>\n" +
                "</xml>");


    }
}
