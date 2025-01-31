package com.ll.hotel.domain.member.member.controller;

import com.ll.hotel.domain.member.member.dto.MemberDTO;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.RefreshTokenRepository;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.domain.member.member.service.RefreshTokenService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.global.security.dto.GeneratedToken;
import com.ll.hotel.global.security.dto.RefreshToken;
import com.ll.hotel.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final RefreshTokenRepository tokenRepository;
    private final RefreshTokenService tokenService;
    private final Ut ut;

    @PostMapping("token/logout")
    public RsData<String> logout(@RequestHeader("Authorization") final String accessToken) {
        try {
            if (!ut.verifyToken(accessToken)) {
                return new RsData<>("401-1", "Access Token 만료", "");
            }

            String email = ut.getUid(accessToken);
            log.debug("Logging out user with email: {}", email);  // 로그 추가

            // Redis에서 토큰 삭제
            try {
                tokenService.removeRefreshToken(email);
                return new RsData<>("200-1", "로그아웃이 완료되었습니다.", "");
            } catch (Exception e) {
                log.error("Error during logout for email {}: {}", email, e.getMessage());
                return new RsData<>("500-1", "로그아웃 처리 중 오류가 발생했습니다.", "");
            }
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            return new RsData<>("500-1", "로그아웃 처리 중 오류가 발생했습니다.", "");
        }
    }

    @PostMapping("/token/refresh")
    public RsData<String> refresh(@RequestHeader("Authorization") final String refreshToken) {
        try {
            log.debug("Received refresh token: {}", refreshToken);
            
            // Bearer 토큰에서 실제 토큰 값만 추출
            String token = refreshToken.replace("Bearer ", "");
            
            // Refresh Token을 사용하여 새로운 Access Token 발급
            Optional<RefreshToken> refreshTokenObj = tokenRepository.findByRefreshToken(token);
            
            log.debug("Found refresh token in repository: {}", refreshTokenObj.isPresent());

            if (refreshTokenObj.isPresent() && ut.verifyToken(token)) {
                RefreshToken resultToken = refreshTokenObj.get();
                String email = resultToken.getId();  // 이메일은 id에서 가져옴
                String newAccessToken = ut.generateAccessToken(email, ut.getRole(token));
                resultToken.updateAccessToken(newAccessToken);
                tokenRepository.save(resultToken);
                return new RsData<>("200-1", "토큰이 갱신되었습니다.", newAccessToken);
            }

            return new RsData<>("400-1", "유효하지 않은 리프레시 토큰입니다.", "");
        } catch (Exception e) {
            log.error("토큰 갱신 중 오류 발생: {}", e.getMessage(), e);
            return new RsData<>("500-1", "서버 오류가 발생했습니다.", "");
        }
    }

    @PostMapping("/login")
    public RsData<GeneratedToken> login(@RequestParam String email, @RequestParam String password) {
        log.debug("Login attempt for email: {}", email);
        
        // 먼저 이메일로 회원 존재 여부 확인
        boolean memberExists = memberService.existsByMemberEmail(email);
        log.debug("Member exists check result: {}", memberExists);
        
        if (!memberExists) {
            log.debug("Member not found with email: {}", email);
            return new RsData<>("400-1", "존재하지 않는 이메일입니다.", new GeneratedToken("", ""));
        }
        
        // 사용자 인증 로직
        boolean authenticated = memberService.authenticate(email, password);
        log.debug("Authentication result for email {}: {}", email, authenticated);
        
        if (authenticated) {
            String accessToken = ut.generateAccessToken(email, "ROLE_USER");
            String refreshToken = ut.generateRefreshToken(email, "ROLE_USER");

            // Redis에 토큰 저장
            tokenService.saveTokenInfo(email, refreshToken, accessToken);
            log.debug("Saved refresh token for email: {}", email);

            GeneratedToken tokens = new GeneratedToken(accessToken, refreshToken);
            return new RsData<>("200-1", "로그인 성공", tokens);
        }

        log.debug("Password authentication failed for email: {}", email);
        return new RsData<>("400-1", "잘못된 비밀번호입니다.", new GeneratedToken("", ""));
    }

    @PostMapping("/join")
    public RsData<MemberDTO> join(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String phoneNumber,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate
    ) {
        log.debug("Join attempt for email: {}", email);
        
        // 이메일 중복 체크
        if (memberService.existsByMemberEmail(email)) {
            log.debug("Email already exists: {}", email);
            return new RsData<>("400-1", "이미 존재하는 이메일입니다.", null);
        }
        
        try {
            MemberDTO memberDTO = new MemberDTO(
                    null,
                    email,
                    name,
                    phoneNumber,
                    birthDate,
                    null,
                    null,
                    null,
                    null
            );
            
            Member member = memberService.join(memberDTO, password);
            log.debug("Member joined successfully with email: {}", email);
            
            MemberDTO responseDTO = new MemberDTO(
                    member.getMemberId(),
                    member.getMemberEmail(),
                    member.getMemberName(),
                    member.getMemberPhoneNumber(),
                    member.getBirthDate(),
                    member.getCreatedAt(),
                    member.getModifiedAt(),
                    member.getRole(),
                    member.getMemberStatus()
            );
            
            return new RsData<>("200-1", "회원가입이 완료되었습니다.", responseDTO);
        } catch (Exception e) {
            log.error("Error during join process: ", e);
            return new RsData<>("500-1", "회원가입 처리 중 오류가 발생했습니다.", null);
        }
    }
}