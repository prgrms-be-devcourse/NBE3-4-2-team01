package com.ll.hotel.domain.member.admin.dto.response;

import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;

public record AdminBusinessResponse(
        String businessRegistrationNumber,
        BusinessApprovalStatus approvalStatus
) {
    public AdminBusinessResponse(Business business) {
        this(business.getBusinessRegistrationNumber(), business.getApprovalStatus());
    }
}
