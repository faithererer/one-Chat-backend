package com.zjc.onechat.service;


import com.zjc.onechat.entity.User;

public interface IUserService {

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);


}

