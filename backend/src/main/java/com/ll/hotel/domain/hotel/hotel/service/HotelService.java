package com.ll.hotel.domain.hotel.hotel.service;

import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final BusinessRepository businessRepository;

    /**
     * HotelImage, hotelOption 등록 추가 필요
     */
    public PostHotelResponse create(PostHotelRequest postHotelRequest) {
        Optional<Business> business = this.businessRepository.findById(postHotelRequest.businessId());

        if (business.isEmpty()) {
            throw new ServiceException("404-1", "사업자가 존재하지 않습니다.");
        }

        Hotel hotel = Hotel.builder()
                .hotelName(postHotelRequest.hotelName())
                .hotelEmail(postHotelRequest.hotelEmail())
                .hotelPhoneNumber(postHotelRequest.hotelPhoneNumber())
                .streetAddress(postHotelRequest.streetAddress())
                .zipCode(postHotelRequest.zipCode())
                .hotelGrade(postHotelRequest.hotelGrade())
                .checkInTime(postHotelRequest.checkInTime())
                .checkOutTime(postHotelRequest.checkOutTime())
                .hotelExplainContent(postHotelRequest.hotelExplainContent())
                .business(business.get())
                // 호텔 이미지 등록 추가 필요
                // 호텔 옵션 등록 추가 필요
                .build();

        return new PostHotelResponse(this.hotelRepository.save(hotel));
    }
}
