package com.blog.practiceapi.controller;

import com.blog.practiceapi.config.data.MemberSession;
import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.response.SessionResponse;
import com.blog.practiceapi.service.AuthorizationService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authService;

    @GetMapping("/testSession")
    public String testSession(MemberSession memberSession) {
        return memberSession.name;
    }
    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {

        SecretKey key = Jwts.SIG.HS256.key().build();

        String jws = Jwts.builder().subject("Joe").signWith(key).compact();

        return new SessionResponse(jws);
    }
}
