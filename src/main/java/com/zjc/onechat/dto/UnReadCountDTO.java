package com.zjc.onechat.dto;

import com.zjc.onechat.entity.Message;
import lombok.Data;

@Data
public class UnReadCountDTO {
    Integer count;
    Message message;
}
