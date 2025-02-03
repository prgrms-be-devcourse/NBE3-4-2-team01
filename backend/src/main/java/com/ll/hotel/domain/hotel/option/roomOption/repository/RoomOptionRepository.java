package com.ll.hotel.domain.hotel.option.roomOption.repository;

import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomOptionRepository extends JpaRepository<RoomOption, Long> {
    List<RoomOption> findByNameIn(Collection<String> names);

    Optional<RoomOption> findByName(String name);

    boolean existsByName(String name);
}
