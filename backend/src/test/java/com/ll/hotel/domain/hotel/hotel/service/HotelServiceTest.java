package com.ll.hotel.domain.hotel.hotel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HotelServiceTest {
    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("호텔 생성")
    public void createHotel() {
        Member member = Member.builder()
                .memberEmail("member@naver.com")
                .password("123")
                .memberName("business")
                .memberPhoneNumber("010-1234-5678")
                .birthDate(LocalDate.of(2020, 2, 2))
                .role(Role.BUSINESS)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        Business business = Business.builder()
                .businessRegistrationNumber("1234567890")
                .approvalStatus(BusinessApprovalStatus.APPROVED)
                .member(member)
                .build();

        member.setBusiness(business);

        this.memberRepository.save(member);
        this.businessRepository.save(business);

        PostHotelRequest postHotelRequest = new PostHotelRequest(business.getId(), "호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, null);

        this.hotelService.create(postHotelRequest);

        Hotel hotel = this.hotelRepository.findById(1L).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        assertEquals(this.hotelRepository.count(), 1L);

        assertEquals(hotel.getHotelName(), "호텔1");
        assertEquals(hotel.getHotelEmail(), "hotel@naver.com");
        assertEquals(hotel.getHotelPhoneNumber(), "010-1234-1234");
        assertEquals(hotel.getBusiness().getId(),1L);
        assertEquals(hotel.getBusiness().getMember().getRole(), Role.BUSINESS);
        assertEquals(hotel.getBusiness().getHotel(), hotel);
    }
}