package com.blog.practiceapi.controller;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.repository.MemberRepository;
import com.blog.practiceapi.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("로그인시 JWT 반환 테스트")
    void jwt_login_return_test() throws Exception {
        //given
        Member member = Member.builder()
                .name("psyduck")
                .email("ddd@naver.com")
                .password("1234")
                .build();
        memberRepository.save(member); //멤버 저장 후 로그인

        Login login = Login.builder()
                .email("ddd@naver.com")
                .password("1234")
                .build();

        String json = null;

        json = new ObjectMapper().writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());



    }
}