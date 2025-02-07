package com.ll.hotel.domain.member.admin.dto.request;

import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record AdminBusinessRequest(
        @Null
        @Pattern(regexp = "^[0-9]{10}$", message = "사업자 등록 번호는 10자리 숫자여야 합니다.")
        String businessRegistrationNumber,

        @PastOrPresent(message = "개업 일자는 현재 날짜 또는 과거여야 합니다.")
        LocalDate startDate,

        @Size(max = 30, message = "대표자명은 최대 30자까지 가능합니다.")
        String ownerName,

        BusinessApprovalStatus businessApprovalStatus
) {}
