package com.ll.hotel.domain.hotel.option.hotelOption.service;

import com.ll.hotel.domain.hotel.option.hotelOption.dto.request.HotelOptionRequest;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.repository.HotelOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HotelOptionService {
    private final HotelOptionRepository hotelOptionRepository;

    @Transactional
    public HotelOption add(HotelOptionRequest.Details details) {
        HotelOption hotelOption = HotelOption
                .builder()
                .name(details.name())
                .build();

        return hotelOptionRepository.save(hotelOption);
    }
}
