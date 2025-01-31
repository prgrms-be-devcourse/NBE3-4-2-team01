package com.ll.hotel.domain.member.favorite.service;

import com.ll.hotel.domain.member.favorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public void addFavorite() {
    }

    public void deleteFavorite() {
    }

    public void getFavorites() {
    }

}
