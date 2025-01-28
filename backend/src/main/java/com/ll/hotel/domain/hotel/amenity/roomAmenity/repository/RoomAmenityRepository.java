package com.ll.hotel.domain.hotel.amenity.roomAmenity.repository;

import com.ll.hotel.domain.hotel.amenity.roomAmenity.entity.RoomAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomAmenityRepository extends JpaRepository<RoomAmenity, Long> {
}
