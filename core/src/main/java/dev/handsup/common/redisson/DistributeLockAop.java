package dev.handsup.common.redisson;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import dev.handsup.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributeLockAop {

	private static final String REDISSON_KEY_PREFIX = "RLOCK_";
	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(dev.handsup.common.redisson.DistributeLock)")
	public Object concurrencyLock(final ProceedingJoinPoint joinPoint) throws Throwable {

		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributeLock annotation = method.getAnnotation(DistributeLock.class);

		final long waitTime = annotation.waitTime();
		final long leaseTime = annotation.leaseTime();
		final TimeUnit unit = annotation.timeUnit();
		final String lockName = annotation.key();

		String key = REDISSON_KEY_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), lockName);
		RLock rLock = redissonClient.getLock(key);

		try {
			boolean hasLock = rLock.tryLock(waitTime, leaseTime, unit);

			if (!hasLock) {
				throw new ValidationException(LockErrorCode.FAILED_TO_GET_LOCK);
			}

			log.info("get lock success {}", key);
			// 실제 수행 로직
			return aopForTransaction.proceed(joinPoint);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.info("get lock fail {}", key);
			throw new InterruptedException();
		} finally {
			rLock.unlock();
		}
	}
}
