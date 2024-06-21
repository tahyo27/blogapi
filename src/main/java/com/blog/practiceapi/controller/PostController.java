package com.blog.practiceapi.controller;

import com.blog.practiceapi.request.PostCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate postCreate) throws Exception {
        //1. 매번 매서드마다 값을 검증 해야한다.
        //       > 까먹을 수 있다.
        //       > 검증 부분에서 버그가 발생할 여지가 높다
        //       > 지겹다
        //2. 응답값에 HashMap -> 응답 클래스를 만들어주는게 좋다.
        //3. 여러개의 에러처리 힘듦
        //4. 세번이상 반복작업 시 자동화 방법 생각


        return Map.of();
    }
}
