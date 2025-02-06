package com.ll.hotel.domain.member.admin.controller;

import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class AdminBusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long testBusinessId;

    @BeforeEach
    void setUp() {
        businessRepository.deleteAll();
        memberRepository.deleteAll();

        Member member = memberRepository.save(Member
                .builder()
                .birthDate(LocalDate.now())
                .memberEmail("member@gmail.com")
                .memberName("member")
                .password("dfkajl12333")
                .memberPhoneNumber("01012345678")
                .memberStatus(MemberStatus.ACTIVE)
                .role(Role.BUSINESS)
                .build()
        );
        Business business = Business
                .builder()
                .businessRegistrationNumber(String.format("1234567890"))
                .approvalStatus(BusinessApprovalStatus.PENDING)
                .member(member)
                .hotel(null)
                .build();
        businessRepository.save(business);

        testBusinessId = business.getId();

    }

    @Test
    @DisplayName("사업자 페이지 조회")
    void findAllPagedTest1() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/admin/businesses")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdminBusinessController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("모든 사업자 정보가 조회되었습니다."));
    }

    @Test
    @DisplayName("사업자 페이지 조회 - 잘못된 페이지를 요청한 경우")
    void findAllPagedTest2() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/admin/businesses?page=20")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdminBusinessController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("사업자 조회")
    void getByIdTest1() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/admin/businesses/{id}", testBusinessId)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdminBusinessController.class))
                .andExpect(handler().methodName("getById"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("사업자 정보가 조회되었습니다."));
    }

    @Test
    @DisplayName("사업자 조회 - 요청이 잘못된 경우")
    void getByIdTest2() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        get("/api/admin/businesses/25")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdminBusinessController.class))
                .andExpect(handler().methodName("getById"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("사업자 승인")
    void approveTest() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(
                        patch("/api/admin/businesses/{id}", testBusinessId)
                                .content("""
                                        {
                                            "businessApprovalStatus": "APPROVED"
                                        }
                                        """.stripIndent())
                                .contentType(
                                        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
                                )
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdminBusinessController.class))
                .andExpect(handler().methodName("approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200"))
                .andExpect(jsonPath("$.msg").value("사업자 승인 정보가 수정되었습니다."));
    }
}
