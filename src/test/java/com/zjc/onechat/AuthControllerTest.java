package com.zjc.onechat;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjc.onechat.controller.AuthController;
import com.zjc.onechat.mapper.UserMapper;
import com.zjc.onechat.mapper.VerificationCodeMapper;
import com.zjc.onechat.service.AuthService;
import com.zjc.onechat.service.SmsService;
import com.zjc.onechat.utils.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private SmsService smsService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private VerificationCodeMapper verificationCodeMapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ObjectMapper objectMapper;
    private String phone = "13000000";
    private String code = "123456";
    private String token = "test-token";

    @BeforeEach
    void setUp() throws Exception {
        given(authService.sendVerificationCode(phone)).willReturn(true);
        given(authService.verifyCodeAndLogin(phone, code)).willReturn(token);
    }

    @Test
    void testSendCode() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("phone", phone);
        String json = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/sendCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true,\"errorMsg\":null,\"data\":\"验证码已发送\",\"total\":null}"));
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .param("phone", phone)
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true,\"errorMsg\":null,\"data\":\"test-token\",\"total\":null}"));
    }
}