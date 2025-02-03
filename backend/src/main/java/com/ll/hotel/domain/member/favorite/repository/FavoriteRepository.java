package com.ll.hotel.domain.member.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.hotel.domain.member.favorite.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findByHotelIdAndMemberId(long hotelId, long memberId);
}