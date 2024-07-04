package com.blog.practiceapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StrDataConfig strDataConfig;

    @Override
    public void addCorsMappings(CorsRegistry registry) { // todo 시큐리티 쪽으로 합칠 예정
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173");
    }

}
