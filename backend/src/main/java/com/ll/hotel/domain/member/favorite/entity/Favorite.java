package com.ll.hotel.domain.member.favorite.entity;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "favorite")
public class Favorite extends BaseEntity {

    @Column(nullable = false, unique = true)
    private long hotelId;

    @Column(nullable = false, unique = true)
    private long memberId;

    @ManyToMany
    private List<Member> memberList;

    @ManyToMany
    private List<Hotel> hotelList;
} 