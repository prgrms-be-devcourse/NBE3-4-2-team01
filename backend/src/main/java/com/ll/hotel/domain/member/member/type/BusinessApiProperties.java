package com.ll.hotel.domain.member.member.type;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties(prefix = "business-api")
public class BusinessApiProperties {
    private String serviceKey;
    private String validationUrl;
    private String statusUrl;
}
