package com.ll.hotel.domain.member.member.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.global.rsData.RsData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final MemberService memberService;

    @PostMapping("/{hotelId}")
    public RsData<?> addFavorite(@PathVariable Long hotelId) {
        memberService.addFavorite(hotelId);
        return RsData.success(HttpStatus.OK, "즐겨찾기에 추가되었습니다.");
    }

    @DeleteMapping("/{hotelId}")
    public RsData<?> removeFavorite(@PathVariable Long hotelId) {
        memberService.removeFavorite(hotelId);
        return RsData.success(HttpStatus.OK, "즐겨찾기가 삭제되었습니다.");
    }

    @GetMapping("/me")
    public RsData<List<HotelDto>> getFavorites() {
        List<HotelDto> favorites = memberService.getFavoriteHotels();
        
        if (favorites.isEmpty()) {
            return RsData.success(HttpStatus.OK, List.of());
        }

        return RsData.success(HttpStatus.OK, favorites);
    }

    @GetMapping("/me/{hotelId}")
    public RsData<Boolean> checkFavorite(@PathVariable("hotelId") Long hotelId) {
        boolean isFavorite = memberService.isFavoriteHotel(hotelId);
        return RsData.success(HttpStatus.OK, isFavorite);
    }
} 