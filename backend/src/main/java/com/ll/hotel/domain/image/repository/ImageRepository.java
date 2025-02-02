package com.ll.hotel.domain.image.repository;

import com.ll.hotel.domain.image.entity.Image;
import com.ll.hotel.domain.image.type.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByImageTypeAndReferenceId(ImageType imageType, Long referenceId);
}
