package com.zjc.onechat.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("chat")
@Data
public class Chat {
    private Long id;
    private Long userId;
    private Long friendId;
    private LocalDateTime lastMessageTime;
}
