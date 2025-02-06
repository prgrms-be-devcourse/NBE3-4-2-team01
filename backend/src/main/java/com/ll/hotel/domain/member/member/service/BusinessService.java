package com.ll.hotel.domain.member.member.service;

import com.ll.hotel.domain.member.member.dto.request.BusinessRequest;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository businessRepository;

    @Transactional
    public Business register(BusinessRequest.RegistrationInfo registrationInfo, Member member, String validationResult) {

        BusinessApprovalStatus status;

        if (validationResult.equals("01")) {
            status = BusinessApprovalStatus.APPROVED;
        } else {
            status = BusinessApprovalStatus.REJECTED;
        }

        Business business = Business
                .builder()
                .businessRegistrationNumber(registrationInfo.businessRegistrationNumber())
                .startDate(registrationInfo.startDate())
                .ownerName(registrationInfo.ownerName())
                .approvalStatus(status)
                .member(member)
                .hotel(null)
                .build();
        return businessRepository.save(business);
    }
}
