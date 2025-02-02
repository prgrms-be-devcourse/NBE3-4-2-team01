package com.ll.hotel.domain.hotel.hotel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ll.hotel.domain.hotel.hotel.dto.GetAllHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void beforeEach() {
        this.createOthersForHotel();
    }

    @Test
    @DisplayName("호텔 생성")
    public void createHotel() {
        Business business = this.businessRepository.findAll().getFirst();

        PostHotelRequest postHotelRequest = new PostHotelRequest(business.getId(), "호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, null);

        PostHotelResponse postHotelResponse = this.hotelService.create(postHotelRequest);

        Hotel hotel = this.hotelRepository.findById(postHotelResponse.hotelId()).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        assertEquals(this.hotelRepository.count(), 1L);

        assertEquals(hotel.getHotelName(), "호텔1");
        assertEquals(hotel.getHotelEmail(), "hotel@naver.com");
        assertEquals(hotel.getHotelPhoneNumber(), "010-1234-1234");
        assertEquals(hotel.getBusiness().getId(), business.getId());
        assertEquals(hotel.getBusiness().getMember().getRole(), Role.BUSINESS);
        assertEquals(hotel.getBusiness().getHotel(), hotel);
    }

    @Test
    @DisplayName("호텔 전체 목록 조회")
    public void findAllHotels() {
        Business business = this.businessRepository.findAll().getFirst();

        PostHotelRequest req1 = new PostHotelRequest(business.getId(), "호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, null);

        PostHotelResponse res1 = this.hotelService.create(req1);

        Hotel hotel = this.hotelRepository.findById(res1.hotelId()).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        Member member = Member.builder()
                .memberEmail("business@naver.com")
                .password("456")
                .memberName("b2")
                .memberPhoneNumber("010-1111-1111")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.BUSINESS)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        business = Business.builder()
                .businessRegistrationNumber("1111111111")
                .approvalStatus(BusinessApprovalStatus.APPROVED)
                .member(member)
                .build();

        member.setBusiness(business);

        this.memberRepository.save(member);
        this.businessRepository.save(business);

        PostHotelRequest req2 = new PostHotelRequest(business.getId(), "호텔2", "sin@naver.com",
                "010-1111-1111", "부산시", 1111,
                5, LocalTime.of(14, 0), LocalTime.of(16, 0), "신호텔", null, null);

        PostHotelResponse res2 = this.hotelService.create(req2);

        hotel = this.hotelRepository.findById(res2.hotelId()).get();
        business.setHotel(hotel);

        List<GetAllHotelResponse> list = this.hotelService.findAll();
        GetAllHotelResponse Allres1 = list.getFirst();
        GetAllHotelResponse Allres2 = list.getLast();

        assertEquals(Allres1.hotelId(), res1.hotelId());
        assertEquals(Allres2.hotelId(), res2.hotelId());
        assertEquals(Allres1.hotelName(), "호텔1");
        assertEquals(Allres2.hotelName(), "호텔2");
        assertEquals(Allres1.streetAddress(), "서울시");
        assertEquals(Allres2.streetAddress(), "부산시");
        assertEquals(Allres1.hotelStatus(), HotelStatus.PENDING.getValue());
        assertEquals(Allres2.hotelStatus(), HotelStatus.PENDING.getValue());
    }

    @Test
    @DisplayName("호텔 단일 목록 조회")
    public void findHotelDetail() {
        Business business = this.businessRepository.findAll().getFirst();

        PostHotelRequest req1 = new PostHotelRequest(business.getId(), "호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, null);

        PostHotelResponse res1 = this.hotelService.create(req1);
        Hotel hotel = this.hotelRepository.findById(res1.hotelId()).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        HotelDto dto = this.hotelService.findHotelDetail(hotel.getId());

        assertEquals(res1.hotelId(), dto.hotelId());
        assertEquals("호텔1", dto.hotelName());
        assertEquals(LocalTime.of(12, 0), dto.checkInTime());
        assertEquals(dto.hotelImages().size(), 0);
        assertEquals(dto.hotelOptions().size(), 0);

        Member member = Member.builder()
                .memberEmail("business@naver.com")
                .password("456")
                .memberName("b2")
                .memberPhoneNumber("010-1111-1111")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.BUSINESS)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        business = Business.builder()
                .businessRegistrationNumber("1111111111")
                .approvalStatus(BusinessApprovalStatus.APPROVED)
                .member(member)
                .build();

        member.setBusiness(business);

        this.memberRepository.save(member);
        this.businessRepository.save(business);

        PostHotelRequest req2 = new PostHotelRequest(business.getId(), "호텔2", "sin@naver.com",
                "010-1111-1111", "부산시", 1111,
                5, LocalTime.of(14, 0), LocalTime.of(16, 0), "신호텔", null, null);

        PostHotelResponse res2 = this.hotelService.create(req2);

        hotel = this.hotelRepository.findById(res2.hotelId()).get();
        business.setHotel(hotel);
        this.businessRepository.save(business);

        dto = this.hotelService.findHotelDetail(hotel.getId());

        assertEquals(res2.hotelId(), dto.hotelId());
        assertEquals("호텔2", dto.hotelName());
        assertEquals(LocalTime.of(14, 0), dto.checkInTime());
        assertEquals(dto.hotelImages().size(), 0);
        assertEquals(dto.hotelOptions().size(), 0);
    }

    public void createOthersForHotel() {
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
    }
}