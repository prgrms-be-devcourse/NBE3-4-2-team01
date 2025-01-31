package com.ll.hotel.domain.member.admin.service;

import com.ll.hotel.domain.member.admin.dto.AdminDTO;
import com.ll.hotel.domain.member.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminDTO createAdmin(AdminDTO adminDTO) {
        return null;
    }

    public AdminDTO loginAdmin(String adminEmail, String password) {
        return null;
    }

    public void logoutAdmin() {
    }

    public AdminDTO getAdminById(Long id) {
        return null;
    }
}
