package com.ll.hotel.domain.member.admin.service;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.member.admin.dto.request.AdminHotelRequest;
import com.ll.hotel.global.exceptions.ServiceException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AdminHotelServiceTest {
    @Autowired
    private AdminHotelService adminHotelService;

    @Autowired
    private HotelRepository hotelRepository;

    private Long testHotelId;

    @BeforeEach
    void setUp() {
        hotelRepository.deleteAll();

        for (int i = 0; i < 3; i++) {
            Hotel hotel = Hotel
                    .builder()
                    .hotelName(String.format("호텔[%02d]", i))
                    .hotelEmail(String.format("hotel[%02d]@gmail.com", i))
                    .hotelGrade(5)
                    .hotelStatus(HotelStatus.PENDING)
                    .checkInTime(LocalTime.of(15, 0))
                    .checkOutTime(LocalTime.of(11, 0))
                    .hotelPhoneNumber("01012345678")
                    .averageRating(5.6)
                    .totalReviewCount(3L)
                    .totalReviewRatingSum(5L)
                    .build();
            hotelRepository.save(hotel);

            if (i == 0) {
                testHotelId = hotel.getId();
            }
        }
    }

    @Test
    @DisplayName("호텔 페이지 조회")
    public void findAllPagedTest1() {
        // Given
        int page = 0;

        // When
        Page<Hotel> result = adminHotelService.findAllPaged(page);

        System.out.println(result);
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getContent().get(0).getHotelGrade()).isEqualTo(5);
        assertThat(result.getContent().get(0).getHotelStatus()).isEqualTo(HotelStatus.PENDING);
    }

    @Test
    @DisplayName("호텔 페이지 조회 - 존재하지 않는 페이지 조회 시 예외 발생")
    void findAllPagedTest2() {
        // Given
        int page = 100;

        // When
        Page<Hotel> result = adminHotelService.findAllPaged(page);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("호텔 조회")
    void findByIdTest1() {
        // Given
        Hotel savedHotel = hotelRepository.findById(testHotelId)
                .orElseThrow(() -> new ServiceException("404", "Hotel Not Found"));

        // When
        Hotel result = adminHotelService.findById(testHotelId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedHotel);
    }

    @Test
    @DisplayName("호텔 조회 - 존재하지 않는 호텔 조회 시 예외 발생")
    void testFindTodoById2() {
        // When & Then
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            adminHotelService.findById(20L);
        });

        // 예외 메시지 확인
        assertThat(exception.getMessage()).isEqualTo("404 : 존재하지 않는 호텔입니다.");
    }

    @Test
    @DisplayName("승인 정보 수정")
    void approveTest() {
        // Given
        AdminHotelRequest adminHotelRequest = new AdminHotelRequest(
                HotelStatus.AVAILABLE
        );

        // 기존 데이터 준비
        Hotel existingHotel = adminHotelService.findById(testHotelId);

        // When: 승인 요청 실행
        adminHotelService.approve(existingHotel, adminHotelRequest);

        // Then: 메모리 상의 데이터 검증
        assertThat(existingHotel).isNotNull();
        assertThat(existingHotel.getHotelStatus()).isEqualTo(HotelStatus.AVAILABLE);

        // Then: DB의 데이터 검증
        Optional<Hotel> savedHotel = hotelRepository.findById(existingHotel.getId());
        assertThat(savedHotel).isPresent();
        assertThat(savedHotel.get().getHotelStatus()).isEqualTo(HotelStatus.AVAILABLE);
    }
}
