package com.ll.hotel.domain.hotel.option.hotelOption.repository;

import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelOptionRepository extends JpaRepository<HotelOption, Long> {
    @Query("""
            SELECT ho
            FROM HotelOption ho
            JOIN ho.hotels h
            WHERE h.id = :hotelId
            AND ho.name IN :names
            """)
    List<HotelOption> findByNames(@Param("hotelId") long hotelId, @Param("names") Collection<String> names);
}
