package com.ll.hotel.domain.hotel.room.repository;

import com.ll.hotel.domain.hotel.room.dto.RoomWithImageDto;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.image.type.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("""
            SELECT new com.ll.hotel.domain.hotel.room.dto.RoomWithImageDto(r, i)
            FROM Room r
            LEFT JOIN Image i
            ON i.referenceId = r.id
            AND i.imageType = :imageType
            WHERE r.hotel.id = :hotelId
            AND r.roomStatus <> 'UNAVAILABLE'
<<<<<<< HEAD
            AND i.createdAt = (
                SELECT MIN(i2.createdAt)
=======
            AND (
            i.uploadedAt = (
                SELECT MIN(i2.uploadedAt)
>>>>>>> d18307c ([feat] 호텔 등록,FEAT] [FE] [BE] 특정 호텔 조회, 객실 등록, 객실 수정, 엔티티 수정 및 추가)
                FROM Image i2
                WHERE i2.referenceId = r.id
                AND i2.imageType = :imageType
            )
            OR i is NULL
            )
            """)
    List<RoomWithImageDto> findAllRooms(@Param("hotelId") long hotelId, @Param("imageType") ImageType imageType);

    @Query("""
            SELECT r
            FROM Room r
            WHERE r.hotel.id = :hotelId
            AND r.id = :roomId
            AND r.roomStatus <> 'UNAVAILABLE'
            """)
    Optional<Room> findRoomDetail(@Param("hotelId") long hotelId, @Param("roomId") long roomId);

    boolean existsByHotelIdAndRoomNameAndIdNot(long hotelId, String roomName, long roomId);
}
