package com.ll.hotel.domain.member.favorite.service;


import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.member.favorite.dto.FavoriteReqBody;
import com.ll.hotel.domain.member.favorite.entity.Favorite;
import com.ll.hotel.domain.member.favorite.repository.FavoriteRepository;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final HotelRepository hotelRepository;
    private final Rq rq;

    @Transactional
    public void addFavorite(FavoriteReqBody favoriteReqBody) {
        hotelRepository.findById(favoriteReqBody.hotelId())
            .orElseThrow(() -> new ServiceException("404-1", "존재하지 않는 호텔입니다."));

        Member actor = rq.getActor();
        if (actor == null) {
            throw new ServiceException("401-1", "로그인이 필요합니다.");
        }

        Optional<Favorite> existingFavorite = favoriteRepository.findByHotelIdAndMemberId(
            favoriteReqBody.hotelId(),
            actor.getId()
        );
        
        if (existingFavorite.isPresent()) {
            throw new ServiceException("400-1", "이미 즐겨찾기에 추가된 호텔입니다.");
        }

        Favorite favorite = Favorite.builder()
                .hotelId(favoriteReqBody.hotelId())
                .memberId(actor.getId())
                .build();

        favoriteRepository.save(favorite);
    }

    @Transactional
    public RsData<String> deleteFavorite(FavoriteReqBody request) {
        Member actor = rq.getActor();
        if (actor == null) {
            throw new ServiceException("401-1", "로그인이 필요합니다.");
        }

        Favorite favorite = favoriteRepository.findByHotelIdAndMemberId(
                request.hotelId(), 
                actor.getId()
            )
            .orElseThrow(() -> new ServiceException("400-1", "즐겨찾기를 찾을 수 없습니다."));

        favoriteRepository.delete(favorite);
        return new RsData<>("200-1", "즐겨찾기 삭제 성공");
    }

    public List<Favorite> getFavorites() {
        Member actor = rq.getActor();
        return favoriteRepository.findByMemberId(actor.getId());
    }

}