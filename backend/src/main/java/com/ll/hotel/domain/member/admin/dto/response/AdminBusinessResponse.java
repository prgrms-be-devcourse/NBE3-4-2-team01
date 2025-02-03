package com.ll.hotel.domain.member.admin.dto.response;

import com.ll.hotel.domain.member.member.dto.MemberDto;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class AdminBusinessResponse {
    public record ApprovalResult(
            String businessRegistrationNumber,
            BusinessApprovalStatus approvalStatus
    ) {
        public static ApprovalResult from(Business business) {
            return new ApprovalResult(
                    business.getBusinessRegistrationNumber(),
                    business.getApprovalStatus()
            );
        }
    }
    public record Summary(
            Long id,
            String businessRegistrationNumber,
            BusinessApprovalStatus approvalStatus
    ) {
        public static Summary from(Business business) {
            return new Summary(
                    business.getId(),
                    business.getBusinessRegistrationNumber(),
                    business.getApprovalStatus()
            );
        }
    }

    public record Detail(
            Long businessId,
            String businessRegistrationNumber,
            BusinessApprovalStatus approvalStatus,

            Long userId,
            String name,
            String email,
            String phoneNumber,
            LocalDate birth,
            MemberStatus status
    ) {
        public static Detail from(Business business) {
            Member owner = business.getMember();
            return new Detail(
                    business.getId(),
                    business.getBusinessRegistrationNumber(),
                    business.getApprovalStatus(),
                    owner.getId(),
                    owner.getMemberName(),
                    owner.getMemberEmail(),
                    owner.getMemberPhoneNumber(),
                    owner.getBirthDate(),
                    owner.getMemberStatus()
            );
        }
    }
}
