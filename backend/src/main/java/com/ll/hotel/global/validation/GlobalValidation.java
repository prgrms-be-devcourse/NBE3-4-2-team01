package com.ll.hotel.global.validation;

import com.ll.hotel.global.exceptions.ServiceException;
import java.time.LocalDate;

public class GlobalValidation {
    public static void checkPageSize(int pageSize) {
        if (pageSize < 1 || pageSize > 100) {
            throw new ServiceException("400-1", "pageSize 는 1에서 100 사이로 입력해주세요.");
        }
    }

    public static void checkCheckInAndOutDate(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new ServiceException("400-2", "체크인 날짜는 체크아웃 날짜보다 늦을 수 없습니다.");
        }
    }
}
