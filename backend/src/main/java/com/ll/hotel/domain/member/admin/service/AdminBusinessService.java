package com.ll.hotel.domain.member.admin.service;

import com.ll.hotel.domain.member.admin.dto.request.AdminBusinessRequest;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBusinessService {
    private final BusinessRepository businessRepository;

    public Page<Business> findAllPaged(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return businessRepository.findAll(pageable);
    }

    public Business findById(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "존재하지 않는 사업자입니다."));
    }

    @Transactional
    public void approve(Business business, AdminBusinessRequest adminBusinessRequest) {
        business.setApprovalStatus(adminBusinessRequest.businessApprovalStatus());
    }

    public void flush() {
        businessRepository.flush();
    }
}
