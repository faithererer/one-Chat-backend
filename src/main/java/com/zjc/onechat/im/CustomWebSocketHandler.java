package com.zjc.onechat.im;

import cn.hutool.json.JSONUtil;
import com.zjc.onechat.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {
    private final SessionManager sessionManager;
    private final MessageService messageService;
    public static final String PING_MESSAGE = "ping";
    public static final String PONG_MESSAGE = "pong";
    Map<String,List<Message>> messageMap = new ConcurrentHashMap<>();
    @Autowired
    public CustomWebSocketHandler(SessionManager sessionManager, MessageService messageService) {
        this.sessionManager = sessionManager;
        this.messageService = messageService;
    }

    @Override
    @Transactional
    public void afterConnectionEstablished(WebSocketSession session){
        sessionManager.registerSession(session);
        System.out.println("WebSocket connection established for user " + session.getAttributes().get("userId").toString());
        // 检查并发送所有未读消息
        String userId = session.getAttributes().get("userId").toString();
        List<Message> pendingMessages = messageService.getOfflineMessage(session.getAttributes().get("userId").toString());
        System.out.println("当前用户未读消息:"+pendingMessages);

        // 将未读消息存入 messageMap
        messageMap.put(userId, pendingMessages);

        for (Message message : pendingMessages) {
            try {
                session.sendMessage(new TextMessage(JSONUtil.toJsonStr(message)));
            } catch (IOException e) {
                System.out.println("ws断开，回滚未读消息");
                messageService.markMessageAsRead(message.getId(), false);
                continue;
            }
            // 标记消息为已读
            messageService.markMessageAsRead(message.getId(), true);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();

        if (PING_MESSAGE.equals(payload)) {
            // 收到客户端的心跳包，回复心跳响应
            System.out.println("收到心跳:"+payload);
            session.sendMessage(new TextMessage(PONG_MESSAGE));
        }else{

            messageService.processReceivedMessage(session, message);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        // TODO 传输失败，通过messageMap回滚已读状态为未读
        // 处理传输失败，回滚消息状态
        System.out.println("传输错误，回滚");
        String userId = session.getAttributes().get("userId").toString();
        List<Message> pendingMessages = messageMap.get(userId);
        Integer size = pendingMessages.size();
        if (pendingMessages != null) {
            for (Message message : pendingMessages) {
                messageService.markMessageAsRead(message.getId(), false); // 将已读标记回滚为未读
            }
            messageMap.remove(userId); // 清除消息缓存
        }
        System.out.println("传输错误，回滚完毕，共有:"+size+"条");


    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("用户："+session.getAttributes().get("userId")+"离线了");

        sessionManager.removeSession(session.getAttributes().get("userId").toString(),session);
    }

}