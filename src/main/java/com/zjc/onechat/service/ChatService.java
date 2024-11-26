package com.zjc.onechat.service;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.onechat.dto.Result;
import com.zjc.onechat.dto.UnReadCountDTO;
import com.zjc.onechat.entity.Chat;
import com.zjc.onechat.entity.Message;
import com.zjc.onechat.mapper.ChatMapper;
import com.zjc.onechat.mapper.MessageMapper;
import com.zjc.onechat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private MessageMapper messageMapper;


    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private UserMapper userMapper;



    /**
     * 获取聊天列表
     * @param userId 用户ID
     * @return 聊天列表
     */
    public List<Chat> getChatList(Long userId) {
        return chatMapper.selectList(new QueryWrapper<Chat>().eq("userId", userId));
    }

    /**
     * 获取聊天消息
     * @param chatId 聊天ID
     * @param userId 用户ID
     * @return 聊天消息列表
     */
    public List<Message> getChatMessages(Long chatId, Long userId) {
        return messageMapper.selectList(new QueryWrapper<Message>().eq("chatId", chatId).eq("senderId", userId).or().eq("senderId", userId));
    }
    public Chat getChatHByMe(Long userId, Long friendId){
        Chat chatsForMe = chatMapper.selectOne(new QueryWrapper<Chat>().eq("user_id", userId));
        if(BeanUtil.isEmpty(chatsForMe)){
            Chat chat = new Chat();
            chat.setFriendId(friendId);
            chat.setUserId(userId);
            chatMapper.insert(chat);
            return chat;
        }
        return chatsForMe;
    }

    /**
     * 发送聊天消息
     * @param chatId 聊天ID
     * @param senderId 发送者ID
     * @param content 消息内容
     * @return 发送的消息
     */
    public Message sendMessage(Long chatId, Long senderId, String content) {
        Message message = new Message();
        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setSentTime(LocalDateTime.now());
        message.setRead(false);
        messageMapper.insert(message);

        // 更新聊天会话的最后消息时间
        Chat chat = chatMapper.selectById(chatId);
        if (chat != null) {
            chat.setLastMessageTime(LocalDateTime.now());
            chatMapper.updateById(chat);
        }

        return message;
    }



    public void saveMessage(Message message) {
        messageMapper.insert(message);
    }

    public List<Message> findUnreadMessages(Long userId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_user_id", userId).eq("is_read", false);
        return messageMapper.selectList(queryWrapper);
    }

    public Result getUnReadCnt(Integer userId, Integer friendId) {
        Integer count = messageMapper.selectCount(new QueryWrapper<Message>().eq("sender_id", friendId)
                .eq("to_user_id", userId).eq("is_read",0));
        System.out.println("未读消息："+friendId+"-->"+userId+":"+count);
        // TODO 把取得的未读消息化为已读：

        UnReadCountDTO unReadCountDTO = new UnReadCountDTO();
        unReadCountDTO.setCount(count);
        Message latestMessage = messageMapper.findLatestMessage(userId, friendId);
        unReadCountDTO.setMessage(latestMessage);
        return Result.ok(unReadCountDTO);
    }
}
