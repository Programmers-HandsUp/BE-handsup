package dev.handsup.common.redisson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DistributeLock {

	String key(); // 락 이름

	TimeUnit timeUnit() default TimeUnit.SECONDS; // 시간 단위

	long waitTime() default 5L; // 대기 시간

	long leaseTime() default 3L; // 임대 시간
}
