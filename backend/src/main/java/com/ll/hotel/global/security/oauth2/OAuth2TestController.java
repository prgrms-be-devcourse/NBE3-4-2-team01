package com.ll.hotel.global.security.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2TestController {
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";  // 임시로 타임리프 템플릿을 사용해서 로그인 페이지 구현
    }
} 