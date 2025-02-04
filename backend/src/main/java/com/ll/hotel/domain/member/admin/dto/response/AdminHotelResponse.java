package com.ll.hotel.domain.member.admin.dto.response;

import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.member.member.entity.Member;

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
            Long id,
            String name,
            String streetAddress,
            Integer zipCode,
            Integer grade,
            HotelStatus status,
            Double averageRating,
            Long totalReviewRatingSum,
            Long totalReviewCount,
            String ownerName,
            String ownerEmail,
            String ownerPhoneNumber
    ) {
        public static Summary from(Hotel hotel) {
            Member owner = hotel.getBusiness().getMember();
            return new Summary(
                    hotel.getId(),
                    hotel.getHotelName(),
                    hotel.getStreetAddress(),
                    hotel.getZipCode(),
                    hotel.getHotelGrade(),
                    hotel.getHotelStatus(),
                    hotel.getAverageRating(),
                    hotel.getTotalReviewRatingSum(),
                    hotel.getTotalReviewCount(),
                    owner.getMemberName(),
                    owner.getMemberEmail(),
                    owner.getMemberPhoneNumber()
            );
        }
    }

    public record Detail(
            HotelDto hotelDto,

            Long ownerId,
            String ownerName,
            String ownerEmail,
            String ownerPhoneNumber,

            Double averageRating,
            Long totalReviewRatingSum,
            Long totalReviewCount
    ) {
        public static Detail from(Hotel hotel) {
            Member owner = hotel.getBusiness().getMember();
            return new Detail(
                    new HotelDto(hotel),
                    owner.getId(),
                    owner.getMemberName(),
                    owner.getMemberEmail(),
                    owner.getMemberPhoneNumber(),
                    hotel.getAverageRating(),
                    hotel.getTotalReviewRatingSum(),
                    hotel.getTotalReviewCount()
            );
        }
    }
}
