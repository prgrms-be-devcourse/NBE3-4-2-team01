package com.ll.hotel.global.rq;

import com.ll.hotel.domain.member.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse res;

    /**
     * 임시, 수정 필요
     */
    public Member getMember() {
        return new Member();
    }

    /**
     * 임시, 수정 필요
     */
    public static boolean checkAdmin() {
//        if (!isAdmin) {
//            new ServiceException("403", "관리자만 해당 기능을 수행할 수 있습니다.");
//        }

        return true;
    }
}
