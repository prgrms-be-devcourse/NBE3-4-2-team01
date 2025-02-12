package com.ll.hotel.domain.booking.payment.service;

import com.ll.hotel.domain.booking.booking.dto.BookingRequest;
import com.ll.hotel.domain.booking.payment.dto.*;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.repository.PaymentRepository;
import com.ll.hotel.domain.booking.payment.type.PaymentStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import com.ll.hotel.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    /*
     * portone api 호출에 필요한 keys
     * application-api-keys.yml에 저장된 값을 가져옴
     */
    @Value("${api-keys.portone.impKey}")
    private String impKey;
    @Value("${api-keys.portone.impSecret}")
    private String impSecret;
    @Value("${api-keys.portone.apiId}")
    private String apiId;
    @Value("${api-keys.portone.channel-key}")
    private String channelKey;

    private final String CREATE_ERROR_MESSAGE = "결제 정보 저장에 실패했습니다.";
    private final int MAX_GENERATE_COUNT = 10;
    private final int UID_GENERATE_LENGTH = 10;
    private final PaymentRepository paymentRepository;

    public long count() {
        return paymentRepository.count();
    }

    // Uid 생성
    public UidResponse generateMerchantUid() {
        //MAX_GENERATE_COUNT번 이내애 중복되지 않는 merchantUid를 생성해야 함, 리팩토링 예정
        for (int i = 0; i < MAX_GENERATE_COUNT; i++) {
            String merchantUid = Ut.random.generateUID(UID_GENERATE_LENGTH);
            if (!paymentRepository.existsByMerchantUid(merchantUid)) {
                return new UidResponse(apiId, channelKey, merchantUid);
            }
        }

        throw new ServiceException("500", "merchantUid 생성에 실패했습니다.");
    }

    /*
     * 결제 정보 저장
     * BookingService에서 호출하여 예약 정보 저장과 동시에 발생하도록 함
     */
    @Transactional
    public Payment create(BookingRequest bookingRequest) {
        try {
            PaymentRequest paymentRequest = PaymentRequest.from(bookingRequest);

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
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("결제 처리 중 에러 발생", e);
            throw new ServiceException("500", "결제 정보 저장에 실패했습니다.");
        }
    }

    /*
     * 결제 취소
     * access token 발급 후 결제 취소 api 호출
     * portone api를 통해 결제는 취소하지만 데이터를 삭제하지는 않음 (soft delete)
     * paymentStatus를 CANCELLED로 변경하여 취소로 처리
     */
    @Transactional
    public Payment softDelete(Payment payment) {
        // 이미 취소되었을 경우
        if (payment.getPaymentStatus() == PaymentStatus.CANCELLED) {
            throw new ServiceException("400", "이미 취소된 결제입니다.");
        }

        WebClient webClient = WebClient.create("https://api.iamport.kr");
        String accessToken = getAccessToken(webClient);

        return cancelPayment(webClient, accessToken, payment);
    }

    // access token 발급
    public String getAccessToken(WebClient webClient) {
        try {
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
                        "토큰 발급 권한이 없습니다."
                );
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("결제 토큰 발급 중 에러 발생", e);
            throw new ServiceException("500", "토큰 발급에 실패했습니다.");
        }
    }

    // 결제 취소
    @Transactional
    public Payment cancelPayment(WebClient webClient, String accessToken, Payment payment) {
        try {
            // 취소 api 호출
            String merchantUid = payment.getMerchantUid();
            ResponseEntity<Void> response = webClient.post()
                    .uri("/payments/cancel")
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(Map.of("merchant_uid", merchantUid))
                    .retrieve()
                    .toEntity(Void.class) // 본문 무시
                    .block();

            // 취소 상태 변경
            if (response.getStatusCode().is2xxSuccessful()) {
                payment.setPaymentStatus(PaymentStatus.CANCELLED);
                return paymentRepository.save(payment);
            } else {
                throw new ServiceException(
                        response.getStatusCode().toString(),
                        "결제 취소 권한이 없습니다."
                );
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("결제 취소 중 에러 발생", e);
            throw new ServiceException("500", "결제 취소에 실패했습니다.");
        }
    }

    // 기본 조회 메서드
    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ServiceException("404", "결제 정보를 찾을 수 없습니다."));
    }
}
