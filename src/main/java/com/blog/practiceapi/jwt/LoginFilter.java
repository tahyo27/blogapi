package com.blog.practiceapi.jwt;

import com.blog.practiceapi.exception.InvalidLogInException;
import com.blog.practiceapi.exception.InvalidRequest;
import com.blog.practiceapi.request.Login;
import com.blog.practiceapi.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.UTF8;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        setFilterProcessesUrl("/auth/login"); // /login에서 변경
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> attemptAuthentication");
        //로그인 형식 JSON으로
        try {
            Login login = objectMapper.readValue(request.getInputStream(), Login.class);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> login filter = {}", login.toString());
            //유저 이름과 패스워드 검증
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(login.getEmail(), //유저네임
                            login.getPassword(), //패스워드
                            null); //롤


            //검증을 위해 인증 매니저로 전달
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            log.error("json 잘못됨");
            throw new InvalidRequest();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info(">>>>>>>>>>>>>>>>> successfulAuthentication");
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();
        log.info(">>>>>>>>>>>>>>>>> UserDetails username {}", username);
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();  //todo 간소화 고민
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        log.info(">>>>>>>>>>>>>>>>> UserDetails username {}", role);
        String jwtToken = jwtUtil.createJwt(username,
                role, 1800L);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> jwtToken = {}", jwtToken);
        response.addHeader("Authorization", "Bearer " + jwtToken);
        response.addHeader("username", username);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info(">>>>>>>>>>>>>>>> unsuccessfulAuthentication");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("401")
                .msg("아이디/비밀번호가 잘못못되었습니다")
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");     //todo 간소화할 수 있을 지 고민
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
