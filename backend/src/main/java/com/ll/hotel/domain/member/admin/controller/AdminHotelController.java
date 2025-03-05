package com.ll.hotel.domain.member.admin.controller;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.member.admin.dto.request.AdminHotelRequest;
import com.ll.hotel.domain.member.admin.dto.response.AdminHotelResponse;
import com.ll.hotel.domain.member.admin.service.AdminHotelService;
import com.ll.hotel.global.exceptions.ServiceException;
import com.ll.hotel.global.rsData.RsData;
import com.ll.hotel.standard.page.dto.PageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/hotels")
@RequiredArgsConstructor
public class AdminHotelController {
    private final AdminHotelService adminHotelService;

    @GetMapping
    public RsData<PageDto<AdminHotelResponse.Summary>> getAll(

            @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<AdminHotelResponse.Summary> pagedHotelSummaries = adminHotelService.findAllPaged(page)
                .map(AdminHotelResponse.Summary::from);

        if (!pagedHotelSummaries.hasContent()) {
            throw new ServiceException("404", "요청하신 호텔 정보 페이지가 없습니다.");
        }

        return RsData.success(HttpStatus.OK, new PageDto<>(pagedHotelSummaries));
    }

    @GetMapping("/{id}")
    public RsData<AdminHotelResponse.Detail> getById(@PathVariable("id") Long id) {

        Hotel hotel = adminHotelService.findById(id);

        return RsData.success(HttpStatus.OK, AdminHotelResponse.Detail.from(hotel));
    }

    @PatchMapping("/{id}")
    public RsData<AdminHotelResponse.ApprovalResult> approve(@PathVariable("id") Long id,
                                              @RequestBody @Valid AdminHotelRequest adminHotelRequest) {
        Hotel hotel = adminHotelService.findById(id);

        adminHotelService.approve(hotel, adminHotelRequest);

        adminHotelService.flush();

        return RsData.success(HttpStatus.OK, AdminHotelResponse.ApprovalResult.from(hotel));
    }
}
