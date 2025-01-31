package com.ll.hotel.domain.hotel.option.roomOption.service;

import com.ll.hotel.domain.hotel.option.roomOption.dto.request.RoomOptionRequest;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.roomOption.repository.RoomOptionRepository;
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
    public RoomOption add(RoomOptionRequest.Details details) {
        RoomOption roomOption = RoomOption
                .builder()
                .name(details.name())
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
    public void modify(RoomOption roomOption, RoomOptionRequest.Details details) {
        roomOption.setName(details.name());
    }

    public void flush() {
        roomOptionRepository.flush();
    }

    public void delete(RoomOption roomOption) {
        roomOptionRepository.delete(roomOption);
    }
}
