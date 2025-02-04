package com.ll.hotel.domain.hotel.option.hotelOption.service;

import com.ll.hotel.domain.hotel.option.hotelOption.dto.request.HotelOptionRequest;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.repository.HotelOptionRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import org.junit.jupiter.api.BeforeEach;
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
public class HotelOptionServiceTest {
    @Autowired
    private HotelOptionService hotelOptionService;

    @Autowired
    private HotelOptionRepository hotelOptionRepository;

    private Long testId;

    @BeforeEach
    void setUp() {
        hotelOptionRepository.deleteAll();

        HotelOption hotelOption = hotelOptionRepository.save(HotelOption
                .builder()
                .name("호텔 옵션")
                .build()
        );
        testId = hotelOption.getId();
    }

    @Test
    @DisplayName("호텔 옵션 추가")
    void addHotelOptionTest() {
        // Given
        HotelOptionRequest.Details details = new HotelOptionRequest.Details(
                "추가 테스트");

        // When
        HotelOption result = hotelOptionService.add(details);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(details.name());

        // DB에 실제로 저장된 값 검증
        Optional<HotelOption> savedHotelOption = hotelOptionRepository.findById(result.getId());
        assertThat(savedHotelOption).isPresent();
        assertThat(savedHotelOption.get().getName()).isEqualTo(details.name());
    }

    @Test
    @DisplayName("호텔 옵션 전체 조회")
    void findAllHotelOptionsTest() {
        // Given
        HotelOptionRequest.Details details1 = new HotelOptionRequest.Details("추가 옵션1");
        HotelOptionRequest.Details details2 = new HotelOptionRequest.Details("추가 옵션2");

        hotelOptionService.add(details1);
        hotelOptionService.add(details2);

        // When
        List<HotelOption> result = hotelOptionService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat((long) result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("호텔 옵션 조회")
    void findHotelOptionTest() {
        // When
        HotelOption result = hotelOptionService.findById(testId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("호텔 옵션");
    }

    @Test
    @DisplayName("호텔 옵션 수정")
    void modifyHotelOptionTest() {
        // Given
        HotelOption hotelOption = hotelOptionService.findById(testId);
        HotelOptionRequest.Details modifiedDetails = new HotelOptionRequest.Details("수정 후 테스트");

        // When
        hotelOptionService.modify(hotelOption, modifiedDetails);

        HotelOption result = hotelOptionService.findById(hotelOption.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(modifiedDetails.name());
    }

    @Test
    @DisplayName("호텔 옵션 삭제")
    void deleteHotelOptionTest() {
        // Given
        HotelOption hotelOption = hotelOptionService.findById(testId);

        // When
        hotelOptionService.delete(hotelOption);

        // Then
        assertThatThrownBy(() -> hotelOptionService.findById(testId))
                .isInstanceOf(ServiceException.class);
    }
}
