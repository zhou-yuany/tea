package com.tea.server.socket;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/webSocket/{shopId}")
public class DeviceWebSocket {

    private final static Logger logger = LoggerFactory.getLogger(DeviceWebSocket.class);

    private static ConcurrentHashMap<Long, Session> orderInfoMap = new ConcurrentHashMap<>();
    private static int onlineCount = 0;

    /**
     * 发送消息
     * @param
     * @param message
     */
    @OnMessage
    public void onMessage(@PathParam("shopId") Long shopId, String message) {
        try {
            // 向指定用户发送消息
            orderInfoMap.get(shopId).getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立连接调用的方法
     * @param session
     * @param shopId
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("shopId") Long shopId) {
        if (null != shopId) {
            orderInfoMap.put(shopId, session);
            onlineCount = orderInfoMap.size();
            logger.info("建立连接成功，当前人数为" + onlineCount);
        }
    }

    /**
     * 关闭链接调用接口
     * @param shopId
     */
    @OnClose
    public void onClose(@PathParam("shopId") Long shopId) {
        if (null != shopId) {
            orderInfoMap.remove(shopId);
            onlineCount = orderInfoMap.size();
            logger.info("断开连接成功，当前人数为" + onlineCount);
        }
    }
    // 此为单点消息
    public static void sendOneMessage(Long shopId, CodeData ca) {
        // System.out.println(userInfoMap);

        try {
            orderInfoMap.get(shopId).getBasicRemote().sendText(JSONObject.toJSONString(ca));

        }catch (Exception e){
            logger.info("发送失败");
            e.printStackTrace();
        }
    }
}
