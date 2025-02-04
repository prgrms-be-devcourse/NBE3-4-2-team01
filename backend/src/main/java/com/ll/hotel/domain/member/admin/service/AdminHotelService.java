package com.ll.hotel.domain.member.admin.service;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.member.admin.dto.request.AdminHotelRequest;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminHotelService {
    private final HotelRepository hotelRepository;

    public Page<Hotel> findAllPaged(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return hotelRepository.findAll(pageable);
    }

    public Hotel findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "존재하지 않는 호텔입니다."));
    }

    @Transactional
    public void approve(Hotel hotel, AdminHotelRequest adminhotelRequest) {
        hotel.setHotelStatus(adminhotelRequest.hotelStatus());
    }

    public void flush() {
        hotelRepository.flush();
    }
}
