package com.ll.hotel.domain.hotel.option.hotelOption.service;

import com.ll.hotel.domain.hotel.option.hotelOption.dto.request.HotelOptionRequest;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.repository.HotelOptionRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<HotelOption> findAll() {
        return hotelOptionRepository.findAll();
    }

    public HotelOption findById(Long id) {
        return hotelOptionRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "유효하지 않은 편의시설입니다."));
    }

    @Transactional
    public void modify(HotelOption hotelOption, HotelOptionRequest.Details details) {
        hotelOption.setName(details.name());
    }

    public void flush() {
        hotelOptionRepository.flush();
    }
}
