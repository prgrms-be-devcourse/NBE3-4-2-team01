package com.ll.hotel.global.exceptions;

import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;

public class CustomS3Exception extends RuntimeException {
    private final String resultCode;
    private final String msg;


    public CustomS3Exception(String resultCode, String msg, Throwable cause) {
        super(resultCode + ":" + msg, cause);
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public RsData<Empty> getRsData() {
        return new RsData<>(resultCode, msg);
    }
}