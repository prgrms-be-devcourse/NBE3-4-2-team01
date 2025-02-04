package com.ll.hotel.domain.member.favorite.controller;

import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.member.favorite.dto.FavoriteReqBody;
import com.ll.hotel.domain.member.favorite.entity.Favorite;
import com.ll.hotel.domain.member.favorite.service.FavoriteService;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final HotelRepository hotelRepository;

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

    @GetMapping("/me")
    public RsData<List<HotelDto>> getFavorites() {
        List<Favorite> favorites = favoriteService.getFavorites();
        
        if (favorites.isEmpty()) {
            return new RsData<>("200-1", "즐겨찾기한 호텔이 없습니다.", List.of());
        }

        List<HotelDto> hotelDtos = favorites.stream()
            .filter(Objects::nonNull)
            .map(favorite -> {
                Hotel hotel = hotelRepository.findById(favorite.getHotelId())
                    .orElseThrow(() -> new ServiceException("404-1", "호텔 정보를 찾을 수 없습니다."));
                return new HotelDto(hotel);
            })
            .collect(Collectors.toList());

        return new RsData<>("200-1", "즐겨찾기 목록을 조회했습니다.", hotelDtos);
    }
} 