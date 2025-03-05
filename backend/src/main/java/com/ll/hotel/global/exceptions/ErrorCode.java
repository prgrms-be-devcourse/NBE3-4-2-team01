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

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    // Business
    NOT_BUSINESS(HttpStatus.FORBIDDEN, "사업가만 관리할 수 있습니다."),
    INVALID_BUSINESS(HttpStatus.FORBIDDEN, "해당 호텔의 사업가가 아닙니다."),
    BUSINESS_NOT_FOUND(HttpStatus.NOT_FOUND, "사업자가 존재하지 않습니다."),
    BUSINESS_HOTEL_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "한 사업자는 하나의 호텔만 등록할 수 있습니다."),

    // Hotel
    HOTEL_NOT_FOUND(HttpStatus.NOT_FOUND, "호텔이 존재하지 않습니다"),
    HOTEL_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "호텔 상태 정보를 정확히 입력해주세요."),
    HOTEL_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "동일한 이메일의 호텔이 이미 존재합니다."),

    // Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "객실이 존재하지 않습니다"),
    ROOM_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "객실 상태 정보를 정확히 입력해주세요."),
    ROOM_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "동일한 이름의 방이 이미 호텔에 존재합니다."),

    // HotelOption
    HOTEL_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "사용할 수 없는 호텔 옵션이 존재합니다."),

    // RoomOption
    ROOM_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "사용할 수 없는 객실 옵션이 존재합니다."),

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

    // Filtering
    PAGE_SIZE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "pageSize는 1에서 100 사이로 입력해주세요."),
    INVALID_CHECK_IN_OUT_DATE(HttpStatus.BAD_REQUEST, "체크인 날짜는 체크아웃 날짜보다 늦을 수 없습니다."),
    INVALID_FILTER_DIRECTION(HttpStatus.BAD_REQUEST, "정렬 방향은 ASC 또는 DESC만 가능합니다.");

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