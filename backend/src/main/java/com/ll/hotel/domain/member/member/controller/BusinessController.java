package com.ll.hotel.domain.member.member.controller;

import com.ll.hotel.domain.member.member.dto.request.BusinessRequest;
import com.ll.hotel.domain.member.member.dto.response.BusinessResponse;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.service.BusinessService;
import com.ll.hotel.global.rq.Rq;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/businesses")
@RequiredArgsConstructor
public class BusinessController {
    private final BusinessService businessService;
    private final Rq rq;

    @PostMapping("/register")
    public RsData<BusinessResponse> register(@RequestBody @Valid BusinessRequest businessRequest) {

        Member member =rq.getActor();
        Business business = businessService.register(businessRequest, member);

        return new RsData<>(
                "201",
                "사업자 정보가 등록되었습니다.",
                BusinessResponse.of(business)
        );
    }
}
