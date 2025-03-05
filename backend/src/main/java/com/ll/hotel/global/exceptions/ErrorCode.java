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

    // Review
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다"),
    REVIEW_CREATION_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 생성 권한이 없습니다"),
    REVIEW_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 조회 권한이 없습니다"),
    REVIEW_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 수정 권한이 없습니다"),
    REVIEW_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 삭제 권한이 없습니다"),
    REVIEW_IMAGE_REGISTRATION_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 사진 저장 권한이 없습니다"),
    USER_REVIEW_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "내 리뷰 목록 조회는 손님만 가능합니다"),

    // ReviewComment
    REVIEW_COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "리뷰 답변이 존재하지 않습니다"),
    REVIEW_COMMENT_CREATION_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 답변 생성 권한이 없습니다"),
    REVIEW_COMMENT_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 답변 수정 권한이 없습니다"),
    REVIEW_COMMENT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "리뷰 답변 삭제 권한이 없습니다"),

    // S3
    S3_PRESIGNED_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Presigned URL 생성 실패"),
    S3_OBJECT_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 객체 삭제 실패"),
    S3_OBJECT_ACCESS_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 객체 조회 실패"),

    // 예약
    BOOKING_CANCEL_TO_CANCEL(HttpStatus.BAD_REQUEST, "이미 취소된 예약입니다."),
    BOOKING_COMPLETE_TO_CANCEL(HttpStatus.BAD_REQUEST, "완료된 예약은 취소할 수 없습니다."),
    BOOKING_COMPLETE_TO_COMPLETE(HttpStatus.BAD_REQUEST, "이미 완료된 예약입니다."),
    BOOKING_CANCEL_TO_COMPLETE(HttpStatus.BAD_REQUEST, "취소된 예약은 완료 처리할 수 없습니다."),

    BOOKING_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "예약 조회 권한이 없습니다."),
    BOOKING_CANCEL_FORBIDDEN(HttpStatus.FORBIDDEN, "예약 취소 권한이 없습니다."),
    BOOKING_COMPLETE_FORBIDDEN(HttpStatus.FORBIDDEN, "예약 완료 처리 권한이 없습니다."),

    BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다."),
    BOOKING_MY_HOTEL_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 호텔이 없습니다."),
    BOOKING_HOTEL_NOT_FOUND(HttpStatus.NOT_FOUND, "예약할 호텔이 존재하지 않습니다."),
    BOOKING_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "예약할 객실이 존재하지 않습니다."),

    BOOKING_CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "예약 생성에 실패했습니다."),
    BOOKING_CANCEL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "예약 취소에 실패했습니다."),
    BOOKING_COMPLETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "예약 완료 처리에 실패했습니다."),

    // 결
    PAYMENT_TOKEN_FORBIDDEN(HttpStatus.FORBIDDEN, "토큰 발급 권한이 없습니다."),
    PAYMENT_CANCEL_FORBIDDEN(HttpStatus.FORBIDDEN, "결제 취소 권한이 없습니다."),

    PAYMENT_CANCEL_TO_CANCEL(HttpStatus.BAD_REQUEST, "이미 취소된 결제입니다."),

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),

    PAYMENT_UID_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Uid 생성에 실패했습니다."),
    PAYMENT_CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "결제 정보 저장에 실패했습니다."),
    PAYMENT_TOKEN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 발급에 실패했습니다."),
    PAYMENT_CANCEL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "결제 취소에 실패했습니다.");

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