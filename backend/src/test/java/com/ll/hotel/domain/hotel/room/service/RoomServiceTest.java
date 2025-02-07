package com.ll.hotel.domain.hotel.room.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.hotel.service.HotelService;
import com.ll.hotel.domain.hotel.option.roomOption.dto.request.RoomOptionRequest;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.roomOption.service.RoomOptionService;
import com.ll.hotel.domain.hotel.room.dto.GetRoomDetailResponse;
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
import com.ll.hotel.global.exceptions.ServiceException;
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
    private RoomService roomService;

    @Autowired
    private RoomOptionService roomOptionService;

    @Autowired
    private HotelRepository hotelRepository;

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
        Member actor = this.memberRepository.findAll().getFirst();
        Hotel hotel = this.hotelRepository.findAll().getFirst();

        this.roomOptionService.add(new RoomOptionRequest.Details("TV"));
        this.roomOptionService.add(new RoomOptionRequest.Details("AirConditioner"));

        Set<String> roomOptions = new HashSet<>(Set.of("TV", "AirConditioner"));

        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);

        this.roomService.createRoom(hotel.getId(), actor, req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        Set<String> roomOption = room.getRoomOptions().stream().map(RoomOption::getName).collect(Collectors.toSet());

        assertEquals(room.getRoomName(), "객실1");
        assertEquals(room.getHotel().getId(), hotel.getId());
        assertEquals(room.getBasePrice(), 300000);
        assertEquals(room.getBedTypeNumber().bed_single(), 4);
        assertEquals(room.getBedTypeNumber().bed_double(), 2);
        assertEquals(room.getBedTypeNumber().bed_king(), 1);
        assertEquals(room.getBedTypeNumber().bed_triple(), 0);
        assertEquals(room.getStandardNumber(), 2);
        assertEquals(roomOption.size(), 2);
        assertTrue(roomOption.contains("TV"));
        assertTrue(roomOption.contains("AirConditioner"));

        bedTypeNumber = Map.of("DOUBLE", 4, "QUEEN", 1);
        PostRoomRequest req2 = new PostRoomRequest("객실2", 2, 500000, 3, 4, bedTypeNumber, null, null);

        this.roomService.createRoom(hotel.getId(), actor, req2);

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
    @DisplayName("객실 생성 실패 - 존재하지 않는 객실 옵션")
    public void createRoomInvalidRoomOptions() {
        Member actor = this.memberRepository.findAll().getFirst();
        Hotel hotel = this.hotelRepository.findAll().getFirst();

        Set<String> roomOptions = new HashSet<>(Set.of("TV", "AirConditioner"));

        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);

        ServiceException error = assertThrows(ServiceException.class, () -> {
            this.roomService.createRoom(hotel.getId(), actor, req1);
        });

        assertEquals(404, error.getRsData().getStatusCode());
        assertEquals("사용할 수 없는 객실 옵션이 존재합니다.", error.getRsData().getMsg());
    }

    @Test
    @DisplayName("객실 전체 조회")
    public void findAllRooms() {
        Member actor = this.memberRepository.findAll().getFirst();
        Hotel hotel = this.hotelRepository.findAll().getFirst();
        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, null);

        this.roomService.createRoom(hotel.getId(), actor, req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        bedTypeNumber = Map.of("DOUBLE", 4, "QUEEN", 1);
        PostRoomRequest req2 = new PostRoomRequest("객실2", 2, 500000, 3, 4, bedTypeNumber, null, null);

        this.roomService.createRoom(hotel.getId(), actor, req2);

        List<GetRoomResponse> rooms = this.roomService.findAllRooms(hotel.getId());
        GetRoomResponse res = rooms.getFirst();

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
        Member actor = this.memberRepository.findAll().getFirst();
        Hotel hotel = this.hotelRepository.findAll().getFirst();
        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, null);

        this.roomService.createRoom(hotel.getId(), actor, req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        bedTypeNumber = Map.of("DOUBLE", 4, "QUEEN", 1);
        PostRoomRequest req2 = new PostRoomRequest("객실2", 2, 500000, 3, 4, bedTypeNumber, null, null);

        this.roomService.createRoom(hotel.getId(), actor, req2);

        GetRoomDetailResponse detRes1 = this.roomService.findRoomDetail(hotel.getId(), roomId);

        assertEquals(detRes1.roomDto().id(), roomId);
        assertEquals(detRes1.roomDto().hotelId(), hotel.getId());
        assertEquals(detRes1.roomDto().roomName(), "객실1");
        assertEquals(detRes1.roomDto().roomNumber(), req1.roomNumber());
        assertEquals(detRes1.roomDto().basePrice(), req1.basePrice());
        assertEquals(detRes1.roomDto().bedTypeNumber().bed_single(), req1.bedTypeNumber().get("SINGLE"));
        assertEquals(detRes1.roomDto().bedTypeNumber().bed_double(), req1.bedTypeNumber().get("DOUBLE"));
        assertEquals(detRes1.roomDto().bedTypeNumber().bed_king(), req1.bedTypeNumber().get("KING"));
        assertEquals(detRes1.roomDto().bedTypeNumber().bed_triple(), 0);
        assertEquals(detRes1.roomDto().roomStatus(), RoomStatus.AVAILABLE.getValue());
        assertEquals(detRes1.roomImageUrls().size(), 0);
        assertEquals(detRes1.roomDto().roomOptions().size(), 0);
        assertEquals(detRes1.roomDto().standardNumber(), 2);

        roomId += 1;
        detRes1 = this.roomService.findRoomDetail(hotel.getId(), roomId);

        assertEquals(detRes1.roomDto().id(), roomId);
        assertEquals(detRes1.roomDto().hotelId(), hotel.getId());
        assertEquals(detRes1.roomDto().roomName(), "객실2");
        assertEquals(detRes1.roomDto().roomNumber(), req2.roomNumber());
        assertEquals(detRes1.roomDto().basePrice(), req2.basePrice());
        assertEquals(detRes1.roomDto().bedTypeNumber().bed_double(), req2.bedTypeNumber().get("DOUBLE"));
        assertEquals(detRes1.roomDto().bedTypeNumber().bed_queen(), req2.bedTypeNumber().get("QUEEN"));
        assertEquals(detRes1.roomDto().bedTypeNumber().bed_triple(), 0);
        assertEquals(detRes1.roomDto().roomStatus(), RoomStatus.AVAILABLE.getValue());
        assertEquals(detRes1.roomImageUrls().size(), 0);
        assertEquals(detRes1.roomDto().roomOptions().size(), 0);
        assertEquals(detRes1.roomDto().standardNumber(), 3);
    }

    @Test
    @DisplayName("객실 옵션 조회")
    public void findRoomOptions() {
        Member actor = this.memberRepository.findAll().getFirst();
        Hotel hotel = this.hotelRepository.findAll().getFirst();

        this.roomOptionService.add(new RoomOptionRequest.Details("TV"));
        this.roomOptionService.add(new RoomOptionRequest.Details("Computer"));
        this.roomOptionService.add(new RoomOptionRequest.Details("ShowerRoom"));

        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);
        Set<String> roomOptions = new HashSet<>(Set.of("ShowerRoom", "Computer", "TV"));

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);

        this.roomService.createRoom(hotel.getId(), actor, req1);

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
        Member actor = this.memberRepository.findAll().getFirst();
        Hotel hotel = this.hotelRepository.findAll().getFirst();

        this.roomOptionService.add(new RoomOptionRequest.Details("TV"));
        this.roomOptionService.add(new RoomOptionRequest.Details("Computer"));
        this.roomOptionService.add(new RoomOptionRequest.Details("ShowerRoom"));
        this.roomOptionService.add(new RoomOptionRequest.Details("AirConditioner"));

        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);
        Set<String> roomOptions = new HashSet<>(Set.of("ShowerRoom", "Computer", "TV"));

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);

        this.roomService.createRoom(hotel.getId(), actor, req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        roomOptions = new HashSet<>(Set.of("TV", "AirConditioner"));
        PutRoomRequest putReq1 = new PutRoomRequest("수정 객실1", 5, null, null, 5, null,
                "in_booking", null, null, roomOptions);

        PutRoomResponse res1 = this.roomService.modifyRoom(hotel.getId(), roomId, actor, putReq1);

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
        assertEquals(roomNames, roomOptions);
    }

    @Test
    @DisplayName("객실 수정 실패 - 존재하지 않는 객실 옵션")
    public void modifyRoomInvalidRoomOption() {
        Member actor = this.memberRepository.findAll().getFirst();
        Hotel hotel = this.hotelRepository.findAll().getFirst();

        this.roomOptionService.add(new RoomOptionRequest.Details("TV"));
        this.roomOptionService.add(new RoomOptionRequest.Details("Computer"));
        this.roomOptionService.add(new RoomOptionRequest.Details("ShowerRoom"));

        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);
        Set<String> roomOptions = new HashSet<>(Set.of("ShowerRoom", "Computer", "TV"));

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);

        this.roomService.createRoom(hotel.getId(), actor, req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        roomOptions = new HashSet<>(Set.of("TV", "AirConditioner"));
        PutRoomRequest putReq1 = new PutRoomRequest("수정 객실1", 5, null, null, 5, null,
                "in_booking", null, null, roomOptions);

        ServiceException error = assertThrows(ServiceException.class, () -> {
            this.roomService.modifyRoom(hotel.getId(), roomId, actor, putReq1);
        });

        assertEquals(404, error.getRsData().getStatusCode());
        assertEquals("사용할 수 없는 객실 옵션이 존재합니다.", error.getRsData().getMsg());
    }

    @Test
    @DisplayName("객실 삭제")
    public void deleteRoom() {
        Member actor = this.memberRepository.findAll().getFirst();
        Hotel hotel = this.hotelRepository.findAll().getFirst();

        this.roomOptionService.add(new RoomOptionRequest.Details("TV"));
        this.roomOptionService.add(new RoomOptionRequest.Details("Computer"));
        this.roomOptionService.add(new RoomOptionRequest.Details("ShowerRoom"));

        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);
        Set<String> roomOptions = new HashSet<>(Set.of("ShowerRoom", "Computer", "TV"));

        PostRoomRequest req1 = new PostRoomRequest("객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);

        this.roomService.createRoom(hotel.getId(), actor, req1);

        Room room = this.roomRepository.findAll().getFirst();
        Long roomId = room.getId();

        this.roomService.deleteRoom(hotel.getId(), roomId, actor);

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

        this.hotelService.createHotel(member, postHotelRequest);

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