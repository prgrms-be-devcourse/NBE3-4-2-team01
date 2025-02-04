package com.ll.hotel.domain.member.favorite.controller;

import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.member.favorite.dto.FavoriteReqBody;
import com.ll.hotel.domain.member.favorite.entity.Favorite;
import com.ll.hotel.domain.member.favorite.service.FavoriteService;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/id")
    public RsData<Void> addFavorite(@RequestBody FavoriteReqBody favoriteReqBody) {
        try {
            favoriteService.addFavorite(favoriteReqBody);
            return new RsData<>("200-1", "즐겨찾기가 추가되었습니다.");
        } catch (ServiceException e) {
            return new RsData<>("400-1", e.getMessage());
        }
    }

    @DeleteMapping({"/id"})
    public RsData<String> deleteFavorite(@RequestBody FavoriteReqBody request) {
        return favoriteService.deleteFavorite(request);
    }

    @GetMapping({"/me"})
    public RsData<List<HotelDto>> getFavorites() {
        List<HotelDto> favorites = favoriteService.getFavorites().stream()
                .map(HotelDto::new)
                .collect(Collectors.toList());
        return new RsData<>("200-1", "즐겨찾기 조회 성공", favorites);
    }
} 