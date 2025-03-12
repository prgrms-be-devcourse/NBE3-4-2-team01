package com.ll.hotel.domain.hotel.room.entity;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.option.entity.RoomOption;
import com.ll.hotel.domain.hotel.room.dto.PostRoomRequest;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import com.ll.hotel.domain.hotel.room.type.RoomStatus;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_hotel_roomName", columnNames = {"hotel_id", "room_name"})
        }
)
public class Room extends BaseTime {
    @Column(nullable = false)
    private String roomName;

    @Column(nullable = false)
    private Integer roomNumber;

    @Column(nullable = false)
    private Integer basePrice;

    @Column(nullable = false)
    private Integer standardNumber;

    @Column(nullable = false)
    private Integer maxNumber;

    @Embedded
    private BedTypeNumber bedTypeNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoomStatus roomStatus = RoomStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @ManyToMany
    Set<RoomOption> roomOptions;

    public static Room from(Hotel hotel, PostRoomRequest request, BedTypeNumber bedTypeNumber,
                            Set<RoomOption> roomOptions) {
        return Room.builder()
                .hotel(hotel)
                .roomName(request.roomName())
                .roomNumber(request.roomNumber())
                .basePrice(request.basePrice())
                .standardNumber(request.standardNumber())
                .maxNumber(request.maxNumber())
                .bedTypeNumber(bedTypeNumber)
                .roomOptions(roomOptions)
                .build();
    }
}
