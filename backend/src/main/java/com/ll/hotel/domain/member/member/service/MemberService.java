package com.ll.hotel.domain.member.member.service;

import com.ll.hotel.domain.member.member.dto.MemberDTO;
import com.ll.hotel.domain.member.member.entity.MemberEntity;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.entity.UserStatus;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberEntity join(MemberDTO memberDTO, String Password) {
        // TODO: OAuth 구현 시 추가 정보 회원 폼으로 변경 예정 및 중복 회원 가입 방지 로직 추가

        String encodedPassword = passwordEncoder.encode(Password);

        MemberEntity createdMember = MemberEntity.builder()
                .memberEmail(memberDTO.getMemberEmail())
                .password(encodedPassword)
                .memberName(memberDTO.getMemberName())
                .memberPhoneNumber(memberDTO.getMemberPhoneNumber())
                .birthDate(memberDTO.getBirthDate())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .role(Role.USER)
                .userStatus(UserStatus.ACTIVE)
                .build();

        return memberRepository.save(createdMember);
    }

    public MemberDTO login(String email, String password) {

        return null;
    }

    public void logout() {

    }

    public MemberDTO getMemberById(Long id) {
  
        return null;
    }

}
