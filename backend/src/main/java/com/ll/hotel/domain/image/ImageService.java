package com.ll.hotel.domain.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public List<Image> findImagesByHotelId(long id) {
        return imageRepository.findByImageTypeAndReferenceId(ImageType.HOTEL, id);
    }

    public List<Image> findImagesByRoomId(long id) {
        return imageRepository.findByImageTypeAndReferenceId(ImageType.ROOM, id);
    }

    public List<Image> findImagesByReviewId(long id) {
        return imageRepository.findByImageTypeAndReferenceId(ImageType.REVIEW, id);
    }
}