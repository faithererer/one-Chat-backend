package com.zjc.onechat.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String phone;
    private String nickName;
    private String avatar;
    private Boolean isOnline;

}

