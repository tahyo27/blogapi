package com.blog.practiceapi.config;

import com.blog.practiceapi.domain.Member;
import com.blog.practiceapi.repository.MemberRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

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
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/sign").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/auth/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .successForwardUrl("/")
                )
                .rememberMe(remember -> remember.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(1296000)
                );
                             // 스프링 6.1부터 메서드 체이닝말고 람다로 해야함
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다"));
            return new User(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("ADMIN")));
        };
//        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
//        UserDetails user = User.withUsername("psyduck")
//                .password("1234")
//                .roles("ADMIN")
//                .build();
//
//        detailsManager.createUser(user);
//        return detailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new SCryptPasswordEncoder(8, 8, 1, 32, 64);
    }
}
