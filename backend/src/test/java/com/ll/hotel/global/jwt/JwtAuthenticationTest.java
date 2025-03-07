package com.ll.hotel.global.jwt;

import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.service.AuthTokenService;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.jwt.dto.GeneratedToken;
import com.ll.hotel.global.jwt.dto.JwtProperties;
import com.ll.hotel.global.request.Rq;
import com.ll.hotel.global.response.RsData;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static com.ll.hotel.global.exceptions.ErrorCode.UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class JwtAuthenticationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtProperties jwtProperties;
    
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    
    @Mock
    private Rq rq;
    
    private Member testMember;
    private GeneratedToken generatedTokens;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // MockMvc 설정
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(jwtAuthFilter)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        // 테스트 멤버 생성
        testMember = Member.builder()
                .memberEmail("test@example.com")
                .memberName("테스트사용자")
                .memberPhoneNumber("010-1234-5678")
                .role(Role.USER)
                .memberStatus(MemberStatus.ACTIVE)
                .build();
        
        memberRepository.save(testMember);
        
        // 토큰 생성
        generatedTokens = authTokenService.generateToken(
                testMember.getMemberEmail(), 
                testMember.getRole().toString()
        );
        
        ReflectionTestUtils.setField(memberService, "rq", rq);
        
        when(rq.getActor()).thenReturn(testMember);
        
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("쿠키에 액세스 토큰을 담아 API 호출")
    void accessAPI_WithCookieToken() throws Exception {
        // given
        Cookie accessTokenCookie = new Cookie("access_token", generatedTokens.accessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        
        // when & then
        mockMvc.perform(get("/api/favorites/me")
               .cookie(accessTokenCookie))
               .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("토큰 없이 API 호출 시 401 에러")
    void noToken_Unauthorized() throws Exception {
        // 인증 실패 케이스 테스트
        when(rq.getActor()).thenThrow(new ServiceException(UNAUTHORIZED.getHttpStatus(), "로그인이 필요합니다."));
        
        // when & then
        mockMvc.perform(get("/api/favorites/me"))
               .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 갱신 테스트")
    void refreshToken_Success() {
        // given
        String refreshToken = generatedTokens.refreshToken();
        
        // when
        RsData<String> result = memberService.refreshAccessToken(refreshToken);
        
        // then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getResultCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getData()).isNotEmpty();
        
        // 새 토큰으로 이메일 추출 테스트
        String email = memberService.getEmailFromToken(result.getData());
        assertThat(email).isEqualTo(testMember.getMemberEmail());
    }
    
    @Test
    @DisplayName("만료된 토큰 검증 테스트")
    void expiredToken_FailsValidation() {
        // given
        String expiredToken = generateExpiredToken();
        
        // when & then
        boolean isValid = memberService.verifyToken(expiredToken);
        assertThat(isValid).isFalse();
    }
    
    @Test
    @DisplayName("JWT 토큰 검증 성공 테스트")
    void validateToken_Success() {
        // given
        String token = generatedTokens.accessToken();
        
        // when
        boolean isValid = memberService.verifyToken(token);
        
        // then
        assertThat(isValid).isTrue();
    }
    
    @Test
    @DisplayName("토큰에서 이메일 추출 테스트")
    void extractEmailFromToken() {
        // given
        String token = generatedTokens.accessToken();
        
        // when
        String email = memberService.getEmailFromToken(token);
        
        // then
        assertThat(email).isEqualTo(testMember.getMemberEmail());
    }

    // 만료된 토큰 생성 도우미 메서드
    private String generateExpiredToken() {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis - 1000; // 1초 전에 만료됨
        
        return Jwts.builder()
                .setSubject(testMember.getMemberEmail())
                .claim("role", testMember.getRole())
                .claim("type", "access")
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()))
                .compact();
    }
} 