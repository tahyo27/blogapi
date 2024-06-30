package com.blog.practiceapi.config;

import com.blog.practiceapi.config.data.MemberSession;
import com.blog.practiceapi.exception.NotAuthenticated;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationResolver implements HandlerMethodArgumentResolver {

    private final StrDataConfig strDataConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) { // 원하는 파라미터 체크
        return parameter.getParameterType().equals(MemberSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(strDataConfig.jwtStrKey));

        if(jws != null && jws.isEmpty()) {
            throw new NotAuthenticated();
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jws);
            //OK, we can trust this JWT
            log.info(">>>>>>>>>>{}", claimsJws);
            String memberId = claimsJws.getPayload().getSubject();
            return new MemberSession(Long.parseLong(memberId));
        } catch (JwtException e) {
            //don't trust the JWT!
            throw new NotAuthenticated();
        }
    }
}
