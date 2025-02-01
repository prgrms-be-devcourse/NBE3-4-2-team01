package com.ll.hotel.domain.hotel.room.service;

import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.room.dto.PostRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PostRoomResponse;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import com.ll.hotel.global.exceptions.ServiceException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public PostRoomResponse create(long hotelId, PostRoomRequest postRoomRequest) {
        Hotel hotel = this.hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔의 정보가 존재하지 않습니다."));

        BedTypeNumber bedTypeNumber = BedTypeNumber.fromJson(postRoomRequest.bedTypeNumber());

        Room room = Room.builder()
                .hotel(hotel)
                .roomName(postRoomRequest.roomName())
                .roomNumber(postRoomRequest.roomNumber())
                .basePrice(postRoomRequest.basePrice())
                .standardNumber(postRoomRequest.standardNumber())
                .maxNumber(postRoomRequest.maxNumber())
                .bedTypeNumber(bedTypeNumber)
                // 객실 옵션 추가
                .build();

        return new PostRoomResponse(this.roomRepository.save(room));
    }
}
