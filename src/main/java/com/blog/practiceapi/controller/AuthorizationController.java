package com.blog.practiceapi.controller;


import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.Sign;
import com.blog.practiceapi.service.AuthorizationService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService authorizationService;
    private final PostRepository postRepository;
    @PostMapping("/auth/login")
    public ResponseEntity<String> login() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        //todo 로그인 성공 어떻게 판단할건지 고민
        if (!role.isEmpty()) {
            return ResponseEntity.ok("로그인 성공!"); // 200 상태와 메세지 //todo 나중에 따로 분리하기
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않습니다."); // 401 상태와 메시지
        }
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
