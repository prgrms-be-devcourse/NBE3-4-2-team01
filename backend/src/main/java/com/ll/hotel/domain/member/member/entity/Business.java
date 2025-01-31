package com.ll.hotel.domain.member.member.entity;

import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Business extends BaseEntity {

    @NotBlank(message = "사업자 등록 번호는 필수 항목입니다.")
    @Pattern(regexp = "^[0-9]{10}$", message = "사업자 등록 번호는 10자리 숫자여야 합니다.")
    @Column(name = "business_registration_number", nullable = false, unique = true)
    private String businessRegistrationNumber;

    @NotNull(message = "사업자 승인 상태는 필수 항목입니다.")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "business_approval_status", nullable = false)
    private BusinessApprovalStatus approvalStatus = BusinessApprovalStatus.PENDING;

    @NotNull(message = "회원 정보는 필수 항목입니다.")
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
