package com.czx.demoj.spring.lombok.aspect;

import com.czx.demoj.spring.lombok.entity.AuditedEntity;
import com.czx.demoj.spring.lombok.entity.ImmutableAuditedEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Aspect
@Component
@Slf4j
public class TimezoneAspect {

    //    @Around(value = "execution(* com.czx.demoj.spring.lombok.service..*(..)) && args(..,timezone)")
    @Around("execution(* com.czx.demoj.spring.lombok.service..*(..))" +
            "&& @annotation(com.czx.demoj.spring.lombok.anno.Audited)" +
            "&& args(.., timezone)")
    public Object convertTimezone(ProceedingJoinPoint joinPoint, String timezone) throws Throwable {
        log.debug("% convert timezone ==>  params:\njoinPoint: {},\ntimezone: {}", joinPoint, timezone);
        Object result = joinPoint.proceed();

        if (result instanceof AuditedEntity entity) {
            if (timezone != null && !timezone.isEmpty()) {
                ZoneId zoneId = ZoneId.of(timezone);
                entity.convertToTimezone(zoneId);
            }
        }

        return result;
    }

}
