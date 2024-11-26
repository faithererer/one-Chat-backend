package com.zjc.onechat.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("verification_code")
@Data
public class VerificationCode {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String phone;
    private String code;
    private LocalDateTime createdAt;

    public VerificationCode() {}

    public VerificationCode(String phone, String code) {
        this.phone = phone;
        this.code = code;
        this.createdAt = LocalDateTime.now();
    }

    // getters and setters
}
