package com.ll.hotel.domain.member.member.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ll.hotel.domain.member.member.dto.request.BusinessRequest;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import java.time.LocalDate;
import java.util.ArrayList;
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
public class BusinessServiceTest {
    @Autowired
    private BusinessService businessService;

    @Autowired
    private MemberRepository memberRepository;

    private Long testId;

    @BeforeEach
    void setUp() {
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
                    .oauths(new ArrayList<>())
                    .build()
            );

            if (i == 0) {
                testId = member.getId();
            }
        }
    }

    @Test
    @DisplayName("사업자 등록 - 01")
    public void registerBusinessTest() {
        // Given
        BusinessRequest.RegistrationInfo businessRequest = new BusinessRequest.RegistrationInfo(
                "1234567890",
                LocalDate.now(),
                "김사장"
        );

        Member member = memberRepository.findById(testId).get();

        // When
        Business result = businessService.register(businessRequest, member, "01");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getApprovalStatus()).isEqualTo(BusinessApprovalStatus.APPROVED);
        assertThat(result.getBusinessRegistrationNumber()).isEqualTo(businessRequest.businessRegistrationNumber());
    }
}
