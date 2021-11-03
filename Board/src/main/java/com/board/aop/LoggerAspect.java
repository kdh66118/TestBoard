package com.board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
/*
	@Bean은 개발자가 제어할 수 없는 외부 라이브러리를 빈(Bean)으로 등록할 때 사용하고,
	@Component는 개발자가 직접 정의한 클래스를 빈(Bean)으로 등록할 때 사용합니다.
*/
public class LoggerAspect {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Around("execution(* com.board..controller.*Controller.*(..)) or execution(* com.board..service.*Impl.*(..)) or execution(* com.board..mapper.*Mapper.*(..))")
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable{
		String type = "";
		String name = joinPoint.getSignature().getDeclaringTypeName();

		if(name.contains("Controlle") == true) {
			type = "Controller ===> ";
		}else if(name.contains("Service") == true) {
			type = "Service ===> ";
		}else if(name.contains("Mapper") == true) {
			type = "Mapper ===> ";
		}
		logger.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
		return joinPoint.proceed();
	}
}
