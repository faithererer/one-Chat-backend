package com.zjc.onechat.controller;

import com.zjc.onechat.dto.LoginDTO;
import com.zjc.onechat.dto.Result;
import com.zjc.onechat.dto.UserDTO;
import com.zjc.onechat.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    /**
     * 发送验证码
     * @param requestBody 手机号
     * @return 成功或失败消息
     */
    @PostMapping("/sendCode")
    public ResponseEntity<Result> sendCode(@RequestBody Map<String, String> requestBody) {
        String phone = requestBody.get("phone");
        System.out.println(phone);

        try {
            authService.sendVerificationCode(phone);
            return ResponseEntity.ok(Result.ok("验证码已发送"));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.fail("验证码发送失败：" + e.getMessage()));
        }
    }

    /**
     * 验证码登录
     * @param loginDTO 封装验证码和手机号
     * @return JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("老了");
        try {
            UserDTO userDTO = authService.verifyCodeAndLogin(loginDTO.getPhone(), loginDTO.getCode());
            System.out.println(userDTO);
            return ResponseEntity.ok(Result.ok(userDTO));
        } catch (Exception e) {
            return ResponseEntity.ok(Result.fail("登录失败：" + e.getMessage()));
        }
    }
}
