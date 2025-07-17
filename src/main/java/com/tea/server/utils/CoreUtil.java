package com.tea.server.utils;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.MD5;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CoreUtil {

    private static final String[] CHARS = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private static final Cache<Object, Object> CACHE = CacheUtil.newTimedCache(TimeUnit.DAYS.toMillis(1));

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getOrderCode() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "_"
                + randomNumber();
    }

    public static Cache<Object, Object> getCache() {
        return CACHE;
    }

    public static String shortUUID() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = getUUID();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(CHARS[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    public static String randomNumber() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public static void validateWechatSign(Map<String, Object> map, String key){
        String sign = (String) map.remove("sign");
        if(ObjectUtil.isEmpty(sign)){
            log.error("validate payment param: {}", map);
            throw new ValidateException("无签名字段");
        }
        if(!Objects.equals(sign, getWechatSignByKey(map, key))){
            log.error("validate payment param: {}", map);
            throw new ValidateException("签名错误");
        }
    }

    public static String getWechatSignByKey(Map<String, Object> map, String key){
        Set<String> set = map.keySet();
        TreeSet<String> treeSet = new TreeSet<>();
        for(String mapKey : set){
            if(map.get(mapKey) != null && !map.get(mapKey).equals("")){
                treeSet.add(mapKey);
            }
        }
        StringBuilder sb = new StringBuilder();
        for(String mapKey : treeSet){
            if(mapKey.equals("package1")){
                sb.append("package=" + map.get(mapKey) + "&");
            }else {
                sb.append(mapKey + "=" + map.get(mapKey) + "&");
            }
        }
        sb.append("key=" + key);
        return MD5.create().digestHex(sb.toString(), StandardCharsets.UTF_8).toUpperCase();
    }


    public static void generateQRCodeImage(String text, OutputStream os)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 150, 150);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", os);
    }

}
