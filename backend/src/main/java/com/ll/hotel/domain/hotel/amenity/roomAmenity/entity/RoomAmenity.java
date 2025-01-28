package com.ll.hotel.domain.hotel.amenity.roomAmenity.entity;

import com.ll.hotel.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_amenity")
public class RoomAmenity extends BaseEntity {
    @NotBlank(message = "필수 항목입니다.")
    @Size(max = 255, message = "최대 255자까지 작성 가능합니다.")
    @Column(name = "amenity_description", unique = true, nullable = false, length = 255)
    private String description;
}
