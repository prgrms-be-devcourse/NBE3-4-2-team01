package com.ll.hotel.domain.member.admin.repository;

import com.ll.hotel.domain.member.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByAdminEmail(String adminEmail);
}
