package com.ll.hotel.domain.member.member.controller;

import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/me/{hotelId}")
    public RsData<Boolean> checkFavorite(
            @PathVariable("hotelId")long hotelId
    ) {
        boolean isFavorite = memberService.isFavoriteHotel(hotelId);

        return new RsData<>("200-1", "즐겨찾기 아이디들을 조회했습니다.", isFavorite);
    }
} 