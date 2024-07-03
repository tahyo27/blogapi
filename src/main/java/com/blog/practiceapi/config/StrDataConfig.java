package com.blog.practiceapi.config;


import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
@Getter
@Component
public class StrDataConfig {

    @Value("${myblog.strKey}")
    private String strKey;
    @Value("${myblog.myEmail}")
    private String myEmail;
}
