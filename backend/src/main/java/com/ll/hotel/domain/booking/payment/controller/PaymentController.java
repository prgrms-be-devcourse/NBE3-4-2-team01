package com.ll.hotel.domain.booking.payment.controller;

import com.ll.hotel.domain.booking.payment.dto.PaymentRequest;
import com.ll.hotel.domain.booking.payment.dto.PaymentResponse;
import com.ll.hotel.domain.booking.payment.dto.UidResponse;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.service.PaymentService;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
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
    private final Rq rq;

    /*
     * portone api 호출에 필요한 keys
     * application-api-keys.yml에 저장된 값을 가져옴
     */
    @Value("${api-keys.portone.apiId}")
    private String apiId;
    @Value("${api-keys.portone.channel-key}")
    private String channelKey;

    /*
     * 상점 uid 및 api key 발급
     * 결제 관련 api는 /uid 외에 실제로 호출하도록 설계되지 않았음
     * 다만 테스트용으로 작성한 바가 있음
     */
    @GetMapping("/uid")
    public RsData<UidResponse> getUid() {
        Member actor = rq.getActor();
        if (actor == null) {
            throw new ServiceException("401", "Uid 발급 권한이 없습니다.");
        }

        String merchantUid = paymentService.generateMerchantUid();
        return new RsData<>(
                "200",
                "Uid 발급에 성공했습니다.",
                new UidResponse(apiId, channelKey, merchantUid)
        );
    }

    // 결제
    @PostMapping
    @Transactional
    public RsData<PaymentResponse> pay(
            @RequestBody @Valid PaymentRequest paymentRequest) {
        Member actor = rq.getActor();
        if (actor == null || !actor.isAdmin()) {
            throw new ServiceException("401", "결제 api 호출 권한이 없습니다.");
        }

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
        Member actor = rq.getActor();
        if (actor == null || !actor.isAdmin()) {
            throw new ServiceException("401", "결제 api 호출 권한이 없습니다.");
        }

        Payment payment = paymentService.findById(paymentId);
        return new RsData<>(
                "200",
                "결제 정보 조회에 성공했습니다.",
                PaymentResponse.from(payment)
        );
    }

    @DeleteMapping("/{payment_id}")
    @Transactional
    public RsData<PaymentResponse> cancel(
            @PathVariable("payment_id") Long paymentId) {
        Member actor = rq.getActor();
        if (actor == null || !actor.isAdmin()) {
            throw new ServiceException("401", "결제 api 호출 권한이 없습니다.");
        }

        Payment payment = paymentService.softDelete(paymentId);
        return new RsData<>(
                "200",
                "결제 취소에 성공했습니다.",
                PaymentResponse.from(payment)
        );
    }
}
