package com.ll.hotel.domain.hotel.hotel.repository;

import com.ll.hotel.domain.hotel.hotel.dto.GetAllHotelResponse;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.image.ImageType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("""
            SELECT h, i
            FROM Hotel h
            LEFT JOIN h.hotelImages i
            ON i.referenceId = h.id
            AND i.imageType = :imageType
            WHERE i.uploadedAt = (
                SELECT MIN(i2.uploadedAt)
                FROM Image i2
                WHERE i2.referenceId = h.id
                AND i2.imageType = :imageType
            )
            OR i is NULL
            """)
    List<Hotel> findAllHotels(@Param("imageType") ImageType imageType);

    @Query("""
            SELECT h
            FROM Hotel h
            LEFT JOIN FETCH h.hotelImages i
            WHERE h.id = :hotelId
            AND (i IS NULL
            OR (i.referenceId = :hotelId AND i.imageType = :imageType))
            """)
    Optional<Hotel> findHotelDetail(@Param("hotelId") long hotelId, @Param("imageType") ImageType imageType);

//    @Query("""
//            SELECT i
//            FROM Image i
//            WHERE i.referenceId = :hotelId
//            AND i.imageType = :imageType
//            """)
//    List<Image> findHotelImages(@Param("hotelId") long hotelId, @Param("imageType") ImageType imageType);
}
