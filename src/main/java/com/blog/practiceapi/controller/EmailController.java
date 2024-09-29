package com.blog.practiceapi.controller;

import com.blog.practiceapi.request.EmailRequest;
import com.blog.practiceapi.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/mail/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        // 이메일 전송
        try {
            String mailAddress = "testbanchan7@gmail.com";
            String title = "요청사항 및 문의";

            emailService.sendSimpleMessage(mailAddress, title, emailRequest.getMessage());
            
            return ResponseEntity.ok("이메일 전송 성공!");
        } catch (Exception e) {
            // 이메일 전송 실패 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패: " + e.getMessage()); //todo 익셉션 따로 만들지 고민
        }
    }
}
