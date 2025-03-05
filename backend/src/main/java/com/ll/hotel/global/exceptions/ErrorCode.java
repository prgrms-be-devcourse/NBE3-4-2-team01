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
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_LOGGED_OUT(HttpStatus.UNAUTHORIZED, "로그아웃된 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "리프레시 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),
    
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원이 존재하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    OAUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 OAuth 정보를 찾을 수 없습니다."),
    OAUTH_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "OAuth 로그인에 실패했습니다."),
    
    FAVORITE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 즐겨찾기에 추가된 호텔입니다."),
    FAVORITE_NOT_FOUND(HttpStatus.BAD_REQUEST, "즐겨찾기에 없는 호텔입니다."),
    
    HOTEL_NOT_FOUND(HttpStatus.BAD_REQUEST, "호텔이 존재하지 않습니다"),

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
    
    BUSINESS_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "사업가만 관리할 수 있습니다."),
    
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    S3_PRESIGNED_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Presigned URL 생성 실패"),
    S3_OBJECT_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 객체 삭제 실패"),
    S3_OBJECT_ACCESS_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 객체 조회 실패");

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