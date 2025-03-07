package com.ll.hotel.global.aspect;

import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.standard.util.LogUtil;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class BusinessCheckAspect {

    @Around("@annotation(com.ll.hotel.global.annotation.BusinessOnly)")
    public Object checkBusiness(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Arrays.stream(joinPoint.getArgs()).forEach(arg -> {
                if (arg instanceof Member member) {
                    member.checkBusiness();
                }
            });

            return joinPoint.proceed();
        } catch (Throwable ex) {
            LogUtil.logError(log, joinPoint, ex);
            throw ex;
        }
    }
}
