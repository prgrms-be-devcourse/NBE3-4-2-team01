package com.ll.hotel.domain.member.admin.dto.response;

import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;

import java.time.LocalDate;

public class AdminHotelResponse {
    public record ApprovalResult(
            String name,
            HotelStatus status
    ) {
        public static ApprovalResult from(Hotel hotel) {
            return new ApprovalResult(
                    hotel.getHotelName(),
                    hotel.getHotelStatus()
            );
        }
    }

    public record Summary(
            Long hotelId,
            String name,
            String streetAddress,
            String ownerName
    ) {
        public static Summary from(Hotel hotel) {
            Business business = hotel.getBusiness();
            Member owner = business.getMember();
            return new Summary(
                    hotel.getId(),
                    hotel.getHotelName(),
                    hotel.getStreetAddress(),
                    owner.getMemberName()
            );
        }
    }

    public record Detail(
            HotelDto hotelDto,

            Long ownerId,
            String ownerName,
            String buinessRegistrationNumber,
            LocalDate startDate,

            Double averageRating,
            Long totalReviewCount
    ) {
        public static Detail from(Hotel hotel) {
            Business business = hotel.getBusiness();
            return new Detail(
                    new HotelDto(hotel),

                    business.getId(),
                    business.getOwnerName(),
                    business.getBusinessRegistrationNumber(),
                    business.getStartDate(),

                    hotel.getAverageRating(),
                    hotel.getTotalReviewCount()
            );
        }
    }
}
