package com.blog.practiceapi.controller;

import com.blog.practiceapi.repository.MemberRepository;
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
    @DisplayName("스프링 시큐리티 로그인 폼 테스트")
    void jwt_login_test() throws Exception {
        //given
        String username = "psyduck";
        String password = "1234";
        String remember = "1";

        
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(buildUrlEncodedFormEntity(
                                "username", "psyduck",
                                "password", "1234",
                                "remember", "1"
                        )))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }



    @Test
    @DisplayName("인증 Resolver 테스트")
    void resolver_test() throws Exception {
        //given

    }

}