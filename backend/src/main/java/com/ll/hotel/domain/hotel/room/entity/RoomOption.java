package com.ll.hotel.domain.hotel.room.entity;

import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomOption extends BaseTime {
    @Column
    private String roomOptionName;

    @ManyToMany
    Set<Room> room;
}
