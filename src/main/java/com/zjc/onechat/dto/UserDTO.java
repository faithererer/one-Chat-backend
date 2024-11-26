package com.zjc.onechat.dto;

import lombok.Data;

@Data
public class UserDTO {
    String token;
    private Long id;
    private String phone;
    private String nickname;
    private String avatar;
    private Boolean isFriend;
}
