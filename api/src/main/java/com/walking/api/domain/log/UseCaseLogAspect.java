package com.walking.api.domain.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class UseCaseLogAspect {

	/** DAO 패키지 내의 모든 메소드에 대해 로깅을 수행한다. */
	@Pointcut(value = "execution(*  com.walking.api.domain..*.usecase..*.*(..))")
	public void useCaseAdvice() {}

	@Around("useCaseAdvice()")
	public Object requestLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature signature = joinPoint.getSignature();
		String[] splitByDot = signature.getDeclaringTypeName().split("\\.");
		String useCaseName = splitByDot[splitByDot.length - 1];

		Object[] args = joinPoint.getArgs();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		log.debug("{} execute with {}", useCaseName, args);

		Object proceed = joinPoint.proceed();

		stopWatch.stop();
		log.debug("{} finished in {}ms", useCaseName, stopWatch.getTime());
		return proceed;
	}
}
