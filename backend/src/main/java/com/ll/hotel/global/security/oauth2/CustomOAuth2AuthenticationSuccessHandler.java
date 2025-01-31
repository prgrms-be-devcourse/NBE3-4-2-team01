package com.ll.hotel.global.security;

import com.ll.hotel.global.security.dto.GeneratedToken;
import com.ll.hotel.standard.util.Ut;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final Ut ut;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) {
            log.warn("응답이 이미 커밋되었습니다. 리다이렉트를 수행할 수 없습니다.");
            return;
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        try {
            String email = oAuth2User.getAttribute("email");
            String provider = oAuth2User.getAttribute("provider");
            boolean isExist = oAuth2User.getAttribute("exist");
            
            if (email == null || provider == null) {
                log.error("필수 OAuth2 속성이 누락되었습니다. email: {}, provider: {}", email, provider);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 OAuth2 속성이 누락되었습니다.");
                return;
            }

            String role = oAuth2User.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("사용자 권한이 없습니다."))
                    .getAuthority();

            String targetUrl;
            if (isExist) {
                GeneratedToken token = ut.generateToken(email, role);
                log.info("JWT 토큰 생성 성공: {}", token.accessToken());

                targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/")
                        .queryParam("accessToken", token.accessToken())
                        .build()
                        .encode(StandardCharsets.UTF_8)
                        .toUriString();
            } else {
                targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/users/join")
                        .queryParam("email", email)
                        .queryParam("provider", provider)
                        .build()
                        .encode(StandardCharsets.UTF_8)
                        .toUriString();
            }

            if (!response.isCommitted()) {
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
            }
        } catch (Exception e) {
            log.error("인증 성공 처리 중 오류 발생", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다.");
        }
    }
}