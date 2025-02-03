package com.ll.hotel.domain.member.member.service;

import com.ll.hotel.domain.member.member.dto.request.BusinessRequest;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository businessRepository;

    @Transactional
    public Business register(BusinessRequest businessRequest, Member member) {
        Business business = Business
                .builder()
                .businessRegistrationNumber(businessRequest.businessRegistrationNumber())
                .approvalStatus(BusinessApprovalStatus.PENDING)
                .member(member)
                .hotel(null)
                .build();
        return businessRepository.save(business);
    }
}
