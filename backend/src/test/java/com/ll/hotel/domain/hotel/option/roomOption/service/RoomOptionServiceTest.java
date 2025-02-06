package com.ll.hotel.domain.hotel.option.roomOption.service;

import com.ll.hotel.domain.hotel.option.roomOption.dto.request.RoomOptionRequest;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.roomOption.repository.RoomOptionRepository;
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
public class RoomOptionServiceTest {
    @Autowired
    private RoomOptionService roomOptionService;

    @Autowired
    private RoomOptionRepository roomOptionRepository;

    private Long testId;

    @BeforeEach
    void setUp() {
        roomOptionRepository.deleteAll();

        RoomOption roomOption = roomOptionRepository.save(RoomOption
                .builder()
                .name("객실 옵션")
                .build()
        );
        testId = roomOption.getId();
    }

    @Test
    @DisplayName("객실 옵션 추가")
    void addRoomOptionTest() {
        // Given
        RoomOptionRequest.Details details = new RoomOptionRequest.Details(
                "추가 테스트");

        // When
        RoomOption result = roomOptionService.add(details);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(details.name());

        // DB에 실제로 저장된 값 검증
        Optional<RoomOption> savedRoomOption = roomOptionRepository.findById(result.getId());
        assertThat(savedRoomOption).isPresent();
        assertThat(savedRoomOption.get().getName()).isEqualTo(details.name());
    }

    @Test
    @DisplayName("객실 옵션 전체 조회")
    void findAllRoomOptionsTest() {
        // Given
        RoomOptionRequest.Details details1 = new RoomOptionRequest.Details("추가 옵션1");
        RoomOptionRequest.Details details2 = new RoomOptionRequest.Details("추가 옵션2");

        roomOptionService.add(details1);
        roomOptionService.add(details2);

        // When
        List<RoomOption> result = roomOptionService.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat((long) result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("객실 옵션 조회")
    void findRoomOptionTest() {
        // When
        RoomOption result = roomOptionService.findById(testId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("객실 옵션");
    }

    @Test
    @DisplayName("객실 옵션 수정")
    void modifyRoomOptionTest() {
        // Given
        RoomOption roomOption = roomOptionService.findById(testId);
        RoomOptionRequest.Details modifiedDetails = new RoomOptionRequest.Details("수정 후 테스트");

        // When
        roomOptionService.modify(roomOption, modifiedDetails);

        RoomOption result = roomOptionService.findById(roomOption.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(modifiedDetails.name());
    }

    @Test
    @DisplayName("객실 옵션 삭제")
    void deleteRoomOptionTest() {
        // Given
        RoomOption roomOption = roomOptionService.findById(testId);

        // When
        roomOptionService.delete(roomOption);

        // Then
        assertThatThrownBy(() -> roomOptionService.findById(testId))
                .isInstanceOf(ServiceException.class);
    }
}
