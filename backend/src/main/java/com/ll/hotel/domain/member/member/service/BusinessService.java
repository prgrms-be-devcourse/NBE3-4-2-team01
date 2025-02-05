package com.ll.hotel.domain.member.member.service;

import com.ll.hotel.domain.member.member.dto.request.BusinessRequest;
import com.ll.hotel.domain.member.member.dto.response.BusinessResponse;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.type.BusinessApiProperties;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final WebClient webClient;
    private final BusinessApiProperties properties;
    private final BusinessRepository businessRepository;

    @Transactional
    public Business register(BusinessRequest.RegistrationInfo registrationInfo, Member member, String validationResult) {

        BusinessApprovalStatus status;

        if (validationResult.equals("01")) {
            status = BusinessApprovalStatus.APPROVED;
        } else {
            status = BusinessApprovalStatus.REJECTED;
        }

        Business business = Business
                .builder()
                .businessRegistrationNumber(registrationInfo.businessRegistrationNumber())
                .approvalStatus(status)
                .member(member)
                .hotel(null)
                .build();
        return businessRepository.save(business);
    }

    public String validateBusiness(BusinessRequest.RegistrationInfo registrationInfo) {
        String apiUrl = properties.getValidationUrl() + "?serviceKey=" + properties.getServiceKey();
        BusinessRequest.RegistrationApiForm registrationApiForm = BusinessRequest.RegistrationApiForm.from(registrationInfo);

        try {
            BusinessResponse.Verification response = webClient.post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(registrationApiForm)
                    .retrieve()
                    .bodyToMono(BusinessResponse.Verification.class)
                    .block();

            if (response == null || response.data() == null) {
                throw new ServiceException("500", "사업자 등록 검증 API 응답이 올바르지 않습니다.");
            }

            if (response.data().isEmpty()) {
                throw new ServiceException("400", "사업자 정보가 조회되지 않았습니다.");
            }

            Map<String, Object> result = response.data().getFirst();
            return (String) result.get("valid");

        } catch (WebClientResponseException e) {
            throw new ServiceException("402", "사업자 검증 API 호출 실패했습니다.");
        } catch (WebClientRequestException e) {
            throw new ServiceException("503", "사업자 검증 API 요청 중 네트워크 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new ServiceException("500", "사업자 검증 중 예기치 못한 오류가 발생했습니다.");
        }
    }
}
