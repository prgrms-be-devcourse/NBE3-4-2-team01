package com.ll.hotel.global.initData;

import com.ll.hotel.domain.booking.payment.dto.PaymentRequest;
import com.ll.hotel.domain.booking.payment.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.Instant;

@Configuration
@RequiredArgsConstructor
public class BaseInit {
    private final PaymentService paymentService;

    @Autowired
    @Lazy
    private BaseInit self;

    @Bean
    public ApplicationRunner baseInitApplicationRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (paymentService.count() > 0) return;

        Long currentTimestamp = Instant.now().getEpochSecond();
        paymentService.create(new PaymentRequest("BASEINIT01", 1001, currentTimestamp));
        paymentService.create(new PaymentRequest("BASEINIT02", 1002, currentTimestamp));
        paymentService.create(new PaymentRequest("BASEINIT03", 1003, currentTimestamp));
        paymentService.create(new PaymentRequest("BASEINIT04", 1004, currentTimestamp));
    }
}
