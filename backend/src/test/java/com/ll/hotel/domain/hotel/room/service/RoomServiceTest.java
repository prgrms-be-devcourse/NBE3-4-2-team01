package com.ll.hotel.domain.hotel.room.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.hotel.service.HotelService;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.room.dto.GetAllRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.GetRoomOptionResponse;
import com.ll.hotel.domain.hotel.room.dto.GetRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.PostRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PutRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PutRoomResponse;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import com.ll.hotel.domain.hotel.room.type.RoomStatus;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
class RoomServiceTest {
    @Autowired
    private HotelService hotelService;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @BeforeEach
    public void beforeEach() {
        this.createHotel();
    }

    @Test
    @DisplayName("객실 생성")
    public void createRoom() {
        Hotel hotel = this.hotelRepository.findAll().getFirst();
        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, null);

        this.roomService.create(hotel.getId(), req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        assertEquals(room.getRoomName(), "객실1");
        assertEquals(room.getHotel().getId(), hotel.getId());
        assertEquals(room.getBasePrice(), 300000);
        assertEquals(room.getBedTypeNumber().bed_single(), 4);
        assertEquals(room.getBedTypeNumber().bed_double(), 2);
        assertEquals(room.getBedTypeNumber().bed_king(), 1);
        assertEquals(room.getBedTypeNumber().bed_triple(), 0);
        assertEquals(room.getStandardNumber(), 2);

        bedTypeNumber = Map.of("DOUBLE", 4, "QUEEN", 1);
        PostRoomRequest req2 = new PostRoomRequest("객실2", 2, 500000, 3, 4, bedTypeNumber, null, null);

        this.roomService.create(hotel.getId(), req2);

        room = this.roomRepository.findById(roomId + 1).get();

        assertEquals(room.getRoomName(), "객실2");
        assertEquals(room.getHotel().getId(), hotel.getId());
        assertEquals(room.getBasePrice(), 500000);
        assertEquals(room.getBedTypeNumber().bed_double(), 4);
        assertEquals(room.getBedTypeNumber().bed_queen(), 1);
        assertEquals(room.getBedTypeNumber().bed_triple(), 0);
        assertEquals(room.getStandardNumber(), 3);
    }

    @Test
    @DisplayName("객실 전체 조회")
    public void findAllRooms() {
        Hotel hotel = this.hotelRepository.findAll().getFirst();
        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, null);

        this.roomService.create(hotel.getId(), req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        bedTypeNumber = Map.of("DOUBLE", 4, "QUEEN", 1);
        PostRoomRequest req2 = new PostRoomRequest("객실2", 2, 500000, 3, 4, bedTypeNumber, null, null);

        this.roomService.create(hotel.getId(), req2);

        List<GetAllRoomResponse> rooms = this.roomService.findAllRooms(hotel.getId());
        GetAllRoomResponse res = rooms.get(0);

        assertEquals(res.roomId(), roomId);
        assertEquals(res.roomName(), "객실1");
        assertEquals(res.basePrice(), 300000);
        assertEquals(res.bedTypeNumber().bed_single(), 4);
        assertEquals(res.bedTypeNumber().bed_double(), 2);
        assertEquals(res.bedTypeNumber().bed_king(), 1);
        assertEquals(res.bedTypeNumber().bed_triple(), 0);
        assertEquals(res.standardNumber(), 2);

        res = rooms.get(1);

        assertEquals(res.roomId(), roomId + 1);
        assertEquals(res.roomName(), "객실2");
        assertEquals(res.basePrice(), 500000);
        assertEquals(res.bedTypeNumber().bed_double(), 4);
        assertEquals(res.bedTypeNumber().bed_queen(), 1);
        assertEquals(res.bedTypeNumber().bed_triple(), 0);
        assertEquals(res.standardNumber(), 3);
    }

    @Test
    @DisplayName("특정 객실 조회")
    public void findRoom() {
        Hotel hotel = this.hotelRepository.findAll().getFirst();
        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, null);

        this.roomService.create(hotel.getId(), req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        bedTypeNumber = Map.of("DOUBLE", 4, "QUEEN", 1);
        PostRoomRequest req2 = new PostRoomRequest("객실2", 2, 500000, 3, 4, bedTypeNumber, null, null);

        this.roomService.create(hotel.getId(), req2);

        GetRoomResponse res = this.roomService.findRoomDetail(hotel.getId(), roomId);

        assertEquals(res.id(), roomId);
        assertEquals(res.hotelId(), hotel.getId());
        assertEquals(res.roomName(), "객실1");
        assertEquals(res.roomNumber(), req1.roomNumber());
        assertEquals(res.basePrice(), req1.basePrice());
        assertEquals(res.bedTypeNumber().bed_single(), req1.bedTypeNumber().get("SINGLE"));
        assertEquals(res.bedTypeNumber().bed_double(), req1.bedTypeNumber().get("DOUBLE"));
        assertEquals(res.bedTypeNumber().bed_king(), req1.bedTypeNumber().get("KING"));
        assertEquals(res.bedTypeNumber().bed_triple(), 0);
        assertEquals(res.roomStatus(), RoomStatus.AVAILABLE.getValue());
        assertEquals(res.roomImages().size(), 0);
        assertEquals(res.roomOptions().size(), 0);
        assertEquals(res.standardNumber(), 2);

        roomId += 1;
        res = this.roomService.findRoomDetail(hotel.getId(), roomId);

        assertEquals(res.id(), roomId);
        assertEquals(res.hotelId(), hotel.getId());
        assertEquals(res.roomName(), "객실2");
        assertEquals(res.roomNumber(), req2.roomNumber());
        assertEquals(res.basePrice(), req2.basePrice());
        assertEquals(res.bedTypeNumber().bed_double(), req2.bedTypeNumber().get("DOUBLE"));
        assertEquals(res.bedTypeNumber().bed_queen(), req2.bedTypeNumber().get("QUEEN"));
        assertEquals(res.bedTypeNumber().bed_triple(), 0);
        assertEquals(res.roomStatus(), RoomStatus.AVAILABLE.getValue());
        assertEquals(res.roomImages().size(), 0);
        assertEquals(res.roomOptions().size(), 0);
        assertEquals(res.standardNumber(), 3);
    }

    @Test
    @DisplayName("객실 옵션 조회")
    public void findRoomOptions() {
        Hotel hotel = this.hotelRepository.findAll().getFirst();
        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);
        Set<String> roomOptions = new HashSet<>(Set.of("ShowerRoom", "Computer", "TV"));

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);

        this.roomService.create(hotel.getId(), req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        GetRoomOptionResponse res = this.roomService.findRoomOptions(hotel.getId(), roomId);

        assertEquals(res.roomId(), roomId);
        assertEquals(res.roomOptions().size(), 3);
        assertTrue(res.roomOptions().contains("ShowerRoom"));
        assertTrue(res.roomOptions().contains("Computer"));
        assertTrue(res.roomOptions().contains("TV"));
        assertFalse(res.roomOptions().contains("AirConditioner"));
    }

    @Test
    @DisplayName("객실 수정")
    public void modifyRoom() {
        Hotel hotel = this.hotelRepository.findAll().getFirst();
        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);
        Set<String> roomOptions = new HashSet<>(Set.of("ShowerRoom", "Computer", "TV"));

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);

        this.roomService.create(hotel.getId(), req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        roomOptions = new HashSet<>(Set.of("TV", "AirConditioner"));
        PutRoomRequest putReq1 = new PutRoomRequest("수정 객실1", 5, null, null, 5, null,
                "in_booking", null, roomOptions);

        PutRoomResponse res1 = this.roomService.modify(hotel.getId(), roomId, putReq1);

        assertEquals(res1.hotelId(), hotel.getId());
        assertEquals(res1.roomId(), room.getId());
        assertEquals(res1.roomName(), room.getRoomName());
        assertEquals(res1.roomStatus(), RoomStatus.IN_BOOKING.getValue());

        room = this.roomRepository.findById(roomId).get();

        Set<String> roomNames = room.getRoomOptions().stream()
                .map(RoomOption::getName)
                .collect(Collectors.toSet());

        assertEquals(room.getRoomName(), putReq1.roomName());
        assertEquals(room.getRoomNumber(), putReq1.roomNumber());
        assertEquals(room.getBasePrice(), req1.basePrice());
        assertEquals(room.getStandardNumber(), req1.standardNumber());
        assertEquals(room.getMaxNumber(), putReq1.maxNumber());
        assertEquals(room.getBedTypeNumber(), BedTypeNumber.fromJson(req1.bedTypeNumber()));
        assertEquals(room.getRoomStatus(), RoomStatus.IN_BOOKING);
        assertEquals(room.getHotel().getId(), hotel.getId());
        assertEquals(room.getRoomImages().size(), 0);
        assertEquals(roomNames, roomOptions);
    }

    @Test
    @DisplayName("객실 삭제")
    public void deleteRoom() {
        Hotel hotel = this.hotelRepository.findAll().getFirst();
        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);
        Set<String> roomOptions = new HashSet<>(Set.of("ShowerRoom", "Computer", "TV"));

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);

        this.roomService.create(hotel.getId(), req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        this.roomService.delete(hotel.getId(), roomId);

        assertEquals(RoomStatus.UNAVAILABLE, room.getRoomStatus());
    }

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

        Hotel hotel = this.hotelRepository.findAll().getFirst();

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
}