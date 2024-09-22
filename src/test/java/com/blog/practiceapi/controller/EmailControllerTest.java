package com.blog.practiceapi.controller;

import com.blog.practiceapi.request.EmailRequest;
import com.blog.practiceapi.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "tahyo27@gmail.com", roles = {"ADMIN"})
@WebMvcTest(EmailController.class)
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;  // EmailService를 Mock으로 주입

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSendEmail() throws Exception {
        // EmailService의 sendSimpleMessage 메서드를 Mock으로 처리
        doNothing().when(emailService).sendSimpleMessage(anyString(), anyString(), anyString());

        // EmailRequest 객체 생성
        EmailRequest emailRequest = EmailRequest.builder()
                .email("asdf@asdfasdf")
                .name("이름")
                .message("들어갈 메세지입니다")
                .build();

        // JSON 형식으로 요청 본문을 변환
        String requestBody = objectMapper.writeValueAsString(emailRequest);

        // POST 요청을 보내고 응답 확인
        mockMvc.perform(post("/sendmail")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("이메일이 성공적으로 발송되었습니다."));
    }
}
