package com.ll.hotel.domain.hotel.amenity.hotelAmenity.repository;

import com.ll.hotel.domain.hotel.amenity.hotelAmenity.entity.HotelAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelAmenityRepository extends JpaRepository<HotelAmenity, Long> {
}
