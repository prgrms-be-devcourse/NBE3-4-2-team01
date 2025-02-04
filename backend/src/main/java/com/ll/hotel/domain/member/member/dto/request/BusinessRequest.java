package com.ll.hotel.domain.member.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BusinessRequest(
        @NotBlank(message = "사업자 등록 번호는 필수 항목입니다.")
        @Pattern(regexp = "^[0-9]{10}$", message = "사업자 등록 번호는 10자리 숫자여야 합니다.")
        String businessRegistrationNumber
) {}
