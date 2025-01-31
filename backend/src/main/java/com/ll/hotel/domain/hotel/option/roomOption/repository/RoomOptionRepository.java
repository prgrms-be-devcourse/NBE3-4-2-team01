package com.ll.hotel.domain.hotel.option.roomOption.repository;

import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomOptionRepository extends JpaRepository<RoomOption, Long> {
}
