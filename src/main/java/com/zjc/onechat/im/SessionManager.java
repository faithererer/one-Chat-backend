package com.zjc.onechat.im;

import cn.hutool.json.JSONUtil;
import com.zjc.onechat.entity.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void registerSession(WebSocketSession session) {
        sessions.put(session.getAttributes().get("userId").toString(), session);

    }

    public void removeSession(String userId, WebSocketSession session) {
        sessions.remove(session.getAttributes().get("userId").toString());
    }

    public boolean isUserOnline(String userId) {
        System.out.println(sessions);
        return sessions.containsKey(userId);
    }

    public void sendMessage(String userId, Message message) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(JSONUtil.toJsonStr(message)));
        } else {
            throw new RuntimeException("User " + userId + " not connected");
        }
    }


}