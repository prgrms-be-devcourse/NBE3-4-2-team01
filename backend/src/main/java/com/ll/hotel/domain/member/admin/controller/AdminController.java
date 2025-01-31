package com.ll.hotel.domain.member.admin.controller;

import com.ll.hotel.domain.member.admin.dto.AdminRequest;
import com.ll.hotel.domain.member.admin.dto.AdminResponse;
import com.ll.hotel.domain.member.admin.service.AdminService;
import com.ll.hotel.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    
    @PostMapping("/join")
    public ResponseEntity<RsData<AdminResponse>> createAdmin(@RequestBody AdminRequest adminReqBody) {

        return ResponseEntity.ok(new RsData<>(
            "200-1",
            "관리자 계정이 생성되었습니다.",
            new AdminResponse(null, "관리자 계정이 생성되었습니다.")
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<RsData<AdminResponse>> loginAdmin(@RequestBody AdminRequest adminReqBody) {

        return ResponseEntity.ok(new RsData<>(
            "200-1", 
            "로그인 성공", 
            new AdminResponse(null, "로그인 성공")
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<RsData<String>> logoutAdmin() {

        return ResponseEntity.ok(new RsData<>(
            "200-1", 
            "로그아웃 성공", 
            null
        ));
    }

    @GetMapping("/{id}") // 관리자는 조회가 필요할까?
    public ResponseEntity<RsData<AdminResponse>> getAdmin(@PathVariable Long id) {

        return ResponseEntity.ok(new RsData<>(
            "200-1", 
            "관리자 정보를 조회했습니다.", 
            new AdminResponse(null, "관리자 정보를 조회했습니다.")
        ));
    }
}
