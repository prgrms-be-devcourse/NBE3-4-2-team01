package com.ll.hotel.global.exceptions;

import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;

public class ServiceException extends RuntimeException {
    private final String resultCode;
    private final String msg;

    public ServiceException(String resultCode, String msg) {
        super(resultCode + " : " + msg);
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public RsData<Empty> getRsData() {
        return new RsData<>(resultCode, msg);
    }
}
