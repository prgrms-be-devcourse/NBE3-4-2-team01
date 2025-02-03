package com.ll.hotel.domain.hotel.option.roomOption.entity;

import com.ll.hotel.domain.hotel.room.entity.Room;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "room_option")
public class RoomOption extends BaseEntity {
    @NotBlank(message = "필수 항목입니다.")
    @Size(max = 255, message = "최대 255자까지 작성 가능합니다.")
    @EqualsAndHashCode.Include
    @Column(name = "room_option_name", unique = true, nullable = false, length = 255)
    private String name;

    @ManyToMany
    @Builder.Default
    private Set<Room> rooms = new HashSet<>();
}
