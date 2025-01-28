package com.ll.hotel.domain.hotel.amenity.hotelAmenity.service;

import com.ll.hotel.domain.hotel.amenity.hotelAmenity.dto.request.HotelAmenityRequest;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.entity.HotelAmenity;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.repository.HotelAmenityRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<HotelAmenity> findAll() {
        return hotelAmenityRepository.findAll();
    }

    public HotelAmenity findById(Long id) {
        return hotelAmenityRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "유효하지 않은 편의시설입니다."));
    }

    @Transactional
    public void modify(HotelAmenity hotelAmenity, HotelAmenityRequest.Details details) {
        hotelAmenity.setDescription(details.description());
    }

    public void flush() {
        hotelAmenityRepository.flush();
    }
}
