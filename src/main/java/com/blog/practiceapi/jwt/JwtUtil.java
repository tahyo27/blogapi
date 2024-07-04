package com.blog.practiceapi.jwt;

import com.blog.practiceapi.config.StrDataConfig;
import com.blog.practiceapi.exception.JwtNotFoundException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtil {
    private final SecretKey secretKey;

    public JwtUtil(StrDataConfig strDataConfig) {
        log.info(">>>>>>>>>>>>>>>>>>>> JwtUTIL 생성 = {}", strDataConfig.toString());
        String jwtStrKey = Optional.ofNullable(strDataConfig.getStrKey())
                .orElseThrow(JwtNotFoundException::new);
        this.secretKey = new SecretKeySpec(jwtStrKey
                .getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return  Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) { //만료 시간 체크

        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload().getExpiration()
                .before(Date.from(LocalDateTime.now()
                        .atZone(ZoneId.systemDefault()).toInstant()));
    }

    public String createJwt(String username, String role, Long expired) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(LocalDateTime.now().plusSeconds(expired)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(secretKey)
                .compact();
    }

}
