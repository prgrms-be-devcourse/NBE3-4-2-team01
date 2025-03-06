package com.ll.hotel.global.aspect;

import com.ll.hotel.global.exceptions.CustomS3Exception;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionAspect {
    @AfterThrowing(pointcut = "@within(org.springframework.stereotype.Service)", throwing = "ex")

    public void handleException(JoinPoint joinPoint, Exception ex) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String status;
        String msg;

        if (ex instanceof ServiceException) {
            ServiceException exception = (ServiceException) ex;
            status = exception.getResultCode().toString();
            msg = exception.getMsg();
        } else if (ex instanceof CustomS3Exception) {
            CustomS3Exception exception = (CustomS3Exception) ex;
            status = exception.getResultCode().toString();
            msg = exception.getMsg();
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            status = HttpStatus.BAD_REQUEST.toString();
            msg = exception.getMessage();
        } else {
            status = "UNKNOWN";
            msg = ex.getMessage();
        }

        log.error("ERROR = [{}.{}], status: [{}], message: [{}]",
                className, methodName, status, msg
        );
    }
}