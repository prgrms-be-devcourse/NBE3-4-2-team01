package com.ll.hotel.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 모든 요청 허용
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .formLogin(login -> login.disable()) // 폼 로그인 비활성화
                .httpBasic(basic -> basic.disable()); // 기본 인증 비활성화

        return http.build();
    }
}
