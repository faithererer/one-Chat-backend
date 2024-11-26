package com.zjc.onechat.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.onechat.entity.User;
import com.zjc.onechat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }
    public String getUserByPhone(String phone) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("phone", phone));
        if(user==null)
            return null;
        if(user.getAvatar()==null) {
            return null;
        }
        System.out.println(user.getAvatar());
        return user.getAvatar();
    }
    /**
     * 更新用户昵称

     * @return 更新后的用户信息
     */
    public String update(User user) {
        if (user != null) {
            System.out.println(user);
            userMapper.updateById(user);
        }
        return "ok";
    }
    public void updateUserStatus(Long id, Boolean isOnline) {
        User user = userMapper.selectById(id);
        user.setIsOnline(isOnline);
        userMapper.updateById(user);
    }
}
