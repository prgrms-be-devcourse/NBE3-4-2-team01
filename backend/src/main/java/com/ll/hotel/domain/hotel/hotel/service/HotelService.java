package com.ll.hotel.domain.hotel.hotel.service;

import com.ll.hotel.domain.hotel.hotel.dto.GetAllHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.image.ImageType;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final BusinessRepository businessRepository;

    /**
     * HotelImage, hotelOption 등록 추가 필요
     */
    @Transactional
    public PostHotelResponse create(PostHotelRequest postHotelRequest) {
        Business business = this.businessRepository.findById(postHotelRequest.businessId())
                .orElseThrow(() -> new ServiceException("404-1", "사업자가 존재하지 않습니다."));

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
                .business(business)
                // 호텔 이미지 등록 추가 필요
                // 호텔 옵션 등록 추가 필요
                .build();

        return new PostHotelResponse(this.hotelRepository.save(hotel));
    }

    @Transactional
    public List<GetAllHotelResponse> findAll() {
        return this.hotelRepository.findAllHotels(ImageType.HOTEL);
    }

    @Transactional
    public HotelDto findHotelDetail(long hotelId) {
        Hotel hotel = this.hotelRepository.findHotelDetail(hotelId, ImageType.HOTEL)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보를 찾을 수 없습니다."));

        return new HotelDto(hotel);
    }
}
