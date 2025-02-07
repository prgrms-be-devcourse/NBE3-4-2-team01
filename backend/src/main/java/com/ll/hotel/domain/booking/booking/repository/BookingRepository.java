package com.ll.hotel.domain.booking.booking.repository;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.member.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByMember(Member member, PageRequest pageRequest);
    Page<Booking> findByHotelId(Long hotelId, PageRequest pageRequest);
}