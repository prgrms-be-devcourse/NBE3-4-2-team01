package com.ll.hotel.domain.member.member.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDTO {
    private String memberEmail;
    private String memberName;
    private String memberPhoneNumber;
    private LocalDate birthDate;
    private String role;
    private String userStatus;
} 