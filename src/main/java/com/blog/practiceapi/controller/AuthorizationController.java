package com.blog.practiceapi.controller;


import com.blog.practiceapi.request.Sign;
import com.blog.practiceapi.service.AuthorizationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @GetMapping("/authTest")
    public String test() {
        return "테스트 주소";
    }
    @PostMapping("/auth/login")
    public String login() {
        return "로그인 페이지";
    }

    @PostMapping("/auth/sign")
    public void sign(@RequestBody Sign sign) {
        authorizationService.sign(sign);
    }
}
