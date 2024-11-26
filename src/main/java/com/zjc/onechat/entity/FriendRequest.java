package com.zjc.onechat.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("friend_request")
@Data
public class FriendRequest {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long friendId;
    private LocalDateTime createdAt;

    public FriendRequest() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
}
