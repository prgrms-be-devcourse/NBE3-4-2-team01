package com.ll.hotel.global.aspect;

import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.standard.util.LogUtil;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class BusinessCheckAspect {

    @Before("@annotation(com.ll.hotel.global.annotation.BusinessOnly)")
    public void checkBusiness(JoinPoint joinPoint) {
        try {
            Arrays.stream(joinPoint.getArgs()).forEach(arg -> {
                if (arg instanceof Member member) {
                    member.checkBusiness();
                }
            });
        } catch (Throwable ex) {
            LogUtil.logError(log, joinPoint, ex);
            throw ex;
        }
    }
}
