package com.ll.hotel.domain.member.member.service;

import com.ll.hotel.domain.member.member.dto.request.BusinessRequest;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.service.BusinessService;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
                    .password("dfkajl12333")
                    .memberPhoneNumber("01012345678")
                    .memberStatus(MemberStatus.ACTIVE)
                    .role(Role.BUSINESS)
                    .build()
            );

            if (i == 0) {
                testId = member.getId();
            }
        }
    }

    @Test
    @DisplayName("사업자 등록")
    public void registerBusinessTest() {
        // Given
        BusinessRequest businessRequest = new BusinessRequest("1234567890");

        Member member = memberRepository.findById(testId).get();

        // When
        Business result = businessService.register(businessRequest, member);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getApprovalStatus()).isEqualTo(BusinessApprovalStatus.PENDING);
        assertThat(result.getBusinessRegistrationNumber()).isEqualTo(businessRequest.businessRegistrationNumber());
    }
}
