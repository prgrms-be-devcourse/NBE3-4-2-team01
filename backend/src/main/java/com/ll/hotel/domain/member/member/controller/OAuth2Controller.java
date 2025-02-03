package com.ll.hotel.domain.member.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuth2Controller {
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/oauth2/callback")
    public RsData<OAuth2Response> callback(
            @RequestParam(required = false) String accessToken,
            @RequestParam(required = false) String refreshToken,
            @RequestParam String status
    ) {
        if ("SUCCESS".equals(status)) {
            String token = accessToken.replace("Bearer ", "");
            String email = memberService.getEmail(token);
            Member member = memberService.findByMemberEmail(email)
                    .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
            return new RsData<>(
                "200-1",
                "OAuth2 로그인 성공",
                new OAuth2Response(
                    member.getMemberEmail(),
                    member.getMemberName(),
                    member.getProvider(),
                    status,
                    member.getUserRole(),
                    rq.getActor().isUser(),
                    rq.getActor().isAdmin(),
                    rq.getActor().isBusiness(),
                    accessToken,
                    refreshToken
                )
            );
        }
        
        return new RsData<>(
            "400-1",
            "잘못된 요청입니다.",
            null
        );
    }
}

record OAuth2Response(
    String email,
    String name,
    String provider,
    String status,
    String role,
    boolean isUser,
    boolean isAdmin,
    boolean isBusiness,
    String accessToken,
    String refreshToken
) {} 