package com.ll.hotel.global.security.oauth2;

import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.global.security.oauth2.dto.SecurityUser;
import com.ll.hotel.global.security.oauth2.repository.OAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final OAuthRepository oAuthRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("OAuth2 로그인 시도");
        log.debug("Provider: {}", userRequest.getClientRegistration().getRegistrationId());
        log.debug("Access Token: {}", userRequest.getAccessToken().getTokenValue());
        log.debug("Additional Parameters: {}", userRequest.getAdditionalParameters());

        try {
            OAuth2User oauth2User = super.loadUser(userRequest);
            log.debug("OAuth2User Attributes: {}", oauth2User.getAttributes());
            return processOAuth2User(userRequest, oauth2User);
        } catch (Exception e) {
            throw new OAuth2AuthenticationException("OAuth2 로그인 처리 중 오류가 발생했습니다.");
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        String email;
        String name;
        String oauthId;
        
        switch (registrationId) {
            case "naver" -> {
                Map<String, Object> response = (Map<String, Object>) oauth2User.getAttributes().get("response");
                if (response == null) {
                    throw new OAuth2AuthenticationException("Invalid Naver response");
                }
                email = (String) response.get("email");
                name = (String) response.get("name");
                oauthId = (String) response.get("id");
                log.debug("네이버 로그인 정보 - email: {}, name: {}, id: {}", email, name, oauthId);
            }
            case "kakao" -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) oauth2User.getAttributes().get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                email = (String) kakaoAccount.get("email");
                name = (String) profile.get("nickname");
                oauthId = String.valueOf(oauth2User.getAttributes().get("id"));
                log.debug("카카오 로그인 정보 - email: {}, name: {}, id: {}", email, name, oauthId);
            }
            case "google" -> {
                email = oauth2User.getAttribute("email");
                name = oauth2User.getAttribute("name");
                oauthId = oauth2User.getAttribute("sub");
                log.debug("구글 로그인 정보 - email: {}, name: {}, id: {}", email, name, oauthId);
            }
            default -> throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }

        if (email == null || name == null || oauthId == null) {
            throw new OAuth2AuthenticationException("Required attributes are missing");
        }

        boolean isNewUser = !oAuthRepository.existsByProviderAndOauthId(registrationId, oauthId);
        
        return new SecurityUser(
            email,
            name,
            registrationId,
            isNewUser,
            oauth2User.getAttributes(),
            oauth2User.getAuthorities(),
            oauthId
        );
    }
}
