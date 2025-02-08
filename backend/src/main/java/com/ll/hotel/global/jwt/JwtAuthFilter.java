package com.ll.hotel.global.jwt;


import java.io.IOException;

import org.springframework.core.Ordered;
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
import com.ll.hotel.global.security.oauth2.dto.SecurityUser;
import static com.ll.hotel.global.security.oauth2.dto.SecurityUser.of;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter implements Ordered {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        // 토큰 검증 및 처리
        if (StringUtils.hasText(accessToken)) {
            try {
                String email = memberService.extractEmailIfValid(accessToken);
                log.debug("Extracted email from token: {}", email);

                Member member = memberRepository.findByMemberEmail(email)
                    .orElseThrow(() -> new ServiceException("404-1", "해당 회원이 존재하지 않습니다."));
                log.debug("Found member: {}", member.getMemberEmail());

                SecurityUser userDto = of(
                    member.getId(),
                    member.getMemberName(),
                    member.getMemberEmail(),
                    "ROLE_" + member.getRole()
                );

                Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDto,
                    null,
                    userDto.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                log.error("JWT Token Processing Error: {}", e.getMessage());
                throw new ServiceException("401-1", "유효하지 않은 토큰입니다.");
            }
        }
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.contains("api/users/refresh") || 
               (requestURI.contains("/oauth2/callback") && request.getParameter("accessToken") == null);
    }
}