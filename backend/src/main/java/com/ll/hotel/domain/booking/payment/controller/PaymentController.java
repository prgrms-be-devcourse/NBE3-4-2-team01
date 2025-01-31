package com.ll.hotel.domain.booking.payment.controller;

import com.ll.hotel.domain.booking.payment.dto.PaymentResponse;
import com.ll.hotel.domain.booking.payment.dto.UidsResponse;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.service.PaymentService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import com.ll.hotel.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @Value("${spring.portone.apiId}")
    private String apiId;
    @Value("${spring.portone.channel-key}")
    private String channelKey;

    @GetMapping("/uids")
    public RsData<UidsResponse> getUids() {
        String merchantUid = Ut.random.generateUID(10);
        return new RsData<>(
                "200",
                "Uid 발급에 성공했습니다.",
                new UidsResponse(apiId, channelKey, merchantUid)
        );
    }

    @PostMapping
    @Transactional
    public RsData<PaymentResponse> pay(
            @RequestParam("merchant_uid") String merchantUid,
            @RequestParam("imp_uid") String impUid) {
        Payment payment = paymentService.create(merchantUid, impUid);
        return new RsData<>(
                "200",
                "결제에 성공했습니다.",
                PaymentResponse.from(payment)
        );
    }
}
