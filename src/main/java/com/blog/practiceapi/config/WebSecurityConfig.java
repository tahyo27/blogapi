package com.blog.practiceapi.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                        .requestMatchers("/auth/login").permitAll()
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
                .userDetailsService(userDetailsService());
                             // 스프링 6.1부터 메서드 체이닝말고 람다로 해야함
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
        UserDetails user = User.withUsername("psyduck")
                .password("1234")
                .roles("ADMIN")
                .build();

        detailsManager.createUser(user);
        return detailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
