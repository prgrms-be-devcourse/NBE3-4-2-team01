package com.ll.hotel.domain.member.member.dto.response;

import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;

import java.util.List;
import java.util.Map;

public class BusinessResponse {
    public record ApprovalResult(
            Long businessId,
            String businessRegistrationNumber,
            BusinessApprovalStatus approvalStatus,
            String apprvalCode
    ) {
        public static ApprovalResult of(Business business, String approvalCode) {
            return new ApprovalResult(
                    business.getId(),
                    business.getBusinessRegistrationNumber(),
                    business.getApprovalStatus(),
                    approvalCode
            );
        }
    }

    public record Verification(
            List<Map<String, Object>> data
    ) {}
}
