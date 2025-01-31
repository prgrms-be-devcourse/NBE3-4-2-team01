package com.ll.hotel.domain.booking.payment.entity;

import com.ll.hotel.domain.booking.payment.type.PaymentStatus;
import com.ll.hotel.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {
    @NotNull
    @Column(unique = true)
    private String merchantUid;

    @NotNull
    @Column(unique = true)
    private String impUid;

    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PAID;
}
