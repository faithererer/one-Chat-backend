package com.zjc.onechat.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("friend")
@Data
public class Friend {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long friendId;


}
