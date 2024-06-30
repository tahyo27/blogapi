package com.blog.practiceapi.controller;

import com.blog.practiceapi.config.StrDataConfig;
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

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authService;
    private final StrDataConfig strDataConfig;

    @GetMapping("/testSession")
    public Long testSession(MemberSession memberSession) {
        return memberSession.getMemberId();
    }
    @PostMapping("/authorize/login")
    public SessionResponse login(@RequestBody Login login) {
        Long memberId = authService.login(login);

        // 현재 날짜를 얻기 위한 Calendar 객체 생성
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 날짜와 시간을 0으로 설정한 Date 객체 생성
        Date now = calendar.getTime();

        String jws = Jwts.builder()
                .subject(String.valueOf(memberId))
                .signWith(strDataConfig.secretKey())
                .issuedAt(now)
                .compact();

        log.info(">>>>>>>>>>>>>>{}", jws);
        return new SessionResponse(jws);
    }
}
