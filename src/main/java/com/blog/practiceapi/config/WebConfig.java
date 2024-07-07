package com.blog.practiceapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) { // todo 시큐리티 쪽으로 합칠 수 있나?
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173");
    }


}
