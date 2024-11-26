package com.zjc.onechat.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.onechat.dto.UserDTO;
import com.zjc.onechat.entity.User;
import com.zjc.onechat.entity.VerificationCode;
import com.zjc.onechat.mapper.UserMapper;
import com.zjc.onechat.mapper.VerificationCodeMapper;
import com.zjc.onechat.utils.JwtTokenProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Log4j2
@Service
public class AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VerificationCodeMapper verificationCodeMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SmsService smsService;

    /**
     * 发送验证码到用户手机
     * @param phone 用户手机号
     */
    public boolean sendVerificationCode(String phone) throws Exception {
        String code = generateVerificationCode();
        VerificationCode verificationCode = new VerificationCode(phone, code);
        verificationCodeMapper.insert(verificationCode);
//        smsService.sendSms(phone, code);
        System.out.println(smsService.sendSms(phone, code)+"结果");
        log.debug(smsService.sendSms(phone, code)+"结果");
        return true;
    }

    /**
     * 验证验证码并登录
     * @param phone 用户手机号
     * @param code 验证码
     * @return JWT token
     */
    public UserDTO verifyCodeAndLogin(String phone, String code) {
        System.out.println("执行");
        VerificationCode verificationCode = verificationCodeMapper.selectOne(
                new QueryWrapper<VerificationCode>()
                        .eq("phone", phone)
                        .eq("code", code)
                        .orderByDesc("created_at")
                        .last("LIMIT 1")
        );

        if (verificationCode == null) {
            throw new RuntimeException("验证码错误或已过期");
        }
        // 验证成功，判定用户存在乎？
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
        System.out.println("用户状态"+user);
        if (user == null) {
            System.out.println("用户不存在");
            user = new User();
            user.setPhone(phone);
            user.setNickName("User_"+ RandomUtil.randomString(9));
            userMapper.insert(user);
        }
        String token = jwtTokenProvider.createToken(user.getId());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setToken(token);
        userDTO.setNickname(user.getNickName());
        userDTO.setPhone(user.getPhone());
        userDTO.setAvatar(user.getAvatar());
        return userDTO;
    }

    /**
     * 生成六位数的验证码
     * @return 验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}

