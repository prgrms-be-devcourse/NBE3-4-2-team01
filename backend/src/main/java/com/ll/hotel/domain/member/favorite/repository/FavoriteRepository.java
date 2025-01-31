package com.ll.hotel.domain.member.favorite.repository;

import com.ll.hotel.domain.member.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByCustomerId(String customerId);
    Optional<Favorite> findByCustomerIdAndHotelId(String customerId, String hotelId);
    void deleteByCustomerIdAndHotelId(String customerId, String hotelId);
    void deleteByCustomerId(String customerId);
}
