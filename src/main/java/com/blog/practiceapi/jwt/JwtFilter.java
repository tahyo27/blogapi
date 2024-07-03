package com.blog.practiceapi.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");

        if(auth == null || auth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // 다음 필터로 넘김
            return; //메서드 종료
        }
        String jwtToken = auth.split(" ")[1];

        if(jwtUtil.isExpired(jwtToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        JwtTempUser jwtTempUser = JwtTempUser.builder()
                .username(jwtUtil.getUsername(jwtToken))
                .role(jwtUtil.getRole(jwtToken))
                .build();

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>> jwtTempUser = {}", jwtTempUser.toString());

        User user = new User(jwtTempUser.getUsername(), jwtTempUser.getPassword(), List.of(new SimpleGrantedAuthority(jwtTempUser.getRole())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
