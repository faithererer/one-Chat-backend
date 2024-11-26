//package com.zjc.onechat.ws;
//
//import cn.hutool.json.JSONUtil;
//import com.zjc.onechat.entity.Message;
//import com.zjc.onechat.entity.UserHolder;
//import com.zjc.onechat.im.MessageListener;
//import com.zjc.onechat.mapper.MessageMapper;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.AbstractWebSocketHandler;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class CustomWebSocketHandler extends AbstractWebSocketHandler {
//    private final MessageListener messageListener;
//    private final RabbitTemplate rabbitTemplate;
//    private final ObjectMapper objectMapper;
//    private final MessageMapper messageMapper;
//
//    @Autowired
//    @Lazy
//    public CustomWebSocketHandler(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, MessageListener messageListener, MessageMapper messageMapper) {
//        this.rabbitTemplate = rabbitTemplate;
//        this.objectMapper = objectMapper;
//        this.messageListener = messageListener;
//        this.messageMapper = messageMapper;
//    }
//
//    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String userId = String.valueOf(UserHolder.getUserId());
//        session.getAttributes().put("userId", userId);
//        sessions.put(userId, session);
//        messageListener.sendPendingMessages(userId);
//        System.out.println("User " + userId + " connected.");
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String userId = (String) session.getAttributes().get("userId");
//        System.out.println("Received message from user " + userId + ": " + message.getPayload());
//        Message msg = JSONUtil.toBean(message.getPayload(), Message.class);
//        String toUserId = msg.getToUserId().toString();
//        forwardMessageToUser(toUserId, msg);
//    }
//
//
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        String userId = (String) session.getAttributes().get("userId");
//        System.err.println("Transport error for user " + userId + ": " + exception.getMessage());
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        String userId = (String) session.getAttributes().get("userId");
//        sessions.remove(userId);
//        System.out.println("User " + userId + " disconnected.");
//    }
//
//    @Override
//    public boolean supportsPartialMessages() {
//        return false;
//    }
//
//
//
//    public void forwardMessageToUser(String userId, Message message) throws IOException {
//        WebSocketSession session = sessions.get(userId);
//        if (session != null && session.isOpen()) {
//            session.sendMessage(new TextMessage(JSONUtil.toJsonStr(message)));
//            System.out.println("Forwarded message to user " + userId);
//        } else {
//            System.err.println("User " + userId + " not connected");
//            saveOfflineMessage(userId, message);  // 保存离线消息
//        }
//    }
//    public boolean isUserOnline(String userId) {
//        return sessions.containsKey(userId);
//    }
//    public void saveOfflineMessage(String userId, Message message) {
//        // 假设有一个Message对象用于持久化存储
//        message.setToUserId(Long.parseLong(userId));
//        message.setSentTime(LocalDateTime.now());
//        message.setRead(false);
//
//        // 保存消息到数据库
//        messageMapper.insert(message);
//        System.out.println("已经缓存离线消息 :"+message);
//    }
//}
