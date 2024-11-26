package com.zjc.onechat.controller;

import com.zjc.onechat.dto.AvatarDTO;
import com.zjc.onechat.dto.Result;
import com.zjc.onechat.entity.User;
import com.zjc.onechat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    public Result getUserInfo(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        System.out.println(user);
        return Result.ok(user);
    }

    /**
     * 更新用户昵称

     */
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return Result.ok(userService.update(user));
    }

    @PostMapping("/avatar")
    public Result getAvator(@RequestBody AvatarDTO avatarDTO){
        return Result.ok(userService.getUserByPhone(avatarDTO.getPhone()));
    }
}