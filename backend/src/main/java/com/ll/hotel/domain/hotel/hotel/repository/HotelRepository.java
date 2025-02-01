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
    List<GetAllHotelResponse> findAllHotels(@Param("imageType") ImageType imageType);

    @Query("""
            SELECT h
            FROM Hotel h
            LEFT JOIN FETCH h.hotelImages i
            LEFT JOIN FETCH h.rooms r
            LEFT JOIN FETCH r.roomImages ri
            LEFT JOIN FETCH h.reviews rv
            LEFT JOIN FETCH h.hotelOptions ho
            WHERE h.id = :hotelId
            AND i.referenceId = :hotelId
            AND i.imageType = :imageType
            """)
    Optional<Hotel> findHotelDetail(@Param("hotelId") long hotelId, @Param("imageType") ImageType imageType);

//    @Query("""
//            SELECT
//            h.id, h.hotelName, h.hotelEmail, h.hotelPhoneNumber, h.streetAddress, h.zipCode, h.hotelGrade,
//            h.checkInTime, h.checkOutTime, h.hotelExplainContent, h.hotelStatus, hi, r, ho
//            FROM Hotel h
//            LEFT JOIN h.hotelImages hi
//            ON hi.referenceId = h.id
//            AND hi.imageType = :hotelImageType
//            LEFT JOIN h.rooms r
//            LEFT JOIN r.roomImages ri
//            ON ri.referenceId = r.id
//            AND ri.imageType = :imageType
//            LEFT JOIN h.hotelOptions ho
//            WHERE h.id = :hotelId
//            AND ri.uploadedAt = (
//            SELECT MIN(i.uploadedAt)
//            FROM Image i
//            WHERE i.referenceId = r.id
//            AND i.imageType = :imageType
//            )
//            """)
//    List<HotelDto> findHotelById(@Param("hotelId") long hotelId, @Param("hotelImageType") ImageType hotelImageType,
//                           @Param("imageType") ImageType imageType);
}
