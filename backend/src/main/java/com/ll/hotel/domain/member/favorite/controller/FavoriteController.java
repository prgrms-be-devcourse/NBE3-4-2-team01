package com.ll.hotel.domain.member.favorite.controller;

import com.ll.hotel.domain.member.favorite.dto.FavoriteReqBody;
import com.ll.hotel.domain.member.favorite.service.FavoriteService;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping({"/id"})
    public ResponseEntity<RsData<FavoriteReqBody>> addFavorite(@RequestBody FavoriteReqBody request) {
        return ResponseEntity.ok(new RsData<>("200-1", "즐겨찾기 추가 성공", request));
    }

    @DeleteMapping({"/id"})
    public ResponseEntity<RsData<FavoriteReqBody>> deleteFavorite(@RequestBody FavoriteReqBody request) {
        return ResponseEntity.ok(new RsData<>("200-1", "즐겨찾기 삭제 성공", request));
    }

    @GetMapping({"/me"})
    public ResponseEntity<RsData<FavoriteReqBody>> getFavorites() {
        return ResponseEntity.ok(new RsData<>("200-1", "즐겨찾기 조회 성공", null));
    }
} 