package com.ll.hotel.domain.booking.payment.controller;

import com.ll.hotel.domain.booking.payment.dto.PaymentResponse;
import com.ll.hotel.domain.booking.payment.dto.UidResponse;
import com.ll.hotel.domain.booking.payment.service.PaymentService;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.base.Empty;
import com.ll.hotel.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/bookings/payments/")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public String paymentTest() {
        return "paymentTest";
    }

    @GetMapping("/merchant_uid")
    @ResponseBody
    @Transactional
    public RsData<UidResponse> getMerchantUid() {
        String Uid = Ut.random.generateUID(10);
        return new RsData<>(
                "200",
                "Uid 생성에 성공했습니다.",
                new UidResponse(Uid)
        );
    }

    @PostMapping
    @ResponseBody
    @Transactional
    public RsData<Empty> saveUids(
            @RequestParam String merchantUid,
            @RequestParam String impUid) {
        paymentService.saveUids(merchantUid, impUid);
        return new RsData<>(
                "200",
                "결제에 성공했습니다."
        );
    }
}
