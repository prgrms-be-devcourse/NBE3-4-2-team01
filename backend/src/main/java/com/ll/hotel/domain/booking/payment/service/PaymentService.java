package com.ll.hotel.domain.booking.payment.service;

import com.ll.hotel.domain.booking.payment.dto.PaymentRequest;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.repository.PaymentRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    @Value("${spring.portone.impKey}")
    private String impKey;
    @Value("${spring.portone.impSecret}")
    private String impSecret;

    //결제 정보 저장
    @Transactional
    public Payment create(PaymentRequest paymentRequest) {
        // Unix Timestamp를 LocalDateTime으로 변환
        LocalDateTime paidAt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(paymentRequest.paidAtTimestamp()),
                ZoneId.systemDefault()
        );

        Payment payment = Payment.builder()
                .merchantUid(paymentRequest.merchantUid())
                .amount(paymentRequest.amount())
                .paidAt(paidAt)
                .build();

        return paymentRepository.save(payment);
    }

    /*
     * 결제 취소
     * portone api를 통해 결제는 취소하지만 row는 삭제하지 않음 (soft delete)
     * paymentStatus를 CANCELLED로 변경하여 취소로 처리

    @Transactional
    public Payment softDelete(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ServiceException("404", "결제 정보를 찾을 수 없습니다."));


    }*/



    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ServiceException("404", "결제 정보를 찾을 수 없습니다."));
    }
}
