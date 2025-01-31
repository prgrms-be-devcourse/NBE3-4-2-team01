package com.ll.hotel.domain.member.member.repository;

import com.ll.hotel.domain.member.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByEmail(String email);
}
