package com.ll.hotel.global.security.oauth2;

import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.security.dto.SecurityUserDto;
import com.ll.hotel.standard.util.Ut;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomOAuth2JwtAuthFilter extends OncePerRequestFilter {

    private final Ut ut;
    private final MemberRepository memberRepository;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return request.getRequestURI().contains("token/refresh");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String atc = request.getHeader("Authorization");


        if (!StringUtils.hasText(atc)) {
            doFilter(request, response, filterChain);
            return;
        }

        if (!ut.verifyToken(atc)) {
            throw new ServiceException("TOKEN_EXPIRED", "Access Token 만료");
        }

        if (ut.verifyToken(atc)) {
            Member findMember = memberRepository.findByMemberEmail(ut.getUid(atc))
                    .orElseThrow(() -> new ServiceException("MEMBER_NOT_FOUND", "해당 이메일의 회원이 존재하지 않습니다."));

            SecurityUserDto userDto = SecurityUserDto.builder()
                    .memberNo(findMember.getMemberId())
                    .email(findMember.getMemberEmail())
                    .role("ROLE_".concat(findMember.getUserRole()))
                    .nickname(findMember.getMemberName())
                    .build();

            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }



    public Authentication getAuthentication(SecurityUserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                List.of(new SimpleGrantedAuthority(member.role())));
    }

}