package com.zjc.onechat.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@TableName("message")
public class Message implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long chatId;
    private Long senderId;
    private Long toUserId;
    private String content;
    private LocalDateTime sentTime;
    private boolean isRead;
    private Integer messageType;
}
