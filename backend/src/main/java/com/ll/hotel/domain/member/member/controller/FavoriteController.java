package com.ll.hotel.domain.member.member.controller;

import com.ll.hotel.domain.member.member.dto.FavoriteDto;
import com.ll.hotel.domain.member.member.service.MemberService;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final MemberService memberService;

    @PostMapping("/{hotelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFavorite(@PathVariable Long hotelId) {
        memberService.addFavorite(hotelId);
    }

    @DeleteMapping("/{hotelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavorite(@PathVariable Long hotelId) {
        memberService.removeFavorite(hotelId);
    }

    @GetMapping("/me")
    public RsData<List<FavoriteDto>> getFavorites() {
        List<FavoriteDto> favorites = memberService.getFavoriteHotels();
        
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