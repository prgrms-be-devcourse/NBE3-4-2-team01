package com.ll.hotel.domain.hotel.hotel.repository;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ll.hotel.domain.hotel.hotel.dto.GetAllHotelResponse;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.image.ImageType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("""
            SELECT new com.ll.hotel.domain.hotel.hotel.dto.GetAllHotelResponse
            (h.id, h.hotelName, h.streetAddress, h.zipCode, h.hotelStatus, i.imageUrl)
            FROM Hotel h
            LEFT JOIN h.hotelImages i
            ON i.referenceId = h.id
            AND i.imageType = :imageType
            WHERE i.uploadedAt =  (
                SELECT MIN(i2.uploadedAt)
                FROM Image i2
                WHERE i2.referenceId = h.id
                AND i2.imageType = :imageType
            )
            """)
    List<GetAllHotelResponse> findAllHotels(@Param("imageType")ImageType imageType);
}
