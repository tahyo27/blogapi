package com.blog.practiceapi.controller;


import com.blog.practiceapi.request.Sign;
import com.blog.practiceapi.response.ErrorResponse;
import com.blog.practiceapi.service.AuthorizationService;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService authorizationService;


    @GetMapping("/authTest")
    public String test() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        log.info(">>>>>>>>>>>>>>>>>>>> username={}, >>> role={}", username, role);
        
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



    @GetMapping("/testmyblog")
    @RateLimiter(name = "backendA")
    public String test2() {
        
        return "테스트임";
    }

//    public ResponseEntity<ErrorResponse> fallbacktest(String param1, Throwable t) {
//        log.info("asdfasdf" + t.toString());
//        log.info(">>>>>>>>>>>>signFallback 콜");
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .code("429")
//                .msg("한꺼번에 너무 많은 요청입니다.")
//                .build();
//        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
//    }

}
