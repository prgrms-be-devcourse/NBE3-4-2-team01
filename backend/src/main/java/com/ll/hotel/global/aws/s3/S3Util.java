package com.ll.hotel.global.aws.s3;

import com.ll.hotel.domain.image.type.ImageType;

import java.util.UUID;

public class S3Util {
    private S3Util(){}

    // 사진 저장 path 설정
    public static String buildS3Key(ImageType imageType, long id, String fileType) {
        String fileName = UUID.randomUUID().toString() + "." + fileType;
        return switch (imageType) {
            case HOTEL -> "hotels/" + id + "/" + fileName;
            case ROOM -> "rooms/" + id + "/" + fileName;
            case REVIEW -> "reviews/" + id + "/" + fileName;
        };
    }
}
