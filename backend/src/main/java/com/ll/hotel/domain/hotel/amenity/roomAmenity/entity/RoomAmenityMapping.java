package com.ll.hotel.domain.hotel.amenity.roomAmenity.entity;

import com.ll.hotel.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "room_amenity_mapping",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_room_amenity_mapping", columnNames = {"room_id", "room_amenity_id"})
        }
)
public class RoomAmenityMapping extends BaseEntity {
    @NotNull(message = "객실 정보는 필수입니다.")
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @NotNull(message = "편의시설 정보는 필수입니다.")
    @Column(name = "room_amenity_id", nullable = false)
    private Long roomAmenityId;
}
