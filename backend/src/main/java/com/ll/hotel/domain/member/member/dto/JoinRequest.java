package com.ll.hotel.domain.member.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record JoinRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String email,

    @NotBlank(message = "이름은 필수입니다.")
    String name,

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    String phoneNumber,

    LocalDate birthDate,

    @NotBlank(message = "OAuth 제공자는 필수입니다.")
    String provider,
    
    @NotBlank(message = "OAuth ID는 필수입니다.")
    String oauthId
) {}