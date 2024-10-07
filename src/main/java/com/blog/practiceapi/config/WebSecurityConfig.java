package com.blog.practiceapi.config;

import com.blog.practiceapi.jwt.JwtFilter;
import com.blog.practiceapi.jwt.JwtUtil;
import com.blog.practiceapi.jwt.LoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    public WebSecurityConfig(AuthenticationConfiguration authenticationConfiguration, ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers("/error")
                .requestMatchers("/", "/index")
                .requestMatchers("/mail/send")
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authRequests) -> authRequests
                        .requestMatchers("/auth/login", "/posts", "/docs/index.html", "/posts/{postId}/comments", "/posts/{postId}" ).permitAll() //todo 테스트 용 주소들 나중에 삭제
                        .requestMatchers("/temp/image", "/temp/image/{filename}").permitAll() //todo 나중에 admin으로 변경 필요
                        .requestMatchers(HttpMethod.POST, "/auth/sign").permitAll()
                        .requestMatchers("/authTest").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable) //JWT 인증 사용할거기 때문에 폼로그인 및 http베이직 디스에이블
                .httpBasic(AbstractHttpConfigurer::disable)

                 
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, objectMapper), UsernamePasswordAuthenticationFilter.class) //user 인증부분에 넣을거라 at
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //위치 중요
        // 스프링 6.1부터 메서드 체이닝말고 람다로 해야함
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //암호화방식 SCrypt 사용

        return new SCryptPasswordEncoder(8, 8, 1, 32, 64);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173/"); // 허용할 Origin 설정
        configuration.addAllowedMethod("*"); // 허용할 HTTP Method 설정
        configuration.addAllowedHeader("*"); // 허용할 Header 설정
        configuration.setAllowCredentials(true); // 자격 증명 허용 (필요시 설정)

        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("username");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // CORS 설정 등록
        return source;
    }
}
