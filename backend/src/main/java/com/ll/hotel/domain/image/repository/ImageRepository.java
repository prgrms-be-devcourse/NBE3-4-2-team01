package com.ll.hotel.domain.image.repository;

import com.ll.hotel.domain.image.entity.Image;
import com.ll.hotel.domain.image.type.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByImageTypeAndReferenceId(ImageType imageType, Long referenceId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Image i WHERE i.referenceId = :referenceId AND i.imageType = :imageType AND i.imageUrl IN :imageUrls")
    void deleteByReferenceIdAndImageUrls(
            @Param("imageType") ImageType imageType,
            @Param("referenceId") Long referenceId,
            @Param("imageUrls") List<String> imageUrls
    );
}
