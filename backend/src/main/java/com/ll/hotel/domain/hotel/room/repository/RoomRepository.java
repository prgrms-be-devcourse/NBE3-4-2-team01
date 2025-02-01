package com.ll.hotel.domain.hotel.room.repository;

import com.ll.hotel.domain.hotel.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
