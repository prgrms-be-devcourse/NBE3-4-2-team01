package com.ll.hotel.domain.hotel.option.service;

import com.ll.hotel.domain.hotel.option.dto.request.OptionRequest;
import com.ll.hotel.domain.hotel.option.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.repository.RoomOptionRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomOptionService {
    private final RoomOptionRepository roomOptionRepository;

    @Transactional
    public RoomOption add(OptionRequest optionRequest) {
        RoomOption roomOption = RoomOption
                .builder()
                .name(optionRequest.name())
                .build();

        return roomOptionRepository.save(roomOption);
    }

    public List<RoomOption> findAll() {
        return roomOptionRepository.findAll();
    }

    public RoomOption findById(Long id) {
        return roomOptionRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "유효하지 않은 편의시설입니다."));
    }

    @Transactional
    public void modify(RoomOption roomOption, OptionRequest optionRequest) {
        roomOption.setName(optionRequest.name());
    }

    public void flush() {
        roomOptionRepository.flush();
    }

    public void delete(RoomOption roomOption) {
        roomOptionRepository.delete(roomOption);
    }
}
