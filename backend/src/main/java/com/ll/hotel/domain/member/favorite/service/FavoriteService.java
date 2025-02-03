package com.ll.hotel.domain.member.favorite.service;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.member.favorite.dto.FavoriteReqBody;
import com.ll.hotel.domain.member.favorite.entity.Favorite;
import com.ll.hotel.domain.member.favorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public Favorite addFavorite(FavoriteReqBody request) {
        Favorite favorite = Favorite.builder()
                .memberId(request.memberId())
                .hotelId(request.hotelId())
                .build();
        favoriteRepository.save(favorite);
        return favorite;
    }

    public Favorite deleteFavorite(FavoriteReqBody request) {
        Favorite favorite = favoriteRepository.findByHotelIdAndMemberId(request.hotelId(), request.memberId());
        favoriteRepository.delete(favorite);
        return favorite;
    }

    public List<Hotel> getFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        List<Hotel> hotelList = new ArrayList<>();
        
        for (Favorite favorite : favorites) {
//            Hotel hotel = hotelRepository.findById(favorite.getHotelId()).orElse(null);
//            hotelList.add(hotel);
        }
        
        return hotelList;
    }

}