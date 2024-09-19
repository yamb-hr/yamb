package com.tejko.yamb.util;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseTimeAspect {

    private LongAdder totalTime = new LongAdder();
    private AtomicLong requestCount = new AtomicLong();

    @Around("execution(* com.tejko.yamb.api.controllers.*.*(..))")
    public Object measureResponseTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            totalTime.add(duration);
            requestCount.incrementAndGet();
        }
    }

    public double getAverageResponseTime() {
        long count = requestCount.get();
        if (count == 0) {
            return 0.0;
        }
        return totalTime.doubleValue() / count;
    }
}
