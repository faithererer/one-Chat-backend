package com.zjc.onechat.im;

import com.zjc.onechat.entity.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageListener {

    private final SessionManager sessionManager;
    private final MessageService messageService;

    @Autowired
    public MessageListener(SessionManager sessionManager, MessageService messageService) {
        this.sessionManager = sessionManager;
        this.messageService = messageService;
    }

    @RabbitListener(queues = "messageQueue")
    public void receiveMessage(@Payload Message message) throws Exception {
        String toUserId = String.valueOf(message.getToUserId());
        System.out.println(message);
        try {
            if (sessionManager.isUserOnline(toUserId)) {
                sessionManager.sendMessage(toUserId, message);
            } else {
                System.out.println("离线");
                messageService.saveOfflineMessage(message);
            }
        } catch (IOException e) {
            // 日志记录错误或处理消息重新排队/死信逻辑
            System.err.println("Error handling message for user " + toUserId + ": " + e.getMessage());
        }
    }



}
