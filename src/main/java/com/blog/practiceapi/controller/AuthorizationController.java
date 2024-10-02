package com.blog.practiceapi.controller;


import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.Sign;
import com.blog.practiceapi.service.AuthorizationService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService authorizationService;
    private final PostRepository postRepository;
    @PostMapping("/auth/login")
    public ResponseEntity<String> login() {

//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) { // null이 아니고 인증이 되어있는지
            // 로그인 성공
            return ResponseEntity.ok("로그인 성공!");
        } else {
            // 인증 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않습니다.");
        }
    }


    @RateLimiter(name = "sign")
    @PostMapping("/auth/sign")
    public void sign(@RequestBody Sign sign) {
        authorizationService.sign(sign);
    }



}
