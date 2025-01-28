package com.ll.hotel.domain.hotel.amenity.roomAmenity.service;

import com.ll.hotel.domain.hotel.amenity.roomAmenity.dto.request.RoomAmenityRequest;
import com.ll.hotel.domain.hotel.amenity.roomAmenity.entity.RoomAmenity;
import com.ll.hotel.domain.hotel.amenity.roomAmenity.repository.RoomAmenityRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomAmenityService {
    private final RoomAmenityRepository roomAmenityRepository;

    @Transactional
    public RoomAmenity add(RoomAmenityRequest.Details details) {
        RoomAmenity roomAmenity = RoomAmenity
                .builder()
                .description(details.description())
                .build();

        return roomAmenityRepository.save(roomAmenity);
    }
}