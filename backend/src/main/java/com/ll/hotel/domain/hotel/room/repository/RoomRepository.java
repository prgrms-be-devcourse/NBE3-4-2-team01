package com.ll.hotel.domain.hotel.room.repository;

import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.image.type.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("""
            SELECT r, i
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
            OR i is NULL
            """)
    List<Room> findAllRooms(@Param("hotelId") long hotelId, @Param("imageType") ImageType imageType);

    @Query("""
            SELECT r
            FROM Room r
            LEFT JOIN FETCH r.roomImages i
            WHERE r.hotel.id = :hotelId
            AND r.id = :roomId
            AND (i IS NULL
            OR (i.referenceId = :roomId
            AND i.imageType = :imageType))
            """)
    Optional<Room> findRoomDetail(@Param("hotelId") long hotelId, @Param("roomId") long roomId,
                                  @Param("imageType") ImageType imageType);

    @Query("""
            SELECT r
            FROM Room r
            LEFT JOIN FETCH r.roomOptions
            WHERE r.id = :roomId
            """)
    Optional<Room> findRoomOptionsById(@Param("roomId") long roomId);
}
