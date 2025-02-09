package com.ll.hotel.domain.hotel.hotel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ll.hotel.domain.hotel.hotel.dto.GetHotelDetailResponse;
import com.ll.hotel.domain.hotel.hotel.dto.GetHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelResponse;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.hotel.option.hotelOption.dto.request.HotelOptionRequest;
import com.ll.hotel.domain.hotel.option.hotelOption.dto.request.HotelOptionRequest.Details;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.service.HotelOptionService;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HotelServiceTest {
    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelOptionService hotelOptionService;

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
        Member actor = this.memberRepository.findAll().getFirst();
        Business business = this.businessRepository.findAll().getFirst();

        this.hotelOptionService.add(new Details("Parking_lot"));
        this.hotelOptionService.add(new Details("Breakfast"));
        this.hotelOptionService.add(new Details("Lunch"));

        Set<String> hotelOptions = new HashSet<>(Set.of("Parking_lot", "Breakfast", "Lunch"));

        PostHotelRequest postHotelRequest = new PostHotelRequest("호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, hotelOptions);

        PostHotelResponse postHotelResponse = this.hotelService.createHotel(actor, postHotelRequest);

        Hotel hotel = this.hotelRepository.findById(postHotelResponse.hotelId()).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        Set<String> hotelNames = hotel.getHotelOptions().stream()
                .map(HotelOption::getName)
                .collect(Collectors.toSet());

        assertEquals(this.hotelRepository.count(), 1L);

        assertEquals(hotel.getHotelName(), "호텔1");
        assertEquals(hotel.getHotelEmail(), "hotel@naver.com");
        assertEquals(hotel.getHotelPhoneNumber(), "010-1234-1234");
        assertEquals(hotel.getBusiness().getId(), business.getId());
        assertEquals(hotel.getBusiness().getMember().getRole(), Role.BUSINESS);
        assertEquals(hotel.getBusiness().getHotel(), hotel);
        assertEquals(hotel.getHotelOptions().size(), 3);
        assertTrue(hotelNames.contains("Parking_lot"));
        assertTrue(hotelNames.contains("Breakfast"));
        assertTrue(hotelNames.contains("Lunch"));
        assertFalse(hotelNames.contains("AirConditioner"));
    }

    @Test
    @DisplayName("호텔 생성 실패 - 존재하지 않는 호텔 옵션")
    public void createHotelInvalidHotelOptions() {
        Member actor = this.memberRepository.findAll().getFirst();
        Business business = businessRepository.findAll().getFirst();

        this.hotelOptionService.add(new HotelOptionRequest.Details("Parking_lot"));
        this.hotelOptionService.add(new HotelOptionRequest.Details("Breakfast"));

        Set<String> hotelOptions = new HashSet<>(Set.of("Parking_lot", "Breakfast", "Lunch"));

        PostHotelRequest postHotelRequest = new PostHotelRequest("호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, hotelOptions);

        ServiceException error = assertThrows(ServiceException.class, () -> {
            this.hotelService.createHotel(actor, postHotelRequest);
        });

        assertEquals(404, error.getRsData().getStatusCode());
        assertEquals("사용할 수 없는 호텔 옵션이 존재합니다.", error.getRsData().getMsg());
    }

    @Test
    @DisplayName("호텔 전체 목록 조회")
    public void findAllHotels() {
        Member actor = this.memberRepository.findAll().getFirst();
        Business business = this.businessRepository.findAll().getFirst();

        PostHotelRequest req1 = new PostHotelRequest("호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, null);

        PostHotelResponse res1 = this.hotelService.createHotel(actor, req1);

        Hotel hotel = this.hotelRepository.findById(res1.hotelId()).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        Member member = Member.builder()
                .memberEmail("business@naver.com")
                .memberName("b2")
                .memberPhoneNumber("010-1111-1111")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.BUSINESS)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        business = Business.builder()
                .businessRegistrationNumber("1111111111")
                .startDate(LocalDate.now().minusDays(1))
                .ownerName("Business2")
                .member(member)
                .build();

        member.setBusiness(business);

        this.memberRepository.save(member);
        this.businessRepository.save(business);

        PostHotelRequest req2 = new PostHotelRequest("호텔2", "sin@naver.com",
                "010-1111-1111", "부산시", 1111,
                5, LocalTime.of(14, 0), LocalTime.of(16, 0), "신호텔", null, null);

        PostHotelResponse res2 = this.hotelService.createHotel(member, req2);

        hotel = this.hotelRepository.findById(res2.hotelId()).get();
        business.setHotel(hotel);

        Page<GetHotelResponse> resultPage = this.hotelService.findAllHotels(1, 10, "latest", "asc", "", LocalDate.now(),
                LocalDate.now().plusDays(1), 2);
        List<GetHotelResponse> list = resultPage.getContent();
        GetHotelResponse Allres1 = list.getFirst();
        GetHotelResponse Allres2 = list.getLast();

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
    @DisplayName("호텔 전체 목록 조회 - filterDirection 값을 입력하지 않았을 경우")
    public void findAllHotelsWithoutFilterDirection() {
        Member actor = this.memberRepository.findAll().getFirst();
        Business business = this.businessRepository.findAll().getFirst();

        PostHotelRequest req1 = new PostHotelRequest("호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, null);

        PostHotelResponse res1 = this.hotelService.createHotel(actor, req1);

        Hotel hotel = this.hotelRepository.findById(res1.hotelId()).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        Member member = Member.builder()
                .memberEmail("business@naver.com")
                .memberName("b2")
                .memberPhoneNumber("010-1111-1111")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.BUSINESS)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        business = Business.builder()
                .businessRegistrationNumber("1111111111")
                .startDate(LocalDate.now().minusDays(1))
                .ownerName("Business2")
                .member(member)
                .build();

        member.setBusiness(business);

        this.memberRepository.save(member);
        this.businessRepository.save(business);

        PostHotelRequest req2 = new PostHotelRequest("호텔2", "sin@naver.com",
                "010-1111-1111", "부산시", 1111,
                5, LocalTime.of(14, 0), LocalTime.of(16, 0), "신호텔", null, null);

        PostHotelResponse res2 = this.hotelService.createHotel(member, req2);

        hotel = this.hotelRepository.findById(res2.hotelId()).get();
        business.setHotel(hotel);

        Page<GetHotelResponse> resultPage = this.hotelService.findAllHotels(1, 10, "latest", null, "", LocalDate.now(),
                LocalDate.now().plusDays(1), 2);
        List<GetHotelResponse> list = resultPage.getContent();
        GetHotelResponse Allres1 = list.getFirst();
        GetHotelResponse Allres2 = list.getLast();

        assertEquals(Allres1.hotelId(), res2.hotelId());
        assertEquals(Allres2.hotelId(), res1.hotelId());
        assertEquals(Allres1.hotelName(), "호텔2");
        assertEquals(Allres2.hotelName(), "호텔1");
        assertEquals(Allres1.streetAddress(), "부산시");
        assertEquals(Allres2.streetAddress(), "서울시");
        assertEquals(Allres1.hotelStatus(), HotelStatus.PENDING.getValue());
        assertEquals(Allres2.hotelStatus(), HotelStatus.PENDING.getValue());
    }

    @Test
    @DisplayName("호텔 전체 목록 조회 - 주소지 검색")
    public void findAllHotelsWithStreetAddress() {
        Member actor = this.memberRepository.findAll().getFirst();
        Business business = this.businessRepository.findAll().getFirst();

        PostHotelRequest req1 = new PostHotelRequest("호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, null);

        PostHotelResponse res1 = this.hotelService.createHotel(actor, req1);

        Hotel hotel = this.hotelRepository.findById(res1.hotelId()).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        Member member = Member.builder()
                .memberEmail("business@naver.com")
                .memberName("b2")
                .memberPhoneNumber("010-1111-1111")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.BUSINESS)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        business = Business.builder()
                .businessRegistrationNumber("1111111111")
                .startDate(LocalDate.now().minusDays(1))
                .ownerName("Business2")
                .member(member)
                .build();

        member.setBusiness(business);

        this.memberRepository.save(member);
        this.businessRepository.save(business);

        PostHotelRequest req2 = new PostHotelRequest("호텔2", "sin@naver.com",
                "010-1111-1111", "부산시", 1111,
                5, LocalTime.of(14, 0), LocalTime.of(16, 0), "신호텔", null, null);

        PostHotelResponse res2 = this.hotelService.createHotel(member, req2);

        hotel = this.hotelRepository.findById(res2.hotelId()).get();
        business.setHotel(hotel);

        Page<GetHotelResponse> resultPage = this.hotelService.findAllHotels(1, 10, "latest", null, "서울",
<<<<<<< HEAD
                LocalDate.now(),
                LocalDate.now().plusDays(1), 2);
=======
                LocalDate.now(), LocalDate.now().plusDays(1), 2);
>>>>>>> d18307c ([feat] 호텔 등록,FEAT] [FE] [BE] 특정 호텔 조회, 객실 등록, 객실 수정, 엔티티 수정 및 추가)
        List<GetHotelResponse> list = resultPage.getContent();
        GetHotelResponse Allres1 = list.getFirst();

        assertEquals(list.size(), 1);
        assertEquals(Allres1.hotelId(), res1.hotelId());
        assertEquals(Allres1.hotelName(), "호텔1");
        assertEquals(Allres1.streetAddress(), "서울시");
        assertEquals(Allres1.hotelStatus(), HotelStatus.PENDING.getValue());
    }

    @Test
    @DisplayName("호텔 단일 목록 조회")
    public void findHotelDetail() {
        Member actor = this.memberRepository.findAll().getFirst();
        Business business = this.businessRepository.findAll().getFirst();

        PostHotelRequest req1 = new PostHotelRequest("호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, null);

        PostHotelResponse res1 = this.hotelService.createHotel(actor, req1);
        Hotel hotel = this.hotelRepository.findById(res1.hotelId()).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        GetHotelDetailResponse detRes1 = this.hotelService.findHotelDetail(hotel.getId());

        assertEquals(res1.hotelId(), detRes1.hotelDetailDto().hotelId());
        assertEquals("호텔1", detRes1.hotelDetailDto().hotelName());
        assertEquals(LocalTime.of(12, 0), detRes1.hotelDetailDto().checkInTime());
        assertEquals(detRes1.hotelDetailDto().hotelOptions().size(), 0);

        Member member = Member.builder()
                .memberEmail("business@naver.com")
                .memberName("b2")
                .memberPhoneNumber("010-1111-1111")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.BUSINESS)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        business = Business.builder()
                .businessRegistrationNumber("1111111111")
                .startDate(LocalDate.now().minusDays(1))
                .ownerName("Business2")
                .member(member)
                .build();

        member.setBusiness(business);

        this.memberRepository.save(member);
        this.businessRepository.save(business);

        PostHotelRequest req2 = new PostHotelRequest("호텔2", "sin@naver.com",
                "010-1111-1111", "부산시", 1111,
                5, LocalTime.of(14, 0), LocalTime.of(16, 0), "신호텔", null, null);

        PostHotelResponse res2 = this.hotelService.createHotel(member, req2);

        hotel = this.hotelRepository.findById(res2.hotelId()).get();
        business.setHotel(hotel);
        this.businessRepository.save(business);

        detRes1 = this.hotelService.findHotelDetail(hotel.getId());

        assertEquals(res2.hotelId(), detRes1.hotelDetailDto().hotelId());
        assertEquals("호텔2", detRes1.hotelDetailDto().hotelName());
        assertEquals(LocalTime.of(14, 0), detRes1.hotelDetailDto().checkInTime());
        assertEquals(detRes1.hotelDetailDto().hotelOptions().size(), 0);
    }

    @Test
    @DisplayName("호텔 수정")
    public void modifyHotel() {
        Member actor = this.memberRepository.findAll().getFirst();
        Business business = businessRepository.findAll().getFirst();
        this.hotelOptionService.add(new HotelOptionRequest.Details("Parking_lot"));
        this.hotelOptionService.add(new HotelOptionRequest.Details("Breakfast"));
        this.hotelOptionService.add(new HotelOptionRequest.Details("Lunch"));
        this.hotelOptionService.add(new HotelOptionRequest.Details("Dinner"));
        Set<String> hotelOptions = new HashSet<>(Set.of("Parking_lot", "Breakfast", "Lunch"));

        PostHotelRequest postHotelRequest = new PostHotelRequest("호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, hotelOptions);

        PostHotelResponse postHotelResponse = this.hotelService.createHotel(actor, postHotelRequest);

        Hotel hotel = this.hotelRepository.findById(postHotelResponse.hotelId()).get();

        long hotelId = hotel.getId();
        business.setHotel(hotel);
        this.businessRepository.save(business);

        hotelOptions = new HashSet<>(Set.of("Parking_lot", "Dinner"));

        PutHotelRequest req1 = new PutHotelRequest("수정된 호텔1", "moHotel@naver.com", "010-1111-2222", null, 0123, null,
                null, null, null, null, null, null, hotelOptions);

        PutHotelResponse res1 = this.hotelService.modifyHotel(hotelId, actor, req1);

        hotel = this.hotelRepository.findById(res1.hotelId()).get();

        Set<String> hotelOptionNames = hotel.getHotelOptions().stream()
                .map(HotelOption::getName)
                .collect(Collectors.toSet());

        assertEquals(hotel.getId(), hotelId);
        assertEquals(hotel.getStreetAddress(), postHotelRequest.streetAddress());
        assertEquals(hotel.getZipCode(), req1.zipCode());
        assertEquals(hotel.getCheckInTime(), postHotelRequest.checkInTime());
        assertEquals(hotel.getHotelName(), req1.hotelName());
        assertEquals(hotel.getHotelEmail(), req1.hotelEmail());
        assertEquals(hotelOptionNames, hotelOptions);
    }

    @Test
    @DisplayName("호텔 수정 실패 - 존재하지 않는 호텔 옵션")
    public void modifyHotelInvalidHotelOptions() {
        Member actor = this.memberRepository.findAll().getFirst();
        Business business = businessRepository.findAll().getFirst();
        this.hotelOptionService.add(new HotelOptionRequest.Details("Parking_lot"));
        this.hotelOptionService.add(new HotelOptionRequest.Details("Breakfast"));
        this.hotelOptionService.add(new HotelOptionRequest.Details("Lunch"));
        Set<String> hotelOptions = new HashSet<>(Set.of("Parking_lot", "Breakfast", "Lunch"));

        PostHotelRequest postHotelRequest = new PostHotelRequest("호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, hotelOptions);

        PostHotelResponse postHotelResponse = this.hotelService.createHotel(actor, postHotelRequest);

        Hotel hotel = this.hotelRepository.findById(postHotelResponse.hotelId()).get();

        long hotelId = hotel.getId();
        business.setHotel(hotel);
        this.businessRepository.save(business);

        hotelOptions = new HashSet<>(Set.of("Parking_lot", "Dinner"));

        PutHotelRequest req1 = new PutHotelRequest("수정된 호텔1", "moHotel@naver.com", "010-1111-2222", null, 0123, null,
                null, null, null, null, null, null, hotelOptions);

        ServiceException error = assertThrows(ServiceException.class, () -> {
            this.hotelService.modifyHotel(hotelId, actor, req1);
        });

        assertEquals(404, error.getRsData().getStatusCode());
        assertEquals("사용할 수 없는 호텔 옵션이 존재합니다.", error.getRsData().getMsg());
    }

    @Test
    @DisplayName("호텔 삭제")
    public void deleteHotel() {
        Member actor = this.memberRepository.findAll().getFirst();
        Business business = this.businessRepository.findAll().getFirst();

        PostHotelRequest postHotelRequest = new PostHotelRequest("호텔1", "hotel@naver.com",
                "010-1234-1234", "서울시", 0123,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔입니다.", null, null);

        PostHotelResponse postHotelResponse = this.hotelService.createHotel(actor, postHotelRequest);

        Hotel hotel = this.hotelRepository.findById(postHotelResponse.hotelId()).get();

        business.setHotel(hotel);
        this.businessRepository.save(business);

        this.hotelService.deleteHotel(hotel.getId(), actor);

        assertEquals(HotelStatus.UNAVAILABLE, hotel.getHotelStatus());
    }

    public void createOthersForHotel() {
        Member member = Member.builder()
                .memberEmail("member@naver.com")
                .memberName("business")
                .memberPhoneNumber("010-1234-5678")
                .birthDate(LocalDate.of(2020, 2, 2))
                .role(Role.BUSINESS)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        Business business = Business.builder()
                .businessRegistrationNumber("1234567890")
                .startDate(LocalDate.now().minusDays(1))
                .ownerName("Business")
                .member(member)
                .build();

        member.setBusiness(business);

        this.memberRepository.save(member);
        this.businessRepository.save(business);
    }
}