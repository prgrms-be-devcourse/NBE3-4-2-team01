package com.ll.hotel.domain.member.favorite.service;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.member.favorite.dto.FavoriteReqBody;
import com.ll.hotel.domain.member.favorite.entity.Favorite;
import com.ll.hotel.domain.member.favorite.repository.FavoriteRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final HotelRepository hotelRepository;

    @Transactional
    public void addFavorite(FavoriteReqBody favoriteReqBody) {
        Optional<Favorite> existingFavorite = favoriteRepository.findByHotelIdAndMemberId(
            favoriteReqBody.hotelId(),
            favoriteReqBody.memberId()
        );
        
        if (existingFavorite.isPresent()) {
            throw new ServiceException("400-1", "이미 즐겨찾기에 추가된 호텔입니다.");
        }

        Favorite favorite = Favorite.builder()
                .hotelId(favoriteReqBody.hotelId())
                .memberId(favoriteReqBody.memberId())
                .build();

        favoriteRepository.save(favorite);
    }

    @Transactional
    public RsData<String> deleteFavorite(FavoriteReqBody request) {
        Favorite favorite = favoriteRepository.findByHotelIdAndMemberId(request.hotelId(), request.memberId())
                .orElseThrow(() -> new ServiceException("400-1", "즐겨찾기를 찾을 수 없습니다."));
        favoriteRepository.delete(favorite);
        return new RsData<>("200-1", "즐겨찾기 삭제 성공");
    }

    public List<Hotel> getFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        List<Hotel> hotelList = new ArrayList<>();
        
        for (Favorite favorite : favorites) {
            Hotel hotel = hotelRepository.findById(favorite.getHotelId()).orElse(null);
            hotelList.add(hotel);
        }
        
        return hotelList;
    }

}