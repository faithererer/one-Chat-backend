
package com.zjc.onechat.controller;

import com.zjc.onechat.dto.Result;
import com.zjc.onechat.entity.Chat;
import com.zjc.onechat.entity.Message;
import com.zjc.onechat.service.ChatService;
import com.zjc.onechat.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;




//    @MessageMapping("/sendMessage")
//    public void sendMessage(Message message) {
//        System.out.println("shoudao");
//        log.info("收到"+message);
//        Long receiverId = message.getToUserId();
//        User receiver = userService.getUserById(receiverId);
//
//        if (receiver.getIsOnline()) {
//            messagingTemplate.convertAndSendToUser(receiverId.toString(), "/queue/messages", message);
//        } else {
//            message.setRead(false);
//            chatService.saveMessage(message);
//            rabbitTemplate.convertAndSend("messageQueue", message);
//        }
//    }
    /**
     * 获取聊天列表
     * @param userId 用户ID
     * @return 聊天列表
     */
    @GetMapping("/list")
    public ResponseEntity<Result> getChatList(@RequestParam Long userId) {
        List<Chat> chatList = chatService.getChatList(userId);
        return ResponseEntity.ok(Result.ok(chatList));
    }

    /**
     * 获取聊天消息
     * @param chatId 聊天ID
     * @param userId 用户ID
     * @return 聊天消息列表
     */
    @GetMapping("/messages")
    public ResponseEntity<Result> getChatMessages(@RequestParam Long chatId, @RequestParam Long userId) {
        List<Message> messages = chatService.getChatMessages(chatId, userId);
        return ResponseEntity.ok(Result.ok(messages));
    }
    @GetMapping("/chatsByMe")
    public ResponseEntity<Result> getChatHByMe(@RequestParam Long userId, @RequestParam Long friendId) {
        Chat message = chatService.getChatHByMe(userId, friendId);
        return ResponseEntity.ok(Result.ok(message));
    }
    /**
     * 发送聊天消息
     * @param chatId 聊天ID
     * @param senderId 发送者ID
     * @param content 消息内容
     * @return 成功或失败消息
     */
    @PostMapping("/send")
    public ResponseEntity<Result> sendMessage(@RequestParam Long chatId, @RequestParam Long senderId, @RequestParam String content) {
        try {
            Message message = chatService.sendMessage(chatId, senderId, content);
            return ResponseEntity.ok(Result.ok(message));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.fail("消息发送失败：" + e.getMessage()));
        }
    }
    @GetMapping("/unread_count")
    public Result getUnReadCnt(Integer userId, Integer friendId){
        // 查询未读信息
        return chatService.getUnReadCnt(userId,friendId);
    }
}
