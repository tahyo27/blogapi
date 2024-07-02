package com.blog.practiceapi.config;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.jwt.LoginFilter;
import com.blog.practiceapi.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    public WebSecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
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
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authRequests) -> authRequests
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/sign").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable) //JWT 인증 사용할거기 때문에 폼로그인 및 http베이직 디스에이블
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 시큐리티가 세션 사용 X
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class); //user 인증부분에 넣을거라 at

//                .formLogin((formLogin) ->
//                        formLogin
//                                .loginPage("/auth/login")
//                                .loginProcessingUrl("/auth/login")
//                                .usernameParameter("username")
//                                .passwordParameter("password")
//                                .successForwardUrl("/")
//                )

//                .rememberMe(remember -> remember.rememberMeParameter("remember")
//                        .alwaysRemember(false)
//                        .tokenValiditySeconds(1296000)
//                );
                             // 스프링 6.1부터 메서드 체이닝말고 람다로 해야함
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return username -> {
            Member member = memberRepository.findByEmail(username) //email을 유저네임으로 사용
                    .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다"));

            String role = member.getEmail().equals("tahyo27@gmail.com") ? "ADMIN" : "USER";
            return new User(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority(role)));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //암호화방식 SCrypt 사용

        return new SCryptPasswordEncoder(8, 8, 1, 32, 64);
    }
}
