package com.ll.hotel.domain.hotel.option.service;

import com.ll.hotel.domain.hotel.option.dto.request.OptionRequest;
import com.ll.hotel.domain.hotel.option.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.repository.HotelOptionRepository;
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
    public HotelOption add(OptionRequest optionRequest) {
        HotelOption hotelOption = HotelOption
                .builder()
                .name(optionRequest.name())
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
    public void modify(HotelOption hotelOption, OptionRequest optionRequest) {
        hotelOption.setName(optionRequest.name());
    }

    public void flush() {
        hotelOptionRepository.flush();
    }

    public void delete(HotelOption hotelOption) {

        if (!hotelOption.getHotels().isEmpty()) {
            throw new ServiceException("400", "이미 사용 중인 호텔 옵션은 삭제할 수 없습니다.");
        }

        hotelOptionRepository.delete(hotelOption);
    }
}
