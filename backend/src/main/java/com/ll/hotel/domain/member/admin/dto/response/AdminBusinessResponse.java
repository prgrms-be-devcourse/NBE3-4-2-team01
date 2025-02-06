package com.ll.hotel.domain.member.admin.dto.response;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;

import java.time.LocalDate;

public class AdminBusinessResponse {
    public record ApprovalResult(
            Long businessId,
            String businessRegistrationNumber,
            LocalDate startDate,
            String ownerName,
            BusinessApprovalStatus approvalStatus
    ) {
        public static ApprovalResult from(Business business) {
            return new ApprovalResult(
                    business.getId(),
                    business.getBusinessRegistrationNumber(),
                    business.getStartDate(),
                    business.getOwnerName(),
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
            MemberStatus memberStatus,

            Long hotelId,
            String hotelName,
            String streetAddress,
            HotelStatus hotelStatus
    ) {
        public static Detail from(Business business) {
            Member owner = business.getMember();
            Hotel hotel = business.getHotel();
            return new Detail(
                    business.getId(),
                    business.getBusinessRegistrationNumber(),
                    business.getApprovalStatus(),
                    owner.getId(),
                    owner.getMemberName(),
                    owner.getMemberEmail(),
                    owner.getMemberPhoneNumber(),
                    owner.getBirthDate(),
                    owner.getMemberStatus(),
                    (hotel != null) ? hotel.getId() : null,
                    (hotel != null) ? hotel.getHotelName() : null,
                    (hotel != null) ? hotel.getStreetAddress() : null,
                    (hotel != null) ? hotel.getHotelStatus() : null
            );
        }
    }
}
