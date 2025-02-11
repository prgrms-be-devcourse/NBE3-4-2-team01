package com.ll.hotel.domain.member.member.service;


import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.member.member.dto.JoinRequest;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.jwt.dto.JwtProperties;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.global.security.oauth2.entity.OAuth;
import com.ll.hotel.global.security.oauth2.repository.OAuthRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final OAuthRepository oAuthRepository;
    private final AuthTokenService authTokenService;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;
    private final HotelRepository hotelRepository;
    private final Rq rq;

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private static final String LOGOUT_PREFIX = "LOGOUT:";

    @Transactional
    public Member join(@Valid JoinRequest joinRequest) {
        log.debug("Join service - OAuth 정보: provider={}, oauthId={}", 
                 joinRequest.provider(), joinRequest.oauthId());
                 
        Optional<Member> existingMember = memberRepository.findByMemberEmail(joinRequest.email());
        if (existingMember.isPresent()) {
            Member member = existingMember.get();
            
            // OAuth 정보가 있는 경우에만 저장
            if (joinRequest.provider() != null && joinRequest.oauthId() != null) {
                OAuth oauth = OAuth.builder()
                        .member(member)
                        .provider(joinRequest.provider())
                        .oauthId(joinRequest.oauthId())
                        .build();
                oAuthRepository.save(oauth);
                member.getOauths().add(oauth);
                return member;
            }
            
            throw new ServiceException("400", "이미 가입된 이메일입니다.");
        }

        Member newMember = Member.builder()
                .memberEmail(joinRequest.email())
                .memberName(joinRequest.name())
                .memberPhoneNumber(joinRequest.phoneNumber())
                .role(joinRequest.role())
                .memberStatus(MemberStatus.ACTIVE)
                .birthDate(joinRequest.birthDate())
                .build();
        
        Member savedMember = memberRepository.save(newMember);
        
        OAuth oauth = OAuth.builder()
                .member(savedMember)
                .provider(joinRequest.provider())
                .oauthId(joinRequest.oauthId())
                .build();
        oAuthRepository.save(oauth);
        
        return savedMember;
    }

    public Optional<Member> findByMemberEmail(String email) {
        return memberRepository.findByMemberEmail(email);
    }

    public boolean existsByMemberEmail(String email) {
        log.debug("Checking if member exists with email: {}", email);
        boolean exists = memberRepository.existsByMemberEmail(email);
        log.debug("Member exists with email {}: {}", email, exists);
        return exists;
    }

    public boolean verifyToken(String accessToken) {
        return authTokenService.verifyToken(accessToken);
    }

    public RsData<String> refreshAccessToken(String refreshToken) {
        return refreshTokenService.refreshAccessToken(refreshToken);
    }

    public String generateRefreshToken(String email) {
        return refreshTokenService.generateRefreshToken(email);
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
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

        if (accessToken == null) {
            throw new ServiceException("400-1", "토큰이 필요합니다.");
        }

        String email = authTokenService.getEmail(accessToken);
        
        // Redis에서 토큰 무효화
        redisTemplate.opsForValue().set(
            LOGOUT_PREFIX + accessToken,
            email,
            jwtProperties.getAccessTokenExpiration(),
            TimeUnit.MILLISECONDS
        );

        // Refresh 토큰 삭제
        refreshTokenService.removeRefreshToken(email);

        deleteCookie(response);
    }

    private static void deleteCookie(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");

        Cookie roleCookie = new Cookie("role", null);
        roleCookie.setMaxAge(0);
        roleCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");

        Cookie oauth2AuthRequestCookie = new Cookie("oauth2_auth_request", null);
        oauth2AuthRequestCookie.setMaxAge(0);
        oauth2AuthRequestCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(roleCookie);
        response.addCookie(refreshTokenCookie);
        response.addCookie(oauth2AuthRequestCookie);
    }

    public boolean isLoggedOut(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(LOGOUT_PREFIX + token));
    }

    public String getEmailFromToken(String token) {
        return authTokenService.getEmail(token);
    }

    public String extractEmailIfValid(String token) {
        if (isLoggedOut(token)) {
            throw new ServiceException("401-1", "로그아웃된 토큰입니다.");
        }
        if (!verifyToken(token)) {
            throw new ServiceException("401-2", "유효하지 않은 토큰입니다.");
        }
        return getEmailFromToken(token);
    }

    @Transactional
    public void addFavorite(Long hotelId) {
        Member actor = rq.getActor();
        if (actor == null) {
            throw new ServiceException("401-1", "로그인이 필요합니다.");
        }

        Hotel hotel = hotelRepository.findById(hotelId)
            .orElseThrow(() -> new ServiceException("404-1", "존재하지 않는 호텔입니다."));

        if (actor.getFavoriteHotels().contains(hotel)) {
            throw new ServiceException("400-1", "이미 즐겨찾기에 추가된 호텔입니다.");
        }

        actor.getFavoriteHotels().add(hotel);
        hotel.getFavorites().add(actor);
        
        memberRepository.save(actor);
        hotelRepository.save(hotel);
    }

    @Transactional
    public void removeFavorite(Long hotelId) {
        Member actor = rq.getActor();
        if (actor == null) {
            throw new ServiceException("401-1", "로그인이 필요합니다.");
        }

        Hotel hotel = hotelRepository.findById(hotelId)
            .orElseThrow(() -> new ServiceException("404-1", "존재하지 않는 호텔입니다."));

        if (!actor.getFavoriteHotels().contains(hotel)) {
            throw new ServiceException("400-1", "즐겨찾기에 없는 호텔입니다.");
        }

        actor.getFavoriteHotels().remove(hotel);
        hotel.getFavorites().remove(actor);
        
        memberRepository.save(actor);
        hotelRepository.save(hotel);
    }

    public List<HotelDto> getFavoriteHotels() {
        Member actor = rq.getActor();
        log.debug("getFavoriteHotels - actor: {}", actor);
        
        if (actor == null) {
            throw new ServiceException("401-1", "로그인이 필요합니다.");
        }

        Set<Hotel> favorites = actor.getFavoriteHotels();
        log.debug("getFavoriteHotels - favorites size: {}", favorites.size());

        return favorites.stream()
            .map(hotel -> {
                log.debug("getFavoriteHotels - hotel: {}", hotel.getHotelName());
                return new HotelDto(hotel);
            })
            .collect(Collectors.toList());
    }

    public boolean isFavoriteHotel(long hotelId) {
        Member actor = rq.getActor();

        if (actor == null) {
            throw new ServiceException("401-1", "로그인이 필요합니다.");
        }

        Set<Hotel> favorites = actor.getFavoriteHotels();

       return favorites.stream()
                .anyMatch(hotel -> hotel.getId() == hotelId);
    }

    @Transactional(readOnly = true)
    public Member findByProviderAndOauthId(String provider, String oauthId) {
        return oAuthRepository.findByProviderAndOauthIdWithMember(provider, oauthId)
            .orElseThrow(() -> new ServiceException("404-1", "해당 OAuth 정보를 찾을 수 없습니다."))
            .getMember();
    }
}