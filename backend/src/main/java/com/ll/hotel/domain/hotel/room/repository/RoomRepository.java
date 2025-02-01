package com.ll.hotel.domain.hotel.room.repository;

import com.ll.hotel.domain.hotel.room.dto.GetAllRoomResponse;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.image.ImageType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("""
            SELECT
            r.id, r.roomName, r.basePrice, r.standardNumber, r.maxNumber, r.bedTypeNumber, i.imageUrl
            FROM Room r
            LEFT JOIN r.roomImages i
            ON i.referenceId = r.id
            AND i.imageType = :imageType
            WHERE r.hotel.id = :hotelId
            AND i.uploadedAt = (
                SELECT MIN(i2.uploadedAt)
                FROM Image i2
                WHERE i2.referenceId = r.id
                AND i2.imageType = :imageType
            )
            """)
    List<GetAllRoomResponse> findAllRooms(@Param("hotelId") long hotelId, @Param("imageType")ImageType imageType);
//    @Query("""
//            SELECT new com.ll.hotel.domain.hotel.room.dto.RoomDetailDto
//            (r.id, r.hotel.id, r.roomName, r.roomNumber, r.basePrice, r.standardNumber, r.maxNumber,
//            r.bedTypeNumber, r.roomStatus, i)
//            FROM Room r
//            LEFT JOIN r.roomImages i
//            ON i.referenceId = :roomId
//            AND i.imageType = :imageType
//            WHERE r.hotel.id = :hotelId
//            AND r.id = :roomId
//            GROUP BY r.id, i.id
//            """)
//    List<RoomDetailDto> findRoomDetail(@Param("hotelId") long hotelId, @Param("roomId") long roomId, @Param("imageType") ImageType imageType);

    @Query("""
            SELECT r
            FROM Room r
            LEFT JOIN FETCH r.roomImages i
            LEFT JOIN FETCH r.roomOptions ri
            WHERE r.hotel.id = :hotelId
            AND r.id = :roomId
            AND i.referenceId = :roomlId
            AND i.imageType = :imageType
            """)
    Optional<Room> findRoomDetail(@Param("hotelId") long hotelId, @Param("roomId") long roomId, @Param("imageType") ImageType imageType);
}
