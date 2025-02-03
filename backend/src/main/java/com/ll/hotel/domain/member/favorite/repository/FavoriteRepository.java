package com.ll.hotel.domain.member.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.hotel.domain.member.favorite.entity.Favorite;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByHotelIdAndMemberId(Long hotelId, Long memberId);
}