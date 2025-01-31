package com.ll.hotel.domain.hotel.room.entity;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import com.ll.hotel.domain.hotel.room.type.RoomStatus;
import com.ll.hotel.domain.image.Image;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    /**
     * 수정 필요
     */
    @Column
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

    @ManyToMany
    Set<RoomOption> roomOptions;

//    public void addRoomImage(RoomImage roomImage) {
//        this.roomImages.add(roomImage);
//        roomImage.setRoom(this);
//    }
}
