package com.ll.hotel.domain.member.admin.dto;

import com.ll.hotel.domain.member.member.dto.MemberDto;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;

public record AdminBusinessDto(
        String businessRegistrationNumber,
        BusinessApprovalStatus approvalStatus,
        MemberDto memberDto
) {
    public static AdminBusinessDto toDto(Business business) {
        return new AdminBusinessDto(
                business.getBusinessRegistrationNumber(),
                business.getApprovalStatus(),
                MemberDto.toDto(business.getMember()) // 수정 필요
        );
    }
}
