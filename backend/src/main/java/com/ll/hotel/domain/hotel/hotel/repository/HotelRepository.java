package com.ll.hotel.domain.hotel.hotel.repository;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
