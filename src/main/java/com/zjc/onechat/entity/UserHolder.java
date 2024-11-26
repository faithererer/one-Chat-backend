package com.zjc.onechat.entity;

import lombok.Data;

@Data
public class UserHolder {
    private static ThreadLocal<Long> userId = new ThreadLocal<>();

    public static void setUserId(Long id) {
        userId.set(id);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static void clearUserId() {
        userId.remove();
    }
}
