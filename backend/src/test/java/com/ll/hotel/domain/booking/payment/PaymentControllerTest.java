package com.ll.hotel.domain.booking.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.hotel.domain.booking.payment.controller.PaymentController;
import com.ll.hotel.domain.booking.payment.dto.PaymentRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional*/
public class PaymentControllerTest {
/*
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("merchantUid 생성")
    void t1() throws Exception {
        // UID GET 요청
        ResultActions resultActions = mvc
                .perform(get("/api/bookings/payments/uid"))
                //.with(SecurityMockMvcRequestPostProcessors.oauth2Login())
                .andDo(print());

        // 검증
        resultActions
                .andExpect(handler().handlerType(PaymentController.class))
                .andExpect(handler().methodName("getUid"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("Uid 발급에 성공했습니다."))
                .andExpect(jsonPath("$.data.merchantUid").exists())
                .andExpect(jsonPath("$.data.apiId").exists())
                .andExpect(jsonPath("$.data.channelKey").exists());
    }

    @Test
    @DisplayName("결제 내역 저장")
    void t2() throws Exception {
        // 테스트 데이터
        ObjectMapper objectMapper = new ObjectMapper();
        Long currentTimestamp = Instant.now().getEpochSecond();
        PaymentRequest paymentRequest = new PaymentRequest(
                "TESTUID123",
                123,
                currentTimestamp
        );

        // 데이터 POST 요청
        ResultActions resultActions = mvc
                .perform(
                        post("/api/bookings/payments")
                                .content(objectMapper.writeValueAsString(paymentRequest))
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // 검증
        resultActions
                .andExpect(handler().handlerType(PaymentController.class))
                .andExpect(handler().methodName("pay"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("결제에 성공했습니다."))
                .andExpect(jsonPath("$.data.merchantUid").value("TESTUID123"))
                .andExpect(jsonPath("$.data.amount").value(123));
    }

    @Test
    @DisplayName("결제 내역 단건 조회 성공")
    void t3_1() throws Exception {
        for (int i = 1; i <= 4; i++) {
            // 결제 내역 GET 요청
            ResultActions resultActions = mvc
                    .perform(get("/api/bookings/payments/" + i))
                    .andDo(print());

            // 검증
            resultActions
                    .andExpect(handler().handlerType(PaymentController.class))
                    .andExpect(handler().methodName("getPayment"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("결제 정보 조회에 성공했습니다."))
                    .andExpect(jsonPath("$.data.paymentId").value(i))
                    .andExpect(jsonPath("$.data.merchantUid").value("BASEINIT0" + i))
                    .andExpect(jsonPath("$.data.amount").value(1000 + i));
        }
    }

    @Test
    @DisplayName("결제 내역 단건 조회 실패")
    void t3_2() throws Exception {
        // 없는 결제 내역 GET 요청
        ResultActions resultActions = mvc
                .perform(get("/api/bookings/payments/" + 5))
                .andDo(print());

        // 검증
        resultActions
                .andExpect(handler().handlerType(PaymentController.class))
                .andExpect(handler().methodName("getPayment"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg").value("결제 정보를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("결제 내역 취소 성공")
    void t4_1() throws Exception {
        for (int i = 1; i <= 4; i++) {
            // 결제 내역 취소 DELETE 요청
            ResultActions resultActions = mvc
                    .perform(delete("/api/bookings/payments/" + i))
                    .andDo(print());

            // 검증
            resultActions
                    .andExpect(handler().handlerType(PaymentController.class))
                    .andExpect(handler().methodName("cancel"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("결제 취소에 성공했습니다."))
                    .andExpect(jsonPath("$.data.paymentId").value(i))
                    .andExpect(jsonPath("$.data.merchantUid").value("BASEINIT0" + i))
                    .andExpect(jsonPath("$.data.amount").value(1000 + i))
                    .andExpect(jsonPath("$.data.paymentStatus").value("CANCELLED"));
        }
    }

    @Test
    @DisplayName("결제 내역 취소 실패")
    void t4_2() throws Exception {
        // 결제 내역 취소 DELETE 2번 요청
        mvc.perform(delete("/api/bookings/payments/1")).andDo(print());
        ResultActions resultActions = mvc
                .perform(delete("/api/bookings/payments/1"))
                .andDo(print());

        // 검증
        resultActions
                .andExpect(handler().handlerType(PaymentController.class))
                .andExpect(handler().methodName("cancel"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("이미 취소된 결제입니다."));
    }*/
}
