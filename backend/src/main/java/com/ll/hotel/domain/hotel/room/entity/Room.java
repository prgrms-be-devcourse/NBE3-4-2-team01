package com.ll.hotel.domain.hotel.room.entity;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import com.ll.hotel.domain.hotel.room.type.RoomStatus;
import com.ll.hotel.domain.image.entity.Image;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseTime {
    @Column
    private String roomName;

    @Column
    private Integer roomNumber;

    @Column
    private Integer basePrice;

    @Column
    private Integer standardNumber;

    @Column
    private Integer maxNumber;

    @Embedded
    private BedTypeNumber bedTypeNumber;

    @Column
    @Builder.Default
    private RoomStatus roomStatus = RoomStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "referenceId")
    @Builder.Default
    private List<Image> roomImages = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    Set<RoomOption> roomOptions;
}
