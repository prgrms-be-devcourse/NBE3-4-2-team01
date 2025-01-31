package com.ll.hotel.domain.booking.payment.service;

import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment create(String merchantUid, String impUid) {
        Payment payment = Payment.builder()
                .merchantUid(merchantUid)
                .impUid(impUid)
                .build();

        return paymentRepository.save(payment);
    }
}
