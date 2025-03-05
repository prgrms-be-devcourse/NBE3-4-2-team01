package com.ll.hotel.domain.member.admin.controller;

import com.ll.hotel.domain.member.admin.dto.request.AdminBusinessRequest;
import com.ll.hotel.domain.member.admin.dto.response.AdminBusinessResponse;
import com.ll.hotel.domain.member.admin.service.AdminBusinessService;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.page.dto.PageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/businesses")
@RequiredArgsConstructor
public class AdminBusinessController {
    private final AdminBusinessService adminBusinessService;

    @GetMapping
    public RsData<PageDto<AdminBusinessResponse.Summary>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<AdminBusinessResponse.Summary> pagedBusinessSummaries = adminBusinessService.findAllPaged(page)
                .map(AdminBusinessResponse.Summary::from);

        if (!pagedBusinessSummaries.hasContent()) {
            throw new ServiceException("404", "요청하신 사업자 정보 페이지가 없습니다.");
        }

        return RsData.success(HttpStatus.OK, new PageDto<>(pagedBusinessSummaries));
    }

    @GetMapping("/{id}")
    public RsData<AdminBusinessResponse.Detail> getById(@PathVariable("id") Long id) {

        Business business = adminBusinessService.findById(id);

        return RsData.success(HttpStatus.OK, AdminBusinessResponse.Detail.from(business));
    }

    @PatchMapping("/{id}")
    public RsData<AdminBusinessResponse.ApprovalResult> approve(@PathVariable("id") Long id,
                                                 @RequestBody @Valid AdminBusinessRequest adminBusinessRequest) {
        Business business = adminBusinessService.findById(id);

        adminBusinessService.approve(business, adminBusinessRequest);

        adminBusinessService.flush();

        return RsData.success(HttpStatus.OK, AdminBusinessResponse.ApprovalResult.from(business));
    }
}
