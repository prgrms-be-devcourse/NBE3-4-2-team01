package com.ll.hotel.global.security.oauth2;


import com.ll.hotel.domain.member.member.service.AuthTokenService;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.global.security.oauth2.dto.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthTokenService authTokenService;
    private final MemberService memberService;

    @Value("${app.oauth2.authorizedRedirectUris}")
    private String authorizedRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        
        if (securityUser.isNewUser()) {
            String redirectUrl = UriComponentsBuilder.fromUriString(authorizedRedirectUri)
                    .queryParam("email", URLEncoder.encode(securityUser.getEmail(), StandardCharsets.UTF_8))
                    .queryParam("name", URLEncoder.encode(securityUser.getUsername(), StandardCharsets.UTF_8))
                    .queryParam("provider", URLEncoder.encode(securityUser.getProvider(), StandardCharsets.UTF_8))
                    .queryParam("status", "REGISTER")
                    .build()
                    .encode()
                    .toUriString();

            response.sendRedirect(redirectUrl);
        } else {
            String accessToken = authTokenService.generateToken(securityUser.getEmail()).accessToken();
            String refreshToken = memberService.generateRefreshToken(securityUser.getEmail());
            
            authTokenService.generateToken(securityUser.getEmail());

            String redirectUrl = UriComponentsBuilder.fromUriString(authorizedRedirectUri)
                    .queryParam("accessToken", "Bearer " + accessToken)
                    .queryParam("refreshToken", "Bearer " + refreshToken)
                    .queryParam("status", "SUCCESS")
                    .build()
                    .encode()
                    .toUriString();
            
            response.sendRedirect(redirectUrl);
        }
    }
}