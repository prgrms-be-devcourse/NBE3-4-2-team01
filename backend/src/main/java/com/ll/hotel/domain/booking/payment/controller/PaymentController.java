package com.ll.hotel.domain.booking.payment.controller;

import com.ll.hotel.domain.booking.payment.dto.PaymentRequest;
import com.ll.hotel.domain.booking.payment.dto.PaymentResponse;
import com.ll.hotel.domain.booking.payment.dto.UidResponse;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.service.PaymentService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.util.Ut;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    /*
     * portone api 호출에 필요한 keys
     * application.yml에 저장된 값을 가져옴
     */
    @Value("${api-keys.portone.apiId}")
    private String apiId;
    @Value("${api-keys.portone.channel-key}")
    private String channelKey;

    // 상점 uid 및 api key 발급
    @GetMapping("/uid")
    public RsData<UidResponse> getUid() {
        String merchantUid = Ut.random.generateUID(10);
        return new RsData<>(
                "201",
                "Uid 발급에 성공했습니다.",
                new UidResponse(apiId, channelKey, merchantUid)
        );
    }

    // 결제
    @PostMapping
    @Transactional
    public RsData<PaymentResponse> pay(
            @RequestBody @Valid PaymentRequest paymentRequest) {
        Payment payment = paymentService.create(paymentRequest);
        return new RsData<>(
                "201",
                "결제에 성공했습니다.",
                PaymentResponse.from(payment)
        );
    }

    // 조회
    @GetMapping("/{payment_id}")
    public RsData<PaymentResponse> getPayment(
            @PathVariable("payment_id") Long paymentId) {
        Payment payment = paymentService.findById(paymentId);
        return new RsData<>(
                "200",
                "결제 정보를 조회했습니다.",
                PaymentResponse.from(payment)
        );
    }

    @DeleteMapping("/{payment_id}")
    @Transactional
    public RsData<PaymentResponse> cancel(
            @PathVariable("payment_id") Long paymentId) {
        Payment payment = paymentService.softDelete(paymentId);
        return new RsData<>(
                "200",
                "환불에 성공했습니다.",
                PaymentResponse.from(payment)
        );
    }
}
