package com.blog.practiceapi.controller;


import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.Sign;
import com.blog.practiceapi.service.AuthorizationService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @RateLimiter(name = "sign")
    @PostMapping("/auth/sign")
    public void sign(@RequestBody Sign sign) {
        authorizationService.sign(sign);
    }


    @GetMapping("/testmyblog2")
    public void test2() { // 쿼리 튜닝 및 쿼리 테스트
//        SearchPagingPost searchPagingPost = SearchPagingPost.builder() // 페이지 깊어질수록 시간 늘어남
//                .page(10000)
//                .size(100)
//                .build();
//        long start = System.currentTimeMillis();
//        List<PostResponse> postResponses = postRepository.getPagingList(searchPagingPost)
//                .stream().map(PostResponse::new).toList();
//        long end = System.currentTimeMillis();
//
//        log.info(">>>>>>>>>>>>>>>>>>>>" + (end - start));
    }

    @GetMapping("/testmyblog")
    public void test3() { // 쿼리 튜닝 및 쿼리 테스트
//        Long cursor = 10000L;
//        long start = System.currentTimeMillis();
//        List<PostResponse> postResponses = postRepository.getCursorPaging(cursor)
//                .stream().map(PostResponse::new).toList();
//        long end = System.currentTimeMillis();
//        log.info(">>>>>>>>>>>>>>>>>>>>" + (end - start));

    }


}
