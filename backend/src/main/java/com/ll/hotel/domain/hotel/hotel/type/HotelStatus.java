package com.ll.hotel.domain.hotel.hotel.type;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HotelStatus {
    AVAILABLE("사용 가능"),
    PENDING("승인 대기 중"),
    UNAVAILABLE("사용 불가");

    private final String value;

    public static HotelStatus fromValue(String value) {
        return Arrays.stream(HotelStatus.values())
                .filter(status -> status.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 호텔 상태 값: " + value));
    }
}
