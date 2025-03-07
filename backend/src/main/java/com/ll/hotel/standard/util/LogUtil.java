package com.ll.hotel.standard.util;

import com.ll.hotel.global.app.AppConfig;
import com.ll.hotel.global.exceptions.CustomS3Exception;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.response.RsData;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class LogUtil {
    public static void logControllerRequest(Logger log, String className, String methodName) {
        log.info("Request Controller = [{}.{}]", className, methodName);
    }

    public static void logControllerResponse(Logger log, String className, String methodName, RsData<?> rsData)
            throws Throwable {
        String jsonData = AppConfig.getObjectMapper().writeValueAsString(rsData.getData());

        log.info("Response Controller = [{}.{}], status: [{}], message: [{}], data: [{}]",
                className, methodName, rsData.getResultCode(), rsData.getMsg(), jsonData
        );
    }

    public static void logServiceRequest(Logger log, String className, String methodName) {
        log.info("Request Service = [{}.{}]", className, methodName);
    }

    public static void logError(Logger log, JoinPoint joinPoint, Throwable ex) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String status;
        String msg;

        switch (ex) {
            case ServiceException exception -> {
                status = exception.getResultCode().toString();
                msg = exception.getMsg();
            }
            case CustomS3Exception exception -> {
                status = exception.getResultCode().toString();
                msg = exception.getMsg();
            }
            case MethodArgumentNotValidException exception -> {
                status = HttpStatus.BAD_REQUEST.toString();
                msg = exception.getMessage();
            }
            default -> {
                status = "UNKNOWN";
                msg = ex.getMessage();
            }
        }

        log.error("ERROR = [{}.{}], status: [{}], message: [{}]",
                className, methodName, status, msg
        );
    }
}