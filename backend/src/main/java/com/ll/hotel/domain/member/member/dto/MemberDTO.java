package com.ll.hotel.domain.member.member.dto;

import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.type.MemberStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberDTO(
        Long id,
        String memberEmail,
        String memberName,
        String memberPhoneNumber,
        LocalDate birthDate,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Role role,
        MemberStatus memberStatus,
        String provider
) {}