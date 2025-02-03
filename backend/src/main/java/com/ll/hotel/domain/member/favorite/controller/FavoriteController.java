package com.ll.hotel.domain.member.favorite.controller;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.member.favorite.dto.FavoriteReqBody;
import com.ll.hotel.domain.member.favorite.entity.Favorite;
import com.ll.hotel.domain.member.favorite.service.FavoriteService;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping({"/id"})
    public RsData<Favorite> addFavorite(@RequestBody FavoriteReqBody request) {
        Favorite favorite = favoriteService.addFavorite(request);
        return new RsData<>("200-1", "즐겨찾기 추가 성공", favorite);
    }

    @DeleteMapping({"/id"})
    public RsData<Favorite> deleteFavorite(@RequestBody FavoriteReqBody request) {
        Favorite favorite = favoriteService.deleteFavorite(request);
        return new RsData<>("200-1", "즐겨찾기 삭제 성공", favorite);
    }

    @GetMapping({"/me"})
    public RsData<List<Hotel>> getFavorites() {
        List<Hotel> favorites = favoriteService.getFavorites();
        return new RsData<>("200-1", "즐겨찾기 조회 성공", favorites);
    }
} 