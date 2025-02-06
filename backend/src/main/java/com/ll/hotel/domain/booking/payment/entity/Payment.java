package com.ll.hotel.domain.booking.payment.entity;

import com.ll.hotel.domain.booking.payment.type.PaymentStatus;
import com.ll.hotel.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseTime {
    /*
     * 결제 요청/취소 시 merchant_uid가 필요
     * merchant_uid는 상점(호텔) 측에서 생성하는 주문번호
     * api key는 application.yml에 작성
     */
    @NotNull(message = "거래 UID는 필수입니다.")
    @Column(unique = true)
    private String merchantUid;

    @NotNull(message = "거래 금액은 필수입니다.")
    @Column
    private int amount;

    @NotNull(message = "거래 상태 정보는 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PAID;

    @NotNull(message = "거래 일자는 필수입니다.")
    @Column
    private LocalDateTime paidAt;
}
