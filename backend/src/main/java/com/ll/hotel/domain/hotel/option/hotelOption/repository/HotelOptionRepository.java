package com.ll.hotel.domain.hotel.option.hotelOption.repository;

import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelOptionRepository extends JpaRepository<HotelOption, Long> {
}
