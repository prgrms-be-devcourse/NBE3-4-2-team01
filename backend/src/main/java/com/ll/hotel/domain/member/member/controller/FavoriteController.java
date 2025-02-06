package com.ll.hotel.domain.favorite.controller;

import java.util.List;

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
    public RsData<Void> addFavorite(@PathVariable Long hotelId) {
        memberService.addFavorite(hotelId);
        return new RsData<>("200-1", "즐겨찾기에 추가되었습니다.");
    }

    @DeleteMapping("/{hotelId}")
    public RsData<Void> removeFavorite(@PathVariable Long hotelId) {
        memberService.removeFavorite(hotelId);
        return new RsData<>("200-1", "즐겨찾기가 삭제되었습니다.");
    }

    @GetMapping("/me")
    public RsData<List<HotelDto>> getFavorites() {
        List<HotelDto> favorites = memberService.getFavoriteHotels();
        
        if (favorites.isEmpty()) {
            return new RsData<>("200-1", "즐겨찾기한 호텔이 없습니다.", List.of());
        }

        return new RsData<>("200-1", "즐겨찾기 목록을 조회했습니다.", favorites);
    }
} 