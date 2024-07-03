package com.blog.practiceapi.config;


import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.util.Base64;
@Getter
@ConfigurationProperties(prefix = "myblog")
public class StrDataConfig {

    private String jwtStrKey;
    private String adminEmail;

}
