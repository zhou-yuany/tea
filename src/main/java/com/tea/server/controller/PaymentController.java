package com.tea.server.controller;

import com.tea.server.config.WeChatProperties;
import com.tea.server.entity.RechargeRecord;
import com.tea.server.service.RechargeRecordService;
import com.tea.server.service.impl.PaymentService;
import com.tea.server.utils.CoreUtil;
import com.tea.server.utils.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 支付接口
 */
@Slf4j
@RestController
@RequestMapping("cashierPay")
public class PaymentController {


    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private RechargeRecordService rechargeRecordService;


    @SneakyThrows
    @GetMapping("/pay")
    public void qrcode(@RequestParam(value = "orderId", required = false) Long orderId, @RequestParam(value = "shopId", required = false) Long shopId, @RequestParam(value = "did", required = false) String did,  HttpServletResponse response) {
        log.info("************************************************");
        PaymentService.PaymentResponse paymentResponse = paymentService.prePayment(orderId, shopId, did);
        try (OutputStream os = response.getOutputStream()) {
            CoreUtil.generateQRCodeImage(paymentResponse.getCode_url(), os);
            response.setContentType("image/*");
        }catch (Exception e) {
            log.info("返回错误信息："+e.getMessage());
        }


    }

    // @GetMapping("wechat/long/pull/{orderId}")
    // public FinLogBean longPull(@PathVariable Long orderId){
    //     return finLogService.longPull(orderId);
    // }

    @PostMapping("/notify")
    public String notify(@RequestBody String body) {
        try {
            log.error("notify: {}", body);
            paymentService.notify(body);
            return sendCallBackResult(true, null);
        } catch (Exception e) {
            return sendCallBackResult(false, e.getMessage());
        }
    }

    public String sendCallBackResult(boolean isSuccess, String msg) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml><return_code><![CDATA[");
        if (isSuccess) {
            sb.append("SUCCESS]]></return_code><return_msg><![CDATA[OK");
        } else {
            sb.append("FAIL]]></return_code><return_msg><![CDATA[").append(msg);
        }
        sb.append("]]></return_msg></xml>");
        return sb.toString();
    }



}
