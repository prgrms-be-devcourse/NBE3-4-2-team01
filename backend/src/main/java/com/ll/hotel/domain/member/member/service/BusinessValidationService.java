package com.ll.hotel.domain.member.member.service;

import com.ll.hotel.domain.member.member.dto.request.BusinessRequest;
import com.ll.hotel.domain.member.member.dto.response.BusinessResponse;
import com.ll.hotel.domain.member.member.type.BusinessApiProperties;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BusinessValidationService {
    private final RestTemplate restTemplate;
    private final BusinessApiProperties properties;

    public String validateBusiness(BusinessRequest.RegistrationInfo registrationInfo) {
        String apiUrl = properties.getValidationUrl() + "?serviceKey=" + properties.getServiceKey();

        URI uri = URI.create(apiUrl);

        BusinessRequest.RegistrationApiForm registrationApiForm = BusinessRequest.RegistrationApiForm.from(registrationInfo);

        try {
            ResponseEntity<BusinessResponse.Verification> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    new HttpEntity<>(registrationApiForm, createHeaders()),
                    BusinessResponse.Verification.class
            );

            BusinessResponse.Verification response = responseEntity.getBody();

            if (response == null || response.data() == null) {
                throw new ServiceException("500", "사업자 등록 검증 API 응답이 올바르지 않습니다.");
            }

            if (response.data().isEmpty()) {
                throw new ServiceException("400", "사업자 정보가 조회되지 않았습니다.");
            }

            Map<String, Object> result = response.data().getFirst();
            return (String) result.get("valid");

        } catch (RestClientResponseException e) {
            throw new ServiceException("402", "사업자 검증 API 호출 실패했습니다.");
        } catch (ResourceAccessException e) {
            throw new ServiceException("503", "사업자 검증 API 요청 중 네트워크 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new ServiceException("500", "사업자 검증 중 예기치 못한 오류가 발생했습니다.");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
