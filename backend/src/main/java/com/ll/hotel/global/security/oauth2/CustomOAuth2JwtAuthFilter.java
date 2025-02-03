package com.ll.hotel.global.security.oauth2;


import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.security.dto.SecurityUser;
import static com.ll.hotel.global.security.dto.SecurityUser.of;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomOAuth2JwtAuthFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = bearerToken.substring(7);

        try {
            if (memberService.verifyToken(token)) {
                String email = memberService.getEmail(token);
                Member findMember = memberRepository.findByMemberEmail(email)
                        .orElseThrow(() -> new ServiceException("MEMBER_NOT_FOUND", "해당 이메일의 회원이 존재하지 않습니다."));

                SecurityUser userDto = of(
                        findMember.getMemberId(),
                        findMember.getMemberName(),
                        findMember.getMemberEmail(),
                        "ROLE_" + findMember.getRole()
                );

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDto,
                        null,
                        userDto.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            log.error("JWT Token Processing Error: {}", e.getMessage());
            throw new ServiceException("TOKEN_INVALID", "유효하지 않은 토큰입니다.");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return request.getRequestURI().contains("api/users/refresh");
    }
}