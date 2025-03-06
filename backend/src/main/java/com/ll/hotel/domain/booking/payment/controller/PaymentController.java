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
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final Rq rq;

    // 상점 uid 및 api key 발급
    @GetMapping("/uid")
    public RsData<UidResponse> getUid() {
        Member actor = rq.getActor();

        return RsData.success(
                HttpStatus.OK,
                paymentService.generateMerchantUid()
        );
    }

    /*
     * 본래 결제 api와 예약 api는 독립적으로 설계되어 프론트에서 트랜잭션을 수행할 계획이었으나
     * 프론트 역량이 부족하여 결제와 예약 로직을 백엔드에서 동시에 처리하도록 함
     * 이에 결제 api는 독립적으로 호출이 불가능하도록 변경함
     *
    // 결제
    @PostMapping
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
    */
}