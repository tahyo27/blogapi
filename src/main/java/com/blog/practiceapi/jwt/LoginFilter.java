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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        setFilterProcessesUrl("/auth/login"); // /login에서 변경
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", obtainUsername(request));
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(obtainUsername(request), //유저네임
                        obtainPassword(request), //패스워드
                        null); //롤
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info(">>>>>>>>>>>>>>>>> successfulAuthentication");

        UserDetails user = userDetailsService.loadUserByUsername(obtainUsername(request));
        String userName = user.getUsername();
        String userAuth = user.getAuthorities().toString();
        log.info(">>>>>>>>>>>>>>>>> UserDetails user {}", user);

        log.info(">>>>>>>>>>>>>>>>>>>>> username : {}, >>>>>>>>>>>>> userAuth : {}", userName, userAuth);

        String jwtToken = jwtUtil.createJwt(user.getUsername(),
                user.getAuthorities().toString(), 1800L);
        response.addHeader("Authorization", "Bearer " + jwtToken);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info(">>>>>>>>>>>>>>>> unsuccessfulAuthentication");
        throw new InvalidLogInException();
    }

}
