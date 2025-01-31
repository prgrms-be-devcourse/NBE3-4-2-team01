package com.ll.hotel.domain.hotel.option.roomOption.service;

import com.ll.hotel.domain.hotel.option.roomOption.dto.request.RoomOptionRequest;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.roomOption.repository.RoomOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
