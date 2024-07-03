package com.blog.practiceapi.config;


import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.util.Base64;
@Setter
@ConfigurationProperties(prefix = "myblog")
public class StrDataConfig {

    private String jwtStrKey;
    public String adminEmail;

    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(this.jwtStrKey));
    }
}
