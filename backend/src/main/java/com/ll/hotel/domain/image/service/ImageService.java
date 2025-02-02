package com.ll.hotel.domain.image.service;

import com.ll.hotel.domain.image.entity.Image;
import com.ll.hotel.domain.image.repository.ImageRepository;
import com.ll.hotel.domain.image.type.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    public void saveImages(ImageType imageType, long id, List<String> imageUrls) {
        List<Image> images = imageUrls.stream()
                .map(imageUrl -> ImageConverter.toImage(imageType, id, imageUrl))
                .toList();

        imageRepository.saveAll(images);
    }
}
