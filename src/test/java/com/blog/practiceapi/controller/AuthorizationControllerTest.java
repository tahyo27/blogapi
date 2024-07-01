package com.blog.practiceapi.controller;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.repository.MemberRepository;
import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.request.Sign;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    private String buildUrlEncodedFormEntity(String... params) { //form용 body에 넣는 메서드
        if( (params.length % 2) > 0 ) {
            throw new IllegalArgumentException("Need to give an even number of parameters");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            if( i > 0 ) {
                result.append('&');
            }
            result.
                    append(URLEncoder.encode(params[i], StandardCharsets.UTF_8)).
                    append('=').
                    append(URLEncoder.encode(params[i+1], StandardCharsets.UTF_8));
        }
        return result.toString();
    }

    @Test
    @DisplayName("인증 컨트롤러 로그인 폼 테스트")
    void security_login_test() throws Exception {
        //given
        String username = "psyduck";
        String password = "1234";
        String remember = "1";

        Sign sign = Sign.builder()
                .name("psyduck")
                .email("aaaa@naver.com")
                .password("asdf")
                .build();

        String json = new ObjectMapper().writeValueAsString(sign);

        mockMvc.perform(post("/auth/sign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        
        //expected

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(buildUrlEncodedFormEntity(
                                "username", "aaaa@naver.com",
                                "password", "asdf",
                                "remember", "1"
                        )))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }



    @Test
    @DisplayName("인증 컨트롤러 회원가입 테스트")
    void security_sign_test() throws Exception {
        //given
        Sign sign = Sign.builder()
                .name("psyduck")
                .email("aaaa@naver.com")
                .password("asdf")
                .build();

        String json = new ObjectMapper().writeValueAsString(sign);

        //expected

        mockMvc.perform(post("/auth/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

}