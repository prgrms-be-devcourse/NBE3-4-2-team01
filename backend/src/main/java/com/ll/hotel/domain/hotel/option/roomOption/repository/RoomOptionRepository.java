package com.ll.hotel.domain.hotel.option.roomOption.repository;

import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomOptionRepository extends JpaRepository<RoomOption, Long> {
    @Query("""
            SELECT ro
            FROM RoomOption ro
            JOIN ro.rooms r
            WHERE r.hotel.id = :hotelId
            AND r.id = :roomId
            AND ro.name IN :names
            """)
    List<RoomOption> findByNames(@Param("hotelId") long hotelId, @Param("roomId") long roomId,
                                 @Param("names") Collection<String> names);
}
