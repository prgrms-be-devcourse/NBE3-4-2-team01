package com.ll.hotel.domain.member.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.hotel.domain.member.admin.entity.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    AdminEntity findByAdminEmail(String adminEmail);
}
