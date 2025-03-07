package com.ll.hotel.global.security;


import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.global.jwt.JwtAuthFilter;
import com.ll.hotel.global.jwt.exception.JwtExceptionFilter;
import com.ll.hotel.global.security.cors.CorsProperties;
import com.ll.hotel.global.security.oauth2.CustomOAuth2AuthenticationSuccessHandler;
import com.ll.hotel.global.security.oauth2.CustomOAuth2AuthorizationRequestRepository;
import com.ll.hotel.global.security.oauth2.CustomOAuth2FailureHandler;
import com.ll.hotel.global.security.oauth2.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomOAuth2FailureHandler oAuth2AuthenticationFailureHandler;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CustomOAuth2AuthorizationRequestRepository customOAuth2AuthorizationRequestRepository;
    private final CorsProperties corsProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin()
                        )
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(
                                "/",
                                "/error",
                                "/favicon.ico",
                                "/h2-console/**",
                                "/api/auth/**",
                                "/api/users/join",
                                "/api/users/login",
                                "/api/users/refresh",
                                "/api/hotels/**",
                                "/api/bookings/**",
                                "/api/favorites/**",
                                "/api/reviews/**",
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**",
                                "/api/*/oauth2/callback"
                        ).permitAll()

                        // 관리자 전용
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 사업자 전용
                        .requestMatchers("/api/businesses/register").hasAnyRole("USER", "BUSINESS")
                        .requestMatchers("/api/businesses/**").hasRole("BUSINESS")

                        // 애플리케이션에만 나머지 인증 요구
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write(String.format(
                                    "{\"resultCode\": \"%d-1\", \"msg\": \"%s\", \"data\": null}",
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    "사용자 인증정보가 올바르지 않습니다."
                            ));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write(String.format(
                                    "{\"resultCode\": \"%d-1\", \"msg\": \"%s\", \"data\": null}",
                                    HttpServletResponse.SC_FORBIDDEN,
                                    "접근 권한이 없습니다."
                            ));
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Login -> oauth2Login
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestRepository(customOAuth2AuthorizationRequestRepository)
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .addFilterBefore(new JwtExceptionFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthFilter(memberService, memberRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}