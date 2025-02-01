package com.ll.hotel.domain.hotel.room.service;

import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
}
