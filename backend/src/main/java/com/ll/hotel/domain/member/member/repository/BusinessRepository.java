package com.ll.hotel.domain.member.member.repository;

import com.ll.hotel.domain.member.member.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
}
