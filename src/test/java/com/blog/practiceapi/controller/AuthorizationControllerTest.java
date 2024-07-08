package com.blog.practiceapi.controller;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.repository.MemberRepository;
import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.request.Sign;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void clean() {
        memberRepository.deleteAll();
    }

    /*
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

     */

    @Test
    @DisplayName("인증 컨트롤러 로그인 json 성공 테스트")
    void security_login_success_test() throws Exception {
        //given
        String password = passwordEncoder.encode("1234");
        Member member = Member.builder()
                .name("phyduck")
                .email("asdf@naver.com")
                .password(password) // 비밀번호 인코딩 해서 들어감
                .build();

        memberRepository.save(member);

        Login login = Login.builder()
                .email("asdf@naver.com")
                .password("1234")
                .build();

        String json = new ObjectMapper().writeValueAsString(login);

        //expected

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andDo(MockMvcResultHandlers.print());


    }


    @Test
    @DisplayName("인증 컨트롤러 로그인 json 실패 테스트")
    void security_login_fail_test() throws Exception {
        //given
        String password = passwordEncoder.encode("1234");
        Member member = Member.builder()
                .name("phyduck")
                .email("asdf@naver.com")
                .password(password) // 비밀번호 인코딩 해서 들어감
                .build();

        memberRepository.save(member);

        Login login = Login.builder()
                .email("asdf@naver.com")
                .password("12345")
                .build();

        String json = new ObjectMapper().writeValueAsString(login);

        //expected

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", Matchers.is("아이디/비밀번호가 잘못못되었습니다")))
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