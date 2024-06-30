package com.blog.practiceapi.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "myblog")
public class StrDataConfig {

    public String jwtStrKey;
}
