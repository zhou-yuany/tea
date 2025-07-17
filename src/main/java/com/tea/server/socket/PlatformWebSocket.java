package com.tea.server.socket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@ServerEndpoint("/platSocket/{adminId}")
public class PlatformWebSocket {

    // private static UsersService usersService;
    //
    // @Autowired
    // public void setApplicationContext(UsersService usersService){
    //     ComWebSocket.usersService = usersService;
    // }

    private final static Logger logger = LoggerFactory.getLogger(PlatformWebSocket.class);

    private static ConcurrentHashMap<Long, Session> orderInfoMap = new ConcurrentHashMap<>();
    private static int onlineCount = 0;

    /**
     * 发送消息
     * @param
     * @param message
     */
    @OnMessage
    public void onMessage(@PathParam("adminId") Long adminId, String message) {
        try {
            // 向指定用户发送消息
            orderInfoMap.get(adminId).getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立连接调用的方法
     * @param session
     * @param adminId
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("adminId") Long adminId) {
        if (null != adminId) {
            orderInfoMap.put(adminId, session);
            onlineCount = orderInfoMap.size();
            // logger.info("建立连接成功，当前人数为" + onlineCount);
        }
    }

    /**
     * 关闭链接调用接口
     * @param adminId
     */
    @OnClose
    public void onClose(@PathParam("adminId") Long adminId) {
        if (null != adminId) {
            orderInfoMap.remove(adminId);
            onlineCount = orderInfoMap.size();
            // logger.info("断开连接成功，当前人数为" + onlineCount);
        }
    }
    // 此为单点消息
    public static void sendOneMessage(Long adminId, CodeData ca) {
        // System.out.println(userInfoMap);
        try {
            // log.info("&&&&&&&&&&&&&&&&"+orderInfoMap.get(adminId).getBasicRemote());
            // log.info("*****************"+JSONObject.toJSONString(ca));
            orderInfoMap.get(adminId).getBasicRemote().sendText(JSONObject.toJSONString(ca));

        }catch (Exception e){
            // logger.info("发送失败");
            e.printStackTrace();
        }
    }
}
