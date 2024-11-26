package com.zjc.onechat.im;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.onechat.entity.Message;
import com.zjc.onechat.mapper.MessageMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final RabbitTemplate rabbitTemplate;
    private final SessionManager sessionManager;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(RabbitTemplate rabbitTemplate, SessionManager sessionManager, MessageMapper messageMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.sessionManager = sessionManager;
        this.messageMapper=messageMapper;
    }

    @Transactional
    public void processReceivedMessage(WebSocketSession session, TextMessage message) throws IOException {
        Message msg = JSONUtil.toBean(message.getPayload(), Message.class);
        if (sessionManager.isUserOnline(String.valueOf(msg.getToUserId()))) {
            System.out.println("用户在线:"+msg);;
            sessionManager.sendMessage(String.valueOf(msg.getToUserId()), msg);
        } else {
            // 用户不在线，将消息发送到RabbitMQ队列
            System.out.println("不在线:"+msg);
            rabbitTemplate.convertAndSend("messageQueue", msg);
        }
    }

    void saveOfflineMessage(Message message) {
        // 假设有一个Message对象用于持久化存储
        message.setSentTime(LocalDateTime.now());
        message.setRead(false);

        // 保存消息到数据库
        messageMapper.insert(message);
        System.out.println("已经缓存离线消息 :"+message);
    }
    List<Message> getOfflineMessage(String toUserId){
       return messageMapper.selectList(new QueryWrapper<Message>().eq("to_user_id",toUserId).eq("is_read",false));
    }

    public void markMessageAsRead(Long id,Boolean isRead) {
        Message message = new Message();
        message.setRead(isRead);
        message.setId(id);
        messageMapper.updateById(message);
    }
}
