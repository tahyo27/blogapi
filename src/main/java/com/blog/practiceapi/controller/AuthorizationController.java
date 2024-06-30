package com.blog.practiceapi.controller;

import com.blog.practiceapi.config.data.MemberSession;
import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.response.SessionResponse;
import com.blog.practiceapi.service.AuthorizationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authService;
    private static final String SECRET_KEY = "6YehYF5iMhiqa+aNkhs69LWrPFwK/H+kJ4xuV5ndfAc=";

    @GetMapping("/testSession")
    public Long testSession(MemberSession memberSession) {
        return memberSession.getMemberId();
    }
    @PostMapping("/authorize/login")
    public SessionResponse login(@RequestBody Login login) {
        Long memberId = authService.login(login);

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
        String jws = Jwts.builder().subject(String.valueOf(memberId)).signWith(key).compact();

        log.info(">>>>>>>>>>>>>>{}", jws);
        return new SessionResponse(jws);
    }
}
