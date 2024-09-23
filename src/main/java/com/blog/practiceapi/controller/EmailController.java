package com.blog.practiceapi.controller;

import com.blog.practiceapi.request.EmailRequest;
import com.blog.practiceapi.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/mail/request")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        log.info("이메일 >>>>>>>>>>>>>> 들어옴 {}", emailRequest);
        // 이메일 전송 로직
        String mailAddress = "testbanchan7@gmail.com";
        String title = "요청사항 및 문의";

        emailService.sendSimpleMessage(
                mailAddress, // 받는 사람 이메일
                title,         // 이메일 제목
                emailRequest.getMessage() // 요청에서 받은 메시지를 이메일 내용으로
        );
        return "이메일이 성공적으로 발송되었습니다.";
    }
}
