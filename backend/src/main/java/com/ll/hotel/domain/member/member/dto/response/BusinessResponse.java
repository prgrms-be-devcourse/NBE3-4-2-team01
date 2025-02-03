package com.ll.hotel.domain.member.member.dto.response;

import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;

public record BusinessResponse(
        Long businessId,
        String businessRegistrationNumber,
        BusinessApprovalStatus approvalStatus
) {
    public static BusinessResponse of(Business business) {
        return new BusinessResponse(
                business.getId(),
                business.getBusinessRegistrationNumber(),
                business.getApprovalStatus()
        );
    }
}
