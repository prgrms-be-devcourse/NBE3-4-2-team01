package com.ll.hotel.global.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Getter
@AllArgsConstructor
@Slf4j
public enum ErrorCode {
    PAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "페이지가 존재하지 않습니다"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    HOTEL_NOT_FOUND(HttpStatus.BAD_REQUEST, "호텔이 존재하지 않습니다"),

    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션이 존재하지 않습니다"),
    OPTION_IN_USE(HttpStatus.BAD_REQUEST, "사용 중인 옵션은 삭제할 수 없습니다"),

    BUSINESS_NOT_FOUND(HttpStatus.NOT_FOUND, "사업자가 존재하지 않습니다"),
    INVALID_BUSINESS(HttpStatus.BAD_REQUEST, "사업자가 유효하지 않습니다"),

    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다"),
    REVIEW_CREATION_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 생성 권한이 없습니다"),
    REVIEW_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 조회 권한이 없습니다"),
    REVIEW_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 수정 권한이 없습니다"),
    REVIEW_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 삭제 권한이 없습니다"),
    REVIEW_IMAGE_REGISTRATION_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 사진 저장 권한이 없습니다"),
    USER_REVIEW_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "내 리뷰 목록 조회는 손님만 가능합니다"),

    REVIEW_COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "리뷰 답변이 존재하지 않습니다"),
    REVIEW_COMMENT_CREATION_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 답변 생성 권한이 없습니다"),
    REVIEW_COMMENT_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 답변 수정 권한이 없습니다"),
    REVIEW_COMMENT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 답변 삭제 권한이 없습니다"),

    S3_PRESIGNED_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Presigned URL 생성 실패"),
    S3_OBJECT_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 객체 삭제 실패"),
    S3_OBJECT_ACCESS_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 객체 조회 실패"),

    EXTERNAL_API_UNEXPECTED_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "API 응답이 올바르지 않습니다"),
    EXTERNAL_API_COMMUNICATION_ERROR(HttpStatus.BAD_GATEWAY, "API 요청 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public ServiceException throwServiceException() {
        throw new ServiceException(httpStatus, message);
    }

    public ServiceException throwServiceException(Throwable cause) {
        throw new ServiceException(httpStatus, message, cause);
    }

    public S3Exception throwS3Exception(Throwable cause) {
        throw new CustomS3Exception(httpStatus, message, cause);
    }
}