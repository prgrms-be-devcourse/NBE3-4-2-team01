package com.ll.hotel.global.rq;


import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.global.security.dto.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final MemberRepository memberRepository;
    private Member actor;

    public Member getActor() {
        if (actor == null) {
            actor = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                    .map(Authentication::getPrincipal)
                    .filter(principal -> principal instanceof SecurityUser)
                    .map(principal -> (SecurityUser) principal)
                    .map(securityUser -> memberRepository.findByMemberEmail(securityUser.getEmail())
                            .orElse(null))
                    .orElse(null);
        }
        return actor;
    }
}
