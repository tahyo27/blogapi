package com.blog.practiceapi.controller;

import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.service.AuthorizationService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authService;

    @PostMapping("auth/login")
    public String login(@RequestBody Login login) {

        SecretKey key = Jwts.SIG.HS256.key().build();

        String jws = Jwts.builder().subject("Joe").signWith(key).compact();

        return jws;
    }
}
