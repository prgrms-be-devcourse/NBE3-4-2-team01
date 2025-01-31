package com.ll.hotel.domain.member.member.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ll.hotel.domain.member.member.dto.MemberDTO;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.RefreshTokenRepository;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.domain.member.member.service.RefreshTokenService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.global.security.dto.RefreshToken;
import com.ll.hotel.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {
    private final MemberService memberService;
    private final RefreshTokenRepository tokenRepository;
    private final RefreshTokenService tokenService;
    private final Ut ut;

    @PostMapping("/logout")
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

    @PostMapping("/refresh")
    public RsData<String> refresh(@RequestHeader("Authorization") final String refreshToken) {
        try {
            log.debug("Received refresh token: {}", refreshToken);
            
            // Bearer 토큰에서 실제 토큰 값만 추출
            String token = refreshToken.replace("Bearer ", "");
            
            // Refresh Token을 사용하여 새로운 Access Token 발급
            boolean tokenExists = tokenRepository.existsByRefreshToken(token);
            
            log.debug("Found refresh token in repository: {}", tokenExists);

            if (tokenExists && ut.verifyToken(token)) {
                RefreshToken resultToken = tokenRepository.findByRefreshToken(token).orElseThrow();
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
    public RsData<Map<String, String>> login(@RequestBody LoginRequest request) {
        if (!memberService.authenticate(request.email(), request.password())) {
            return new RsData<>("400-1", "로그인 실패", null);
        }

        String accessToken = ut.generateAccessToken(request.email(), "ROLE_USER");
        String refreshToken = ut.generateRefreshToken(request.email(), "ROLE_USER");
        
        tokenService.saveTokenInfo(request.email(), refreshToken, accessToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return new RsData<>("200-1", "로그인 성공", tokens);
    }

    @PostMapping("/join")
    public RsData<MemberDTO> join(@RequestBody JoinRequest request) {
        log.debug("Join attempt for email: {}", request.email());
        
        if (memberService.existsByMemberEmail(request.email())) {
            log.debug("Email already exists: {}", request.email());
            return new RsData<>("400-1", "이미 존재하는 이메일입니다.", null);
        }
        
        try {
            MemberDTO memberDTO = new MemberDTO(
                    null,
                    request.email(),
                    request.name(),
                    request.phoneNumber(),
                    request.birthDate(),
                    null,
                    null,
                    null,
                    null
            );
            
            Member member = memberService.join(memberDTO, request.password());
            log.debug("Member joined successfully with email: {}", request.email());
            
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

record LoginRequest(String email, String password) {}

record JoinRequest(
    String email,
    String password,
    String name,
    String phoneNumber,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate
) {}