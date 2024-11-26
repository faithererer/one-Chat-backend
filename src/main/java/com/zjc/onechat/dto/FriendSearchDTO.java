package com.zjc.onechat.dto;

import lombok.Data;

@Data
public class FriendSearchDTO {
    private String nickName;
    private String avatar;
    private Long id;
    private Boolean isFriend;

}
