package com.ll.hotel.global.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.hotel.domain.member.member.service.RefreshTokenService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Ut ut;
    private final RefreshTokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        log.debug("OAuth2User attributes: {}", oauth2User.getAttributes());

        String email = extractEmail(oauth2User);
        if (email != null) {
            String accessToken = ut.generateAccessToken(email, "ROLE_USER");
            String refreshToken = ut.generateRefreshToken(email, "ROLE_USER");
            
            tokenService.saveTokenInfo(email, refreshToken, accessToken);
            log.debug("Saved OAuth2 refresh token for email: {}", email);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            RsData<Map<String, String>> rsData = new RsData<>("200-1", "로그인 성공", tokens);
            
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(rsData));
        } else {
            RsData<Void> rsData = new RsData<>("400-1", "이메일을 찾을 수 없습니다.");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString(rsData));
        }
    }

    private String extractEmail(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        // Google
        if (attributes.containsKey("email")) {
            return (String) attributes.get("email");
        }
        
        // Naver
        if (attributes.containsKey("response")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            if (response.containsKey("email")) {
                return (String) response.get("email");
            }
        }
        
        // Kakao
        if (attributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount.containsKey("email")) {
                return (String) kakaoAccount.get("email");
            }
        }
        
        return null;
    }
}