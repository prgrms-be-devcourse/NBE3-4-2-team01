package com.ll.hotel.domain.member.admin.controller;

import com.ll.hotel.domain.member.admin.dto.AdminBusinessDto;
import com.ll.hotel.domain.member.admin.dto.request.AdminBusinessRequest;
import com.ll.hotel.domain.member.admin.service.AdminBusinessService;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminBusinessController {
    private final AdminBusinessService adminBusinessService;

    @GetMapping("/business")
    public RsData<Page<AdminBusinessDto>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<AdminBusinessDto> pagedAdminBusinessDto = adminBusinessService.findAllPagedForAdmin(page)
                .map(AdminBusinessDto::toDto);

        if (!pagedAdminBusinessDto.hasContent()) {
            throw new ServiceException("404", "요청하신 사업자 정보 페이지가 없습니다.");
        }

        return new RsData<>(
                "200",
                "모든 사업자 정보가 조회되었습니다.",
                pagedAdminBusinessDto
        );
    }

    @GetMapping("/business/{id}")
    public RsData<AdminBusinessDto> getById(@PathVariable("id") Long id) {

        Business business = adminBusinessService.findById(id);

        return new RsData<>(
                "200",
                "사업자 정보가 조회되었습니다.",
                AdminBusinessDto.toDto(business)
        );
    }

    @PatchMapping("/business/{id}")
    public RsData<AdminBusinessDto> approve(@PathVariable("id") Long id,
                                           @RequestBody @Valid AdminBusinessRequest adminBusinessRequest) {
        Business business = adminBusinessService.findById(id);

        adminBusinessService.approve(business, adminBusinessRequest);

        adminBusinessService.flush();

        return new RsData<>(
                "200",
                "사업자 승인 정보가 수정되었습니다.",
                AdminBusinessDto.toDto(business)
        );
    }
}
