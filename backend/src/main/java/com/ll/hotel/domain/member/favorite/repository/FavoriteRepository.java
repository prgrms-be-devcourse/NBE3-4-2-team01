package com.ll.hotel.domain.member.favorite.repository;

import com.ll.hotel.domain.member.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByHotelIdAndMemberId(Long hotelId, Long memberId);
    List<Favorite> findByMemberId(Long memberId);
}