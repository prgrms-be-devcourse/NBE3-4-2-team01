package com.ll.hotel.domain.hotel.amenity.hotelAmenity.entity;

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
        name = "hotel_amenity_mapping",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_hotel_amenity_mapping", columnNames = {"hotel_id", "hotel_amenity_id"})
        }
)
public class HotelAmenityMapping extends BaseEntity {
    @NotNull(message = "호텔 정보는 필수입니다.")
    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @NotNull(message = "편의시설 정보는 필수입니다.")
    @Column(name = "hotel_amenity_id", nullable = false)
    private Long hotelAmenityId;
}
