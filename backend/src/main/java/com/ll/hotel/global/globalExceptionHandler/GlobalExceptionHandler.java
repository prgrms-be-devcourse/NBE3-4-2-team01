package com.ll.hotel.global.globalExceptionHandler;

import com.ll.hotel.global.exceptions.CustomS3Exception;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handle(ServiceException ex) {

        String message = ex.getMsg();

        return ResponseEntity
                .status(ex.getResultCode())
                .body(message);
    }

    @ExceptionHandler(CustomS3Exception.class)
    public ResponseEntity<String> handle(CustomS3Exception ex) {
        String message = ex.getMsg();

        return ResponseEntity
                .status(ex.getResultCode())
                .body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body("Validation failed: " + ex.getBindingResult().toString());
    }
}
