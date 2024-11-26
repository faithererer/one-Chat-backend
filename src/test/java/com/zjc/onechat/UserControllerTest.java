package com.zjc.onechat;

import com.zjc.onechat.controller.UserController;
import com.zjc.onechat.entity.User;
import com.zjc.onechat.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;



    @Test
    void testGetUserInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true,\"errorMsg\":null,\"data\":{\"id\":1,\"phone\":\"13287604766\",\"nickname\":\"1111\"},\"total\":null}"));
    }

    @Test
    void testUpdateNickname() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/1/nickname")
                        .param("nickname", "new_nickname"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true,\"errorMsg\":null,\"data\":{\"id\":1,\"phone\":\"13287604766\",\"nickname\":\"new_nickname\"},\"total\":null}"));
    }
}