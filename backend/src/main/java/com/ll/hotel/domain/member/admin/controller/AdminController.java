package com.ll.hotel.domain.member.admin.controller;

import com.ll.hotel.domain.member.admin.dto.AdminReqBody;
import com.ll.hotel.domain.member.admin.dto.AdminResBody;
import com.ll.hotel.global.rsData.RsData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @PostMapping("/join")
    public ResponseEntity<RsData<AdminResBody>> createAdmin(@RequestBody AdminReqBody adminReqBody) {

        return ResponseEntity.ok(new RsData<>(
            "200-1", 
            "관리자 계정이 생성되었습니다.", 
            new AdminResBody(null, "관리자 계정이 생성되었습니다.")
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<RsData<AdminResBody>> loginAdmin(@RequestBody AdminReqBody adminReqBody) {

        return ResponseEntity.ok(new RsData<>(
            "200-1", 
            "로그인 성공", 
            new AdminResBody(null, "로그인 성공")
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

    @GetMapping("/{id}") // 관리자는 조회가 필요할까? 일단 스켈레톤 코드로 작성
    public ResponseEntity<RsData<AdminResBody>> getAdmin(@PathVariable Long id) {

        return ResponseEntity.ok(new RsData<>(
            "200-1", 
            "관리자 정보를 조회했습니다.", 
            new AdminResBody(null, "관리자 정보를 조회했습니다.")
        ));
    }
}
