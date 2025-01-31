package com.ll.hotel.domain.hotel.option.hotelOption.entity;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel_option")
public class HotelOption extends BaseEntity {
    @NotBlank(message = "필수 항목입니다.")
    @Size(max = 255, message = "최대 255자까지 작성 가능합니다.")
    @Column(name = "hotel_option_name", unique = true, nullable = false, length = 255)
    private String name;

    @ManyToMany
    @Builder.Default
    private Set<Hotel> hotels = new HashSet<>();
}
