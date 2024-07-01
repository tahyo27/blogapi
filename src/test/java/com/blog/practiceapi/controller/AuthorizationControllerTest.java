package com.blog.practiceapi.controller;

import com.blog.practiceapi.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("스프링 시큐리티 로그인 테스트")
    void jwt_login_test() throws Exception {
        //given
        String username = "psyduck";
        String password = "1234";


        //expected
        mockMvc.perform(post("/auth/login")
                .param("username", username)
                .param("password", password))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());



    }

    @Test
    @DisplayName("인증 Resolver 테스트")
    void resolver_test() throws Exception {
        //given

    }

}