package com.ll.hotel.domain.hotel.room.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 변경 가능성 높음
 */
@Getter
@AllArgsConstructor
public enum BedTypeNumber {
    SINGLE(1);

    private final int value;

    public String getBedTypeNumber() {
        return this.name() + " / " + this.getValue();
    }
}
