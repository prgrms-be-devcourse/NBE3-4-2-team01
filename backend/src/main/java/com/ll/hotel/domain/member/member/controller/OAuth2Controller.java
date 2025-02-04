package com.ll.hotel.domain.member.member.controller;

import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.global.security.oauth2.entity.OAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuth2Controller {
    private final Rq rq;

    @GetMapping("/oauth2/callback")
    public RsData<OAuth2Response> callback(
            @RequestParam(required = false) String accessToken,
            @RequestParam(required = false) String refreshToken,
            @RequestParam String status
    ) {
        if ("SUCCESS".equals(status)) {
            Member actor = rq.getActor();
            
            if (actor != null) {
                OAuth oAuth = actor.getFirstOAuth();
                
                if (oAuth == null) {
                    return new RsData<>("400", "OAuth 정보를 찾을 수 없습니다.", null);
                }
                
                return new RsData<>(
                    "200",
                    "OAuth2 로그인 성공",
                    new OAuth2Response(
                        actor.getMemberEmail(),
                        actor.getMemberName(),
                        status,
                        actor.getUserRole(),
                        oAuth.getProvider(),
                        oAuth.getOauthId(),
                        actor.isUser(),
                        actor.isAdmin(),
                        actor.isBusiness(),
                        accessToken,
                        refreshToken
                    )
                );
            }
            return new RsData<>("401", "사용자를 찾을 수 없습니다.", null);
        }
        
        return new RsData<>("400", "잘못된 요청입니다.", null);
    }
}

record OAuth2Response(
    String email,
    String name,
    String status,
    String role,
    String provider,
    String oauthId,
    boolean isUser,
    boolean isAdmin,
    boolean isBusiness,
    String accessToken,
    String refreshToken
) {} 