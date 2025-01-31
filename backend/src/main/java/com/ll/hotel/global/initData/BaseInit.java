package com.ll.hotel.global.initData;

import com.ll.hotel.global.security.oauth2.CustomOAuth2JwtProperties;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Data
public class BaseInit {

    private final CustomOAuth2JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(jwtProperties.getSecret()), SignatureAlgorithm.HS256.getJcaName());
    }
}
