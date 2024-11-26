package com.zjc.onechat.dto;

public class FriendRequestDTO {
    private Long Id;
    private Long userId;
    private Long friendId;
    private String avatar;
    private String nickName;
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickName() {
        return nickName;
    }
}
