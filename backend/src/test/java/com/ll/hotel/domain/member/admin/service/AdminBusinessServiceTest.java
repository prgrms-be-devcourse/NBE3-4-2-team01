package com.ll.hotel.domain.member.admin.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ll.hotel.domain.member.admin.dto.request.AdminBusinessRequest;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import java.time.LocalDate;
import java.util.Optional;
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
class AdminBusinessServiceTest {
    @Autowired
    private AdminBusinessService adminBusinessService;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long testBusinessId;

    @BeforeEach
    void setUp() {
        businessRepository.deleteAll();
        memberRepository.deleteAll();

        for (int i = 0; i < 3; i++) {
            Member member = memberRepository.save(Member
                    .builder()
                    .birthDate(LocalDate.now())
                    .memberEmail(String.format("member[%02d]", i))
                    .memberName("member")
                    .memberPhoneNumber("01012345678")
                    .memberStatus(MemberStatus.ACTIVE)
                    .role(Role.BUSINESS)
                    .build()
            );
            Business business = Business
                    .builder()
                    .businessRegistrationNumber(String.format("123456789%01d", i))
                    .startDate(LocalDate.now())
                    .ownerName("김사장")
                    .approvalStatus(BusinessApprovalStatus.PENDING)
                    .member(member)
                    .hotel(null)
                    .build();
            businessRepository.save(business);

            if (i == 0) {
                testBusinessId = business.getId();
            }
        }
    }

    @Test
    @DisplayName("사업자 페이지 조회")
    public void findAllPagedTest1() {
        // Given
        int page = 0;

        // When
        Page<Business> result = adminBusinessService.findAllPaged(page);

        System.out.println(result);
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getContent().get(0).getBusinessRegistrationNumber()).isEqualTo("1234567890");
    }

    @Test
    @DisplayName("사업자 페이지 조회 - 존재하지 않는 페이지 조회 시 예외 발생")
    void findAllPagedTest2() {
        // Given
        int page = 100;

        // When
        Page<Business> result = adminBusinessService.findAllPaged(page);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사업자 조회 - 정상적으로 조회될 때")
    void findByIdTest1() {
        // Given
        Business savedBusiness = businessRepository.findById(testBusinessId)
                .orElseThrow(() -> new ServiceException("404", "Business Not Found"));

        // When
        Business result = adminBusinessService.findById(testBusinessId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedBusiness);
    }

    @Test
    @DisplayName("사업자 조회 - 존재하지 않는 사업자 조회 시 예외 발생")
    void testFindTodoById2() {
        // When & Then
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            adminBusinessService.findById(20L);
        });

        // 예외 메시지 확인
        assertThat(exception.getMessage()).isEqualTo("404 : 존재하지 않는 사업자입니다.");
    }

    @Test
    @DisplayName("사업자 정보 수정")
    void modifyTest() {
        // Given
        AdminBusinessRequest adminBusinessRequest = new AdminBusinessRequest(
                BusinessApprovalStatus.APPROVED);

        // 기존 데이터 준비
        Business existingBusiness = adminBusinessService.findById(testBusinessId);

        // When: 승인 요청 실행
        adminBusinessService.approve(existingBusiness, adminBusinessRequest);

        // Then: 메모리 상의 데이터 검증
        assertThat(existingBusiness).isNotNull();
        assertThat(existingBusiness.getBusinessRegistrationNumber()).isEqualTo("0123456789");
        assertThat(existingBusiness.getStartDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(existingBusiness.getOwnerName()).isEqualTo("김사장");
        assertThat(existingBusiness.getApprovalStatus()).isEqualTo(BusinessApprovalStatus.APPROVED);

        // Then: DB의 데이터 검증
        Optional<Business> savedBusiness = businessRepository.findById(testBusinessId);
        assertThat(savedBusiness).isPresent();
        assertThat(savedBusiness.get().getBusinessRegistrationNumber()).isEqualTo("0123456789"); // DB에 반영된 값 확인
        assertThat(savedBusiness.get().getStartDate()).isEqualTo(LocalDate.of(2020, 1, 1));
        assertThat(savedBusiness.get().getOwnerName()).isEqualTo("김사장");
        assertThat(savedBusiness.get().getApprovalStatus()).isEqualTo(
                BusinessApprovalStatus.APPROVED); // DB에 반영된 승인 상태 확인
    }
}
