package com.ll.hotel.domain.hotel.option.hotelOption.repository;

import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelOptionRepository extends JpaRepository<HotelOption, Long> {
    List<HotelOption> findByNameIn(Collection<String> names);

    Optional<HotelOption> findByName(String name);

    boolean existsByName(String name);
}
