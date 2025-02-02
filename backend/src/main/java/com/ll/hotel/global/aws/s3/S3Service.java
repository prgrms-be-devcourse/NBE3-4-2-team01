package com.ll.hotel.global.aws.s3;

import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.global.exceptions.CustomS3Exception;
import com.ll.hotel.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    String bucketName;

    // 여러 사진 한번에 저장
    public List<URL> generatePresignedUrls(ImageType imageType, long id, List<String> fileTypes) {
        if(!Ut.list.hasValue(fileTypes)) return List.of();

        try {
            return fileTypes.stream()
                    .map(fileType -> createPresignedUrlResponse(imageType, id, fileType))
                    .toList();
        } catch (SdkException e) {
            throw new CustomS3Exception("500-1", "Presigned URL 생성 실패", e);
        }
    }

    // 사진 1개 저장
    public URL createPresignedUrlResponse(ImageType imageType, long id, String fileType) {
        String key = S3Util.buildS3Key(imageType, id, fileType);// 임시

        try {
            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(builder -> builder
                    .putObjectRequest(putObject -> putObject
                            .bucket(bucketName)
                            .key(key))
                    .signatureDuration(Duration.ofMinutes(10)));

            return presignedRequest.url();
        } catch (SdkException e) {
            throw new CustomS3Exception("500-2", "Presigned URL 생성 실패", e);
        }
    }
}
