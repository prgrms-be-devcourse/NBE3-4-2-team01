package com.ll.hotel.domain.booking.payment.service;

import com.ll.hotel.domain.booking.booking.dto.BookingRequest;
import com.ll.hotel.domain.booking.payment.dto.PaymentRequest;
import com.ll.hotel.domain.booking.payment.dto.TokenRequest;
import com.ll.hotel.domain.booking.payment.dto.TokenResponse;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.repository.PaymentRepository;
import com.ll.hotel.domain.booking.payment.type.PaymentStatus;
import com.ll.hotel.global.exceptions.ServiceException;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import com.ll.hotel.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final String CREATE_ERROR_MESSAGE = "결제 정보 저장에 실패했습니다. 관리자에게 문의하세요.";
    private final PaymentRepository paymentRepository;
    @Value("${api-keys.portone.impKey}")
    private String impKey;
    @Value("${api-keys.portone.impSecret}")
    private String impSecret;

    public long count() {
        return paymentRepository.count();
    }

    public String generateMerchantUid() {
        int genCount = 10;
        int uidLength = 10;

        //10번 이내애 중복되지 않는 merchantUid를 생성해야 함, 그렇지 않으면 예외 처리
        while (genCount-- > 0) {
            String merchantUid = Ut.random.generateUID(uidLength);
            if (!paymentRepository.existsByMerchantUid(merchantUid)) {
                return merchantUid;
            }
        }

        throw new ServiceException("400", "merchantUid 생성에 실패했습니다.");
    }

    /*
     * 결제 정보 저장
     * BookingService에서 호출하여 예약 정보 저장과 동시에 발생하도록 함
     * 결제 오류 코드는 500-1, 500-2, 500-3
     * 예약 오류 코드는 500-4, 500-5, 500-6 (BookingService 코드 참고)
     * 코드 500-1: paymentRequest 생성 중 에러
     * 코드 500-2: DateTime 관련 에러
     * 코드 500-3: 결제 생성 및 저장 중 에러
     */
    @Transactional
    public Payment create(BookingRequest bookingRequest) {
        PaymentRequest paymentRequest;

        try {
            paymentRequest = new PaymentRequest(
                    bookingRequest.merchantUid(),
                    bookingRequest.amount(),
                    bookingRequest.paidAtTimestamp()
            );
        } catch (Exception e) {
            throw new ServiceException("500-1", CREATE_ERROR_MESSAGE);
        }

        return create(paymentRequest);
    }

    @Transactional
    public Payment create(PaymentRequest paymentRequest) {
        try {
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
        } catch (DateTimeException e) {
            throw new ServiceException("500-2", CREATE_ERROR_MESSAGE);
        } catch (Exception e) {
            throw new ServiceException("500-3", CREATE_ERROR_MESSAGE);
        }
    }

    /*
     * 결제 취소
     * access token 발급 후 결제 취소 api 호출
     * portone api를 통해 결제는 취소하지만 row는 삭제하지 않음 (soft delete)
     * paymentStatus를 CANCELLED로 변경하여 취소로 처리
     */
    public Payment softDelete(Long paymentId) {
        return softDelete(findById(paymentId));
    }

    @Transactional
    public Payment softDelete(Payment payment) {
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
                    "결제 취소에 실패했습니다."
            );
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
