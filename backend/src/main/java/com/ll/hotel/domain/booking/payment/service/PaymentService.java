package com.ll.hotel.domain.booking.payment.service;

import com.ll.hotel.domain.booking.payment.dto.PaymentRequest;
import com.ll.hotel.domain.booking.payment.dto.TokenRequest;
import com.ll.hotel.domain.booking.payment.dto.TokenResponse;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.repository.PaymentRepository;
import com.ll.hotel.domain.booking.payment.type.PaymentStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    @Value("${spring.portone.impKey}")
    private String impKey;
    @Value("${spring.portone.impSecret}")
    private String impSecret;

    // 결제 정보 저장
    @Transactional
    public Payment create(PaymentRequest paymentRequest) {
        // Unix Timestamp를 LocalDateTime으로 변환
        LocalDateTime paidAt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(paymentRequest.paidAtTimestamp()),
                ZoneId.systemDefault()
        );

        Payment payment = Payment.builder()
                .merchantUid(paymentRequest.merchantUid())
                .amount(paymentRequest.amount())
                .paidAt(paidAt)
                .build();

        return paymentRepository.save(payment);
    }

    /*
     * 결제 취소
     * access token 발급 후 결제 취소 api 호출
     * portone api를 통해 결제는 취소하지만 row는 삭제하지 않음 (soft delete)
     * paymentStatus를 CANCELLED로 변경하여 취소로 처리
     */
    @Transactional
    public Payment softDelete(Long paymentId) {
        Payment payment = findById(paymentId);

        if (payment.getPaymentStatus() == PaymentStatus.CANCELLED) {
            throw new ServiceException("400", "이미 취소된 결제입니다.");
        }

        WebClient webClient = WebClient.create("https://api.iamport.kr");
        String accessToken = getAccessToken(webClient);
        ResponseEntity<Void> response = cancelPayment(webClient, accessToken, payment.getMerchantUid());

        if (response.getStatusCode().is2xxSuccessful()) {
            payment.setPaymentStatus(PaymentStatus.CANCELLED);
            return paymentRepository.save(payment);
        } else {
            throw new ServiceException(
                    response.getStatusCode().toString(),
                    "결제 취소에 실패했습니다.");
        }
    }

    // access token 발급
    public String getAccessToken(WebClient webClient) {
        ResponseEntity<TokenResponse> tokenResponse = webClient.post()
                .uri("/users/getToken")
                .header("Content-Type", "application/json")
                .bodyValue(new TokenRequest(impKey, impSecret))
                .retrieve()
                .toEntity(TokenResponse.class)
                .block();

        if (tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse.getBody().response().accessToken();
        } else {
            throw new ServiceException(
                    tokenResponse.getStatusCode().toString(),
                    "토큰 발급에 실패했습니다."
            );
        }
    }

    // 결제 취소 api 호출
    public ResponseEntity<Void> cancelPayment(WebClient webClient, String accessToken, String merchantUid) {
        return webClient.post()
                .uri("/payments/cancel")
                .header("Authorization", "Bearer " + accessToken)
                .bodyValue(Map.of("merchant_uid", merchantUid))
                .retrieve()
                .toEntity(Void.class) // 본문 무시
                .block();
    }

    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ServiceException("404", "결제 정보를 찾을 수 없습니다."));
    }
}
