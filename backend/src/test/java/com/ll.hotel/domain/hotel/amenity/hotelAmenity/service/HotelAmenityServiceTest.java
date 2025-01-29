package com.ll.hotel.domain.hotel.amenity.hotelAmenity.service;

import com.ll.hotel.domain.hotel.amenity.hotelAmenity.dto.request.HotelAmenityRequest;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.entity.HotelAmenity;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.repository.HotelAmenityRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class HotelAmenityServiceTest {
    @Autowired
    private HotelAmenityService hotelAmenityService;

    @Autowired
    private HotelAmenityRepository hotelAmenityRepository;

    @Test
    @DisplayName("호텔 어메니티 추가")
    void addHotelAmenityTest() {
        // Given
        HotelAmenityRequest.Details details = new HotelAmenityRequest.Details(
                "추가 테스트");

        // When
        HotelAmenity result = hotelAmenityService.add(details);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(details.description());

        // DB에 실제로 저장된 값 검증
        Optional<HotelAmenity> savedHotelAmenity = hotelAmenityRepository.findById(result.getId());
        assertThat(savedHotelAmenity).isPresent();
        assertThat(savedHotelAmenity.get().getDescription()).isEqualTo(details.description());
    }

    @Test
    @DisplayName("호텔 어메니티 전체 조회")
    void findAllHotelAmenitiesTest() {
        // Given
        HotelAmenityRequest.Details details1 = new HotelAmenityRequest.Details("어메니티1");
        HotelAmenityRequest.Details details2 = new HotelAmenityRequest.Details("어메니티2");

        hotelAmenityService.add(details1);
        hotelAmenityService.add(details2);

        // When
        List<HotelAmenity> result = hotelAmenityService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat((long) result.size()).isEqualTo(12);
    }

    @Test
    @DisplayName("호텔 어메니티 조회")
    void findHotelAmenityTest() {
        // Given
        HotelAmenityRequest.Details details = new HotelAmenityRequest.Details("조회 테스트");
        HotelAmenity hotelAmenity = hotelAmenityService.add(details);

        // When
        HotelAmenity result = hotelAmenityService.findById(hotelAmenity.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(details.description());
    }

    @Test
    @DisplayName("호텔 어메니티 수정")
    void modifyHotelAmenityTest() {
        // Given
        HotelAmenityRequest.Details details = new HotelAmenityRequest.Details("수정 전 테스트");
        HotelAmenity hotelAmenity = hotelAmenityService.add(details);

        HotelAmenityRequest.Details modifiedDetails = new HotelAmenityRequest.Details("수정 후 테스트");

        // When
        hotelAmenityService.modify(hotelAmenity, modifiedDetails);

        HotelAmenity result = hotelAmenityService.findById(hotelAmenity.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(modifiedDetails.description());
    }

    @Test
    @DisplayName("호텔 어메니티 삭제")
    void deleteHotelAmenityTest() {
        // Given
        HotelAmenityRequest.Details details = new HotelAmenityRequest.Details("삭제 테스트");
        HotelAmenity hotelAmenity = hotelAmenityService.add(details);

        Long hotelAmenityId = hotelAmenity.getId();

        // When
        hotelAmenityService.delete(hotelAmenity);

        // Then
        assertThatThrownBy(() -> hotelAmenityService.findById(hotelAmenityId))
                .isInstanceOf(ServiceException.class);
    }
}
