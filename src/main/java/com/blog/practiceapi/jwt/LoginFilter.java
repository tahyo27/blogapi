package com.blog.practiceapi.jwt;

import com.blog.practiceapi.exception.InvalidLogInException;
import com.blog.practiceapi.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        setFilterProcessesUrl("/auth/login"); // /login에서 변경
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", obtainUsername(request));
        //유저 이름과 패스워드 검증
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(obtainUsername(request), //유저네임
                        obtainPassword(request), //패스워드
                        null); //롤
        //검증을 위해 인증 매니저로 전달
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info(">>>>>>>>>>>>>>>>> successfulAuthentication");
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();
        log.info(">>>>>>>>>>>>>>>>> UserDetails username {}", username);
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        log.info(">>>>>>>>>>>>>>>>> UserDetails username {}", role);
        String jwtToken = jwtUtil.createJwt(username,
                role, 1800L);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> jwtToken = {}", jwtToken);
        response.addHeader("Authorization", "Bearer " + jwtToken);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info(">>>>>>>>>>>>>>>> unsuccessfulAuthentication");
        throw new InvalidLogInException();
    }

}
