package com.ll.hotel.domain.hotel.amenity.hotelAmenity.service;

import com.ll.hotel.domain.hotel.amenity.hotelAmenity.dto.request.HotelAmenityRequest;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.entity.HotelAmenity;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.repository.HotelAmenityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HotelAmenityService {
    private final HotelAmenityRepository hotelAmenityRepository;

    @Transactional
    public HotelAmenity add(HotelAmenityRequest.Details details) {
        HotelAmenity hotelAmenity = HotelAmenity
                .builder()
                .description(details.description())
                .build();

        return hotelAmenityRepository.save(hotelAmenity);
    }
}
