package com.ll.hotel.domain.member.member.service;

import com.ll.hotel.domain.member.member.dto.MemberDTO;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    public Member join(MemberDTO memberDTO, String Password) {

        String encodedPassword = passwordEncoder.encode(Password);

        Member createdMember = Member.builder()
                .memberEmail(memberDTO.memberEmail())
                .password(encodedPassword)
                .memberName(memberDTO.memberName())
                .memberPhoneNumber(memberDTO.memberPhoneNumber())
                .birthDate(memberDTO.birthDate())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .role(Role.USER)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        return memberRepository.save(createdMember);
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

    public boolean authenticate(String email, String password) {
        Optional<Member> memberOptional = findByMemberEmail(email);
        
        if (memberOptional.isEmpty()) {
            log.debug("Member not found with email: {}", email);
            return false;
        }
        
        Member member = memberOptional.get();
        boolean isPasswordMatch = passwordEncoder.matches(password, member.getPassword());
        
        log.debug("Password match result for email {}: {}", email, isPasswordMatch);
        
        return isPasswordMatch;
    }

}
