package com.board.aop;

import java.util.Collections;
import java.util.List;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspect {

	@Autowired
	private TransactionManager transactionManager;

	private static String EXPRESSION = "execution(* com.board..service.*Impl.*(..))";

	@Bean
	public TransactionInterceptor transactionAdvice() {

		List<RollbackRuleAttribute> rollbackRules = Collections.singletonList(new RollbackRuleAttribute(Exception.class));

		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();

		transactionAttribute.setRollbackRules(rollbackRules);
		transactionAttribute.setName("*");

		MatchAlwaysTransactionAttributeSource attributeSource = new MatchAlwaysTransactionAttributeSource();
		attributeSource.setTransactionAttribute(transactionAttribute);

		return new TransactionInterceptor(transactionManager, attributeSource);
	}

	/*
	 * private static final String AOP_TRANSACTION_METHOD_NAME = "*": 트랜잭션을 설정할 때 사용되는 설정값을 상수로 선언합니다.
	 * transactionAttribute.setName(AOP_TRANSACTION_METHOD_NAME): 트랜잭션 이름을 설정합니다. 트랜잭션 모니터에서 트랜잭션 이름을 확인할 수 있습니다.
	 * transactionAttribute.setRollbackRules(Collectection.singletonList(new RollbackRullAttribute(Exception.class))): 트랜잭션에서 롤백을 하는 룰을 설정합니다. 여기서는 예외가 발생했을 때 롤백이 되도록 지정했습니다.
	 * Exception.class를 롤백 룰로 등록하면, 자바의 모든 예외는 Exception 클래스를 상속받으므로 어떠한 예외가 발생하더라도 롤백이 수행됩니다.
	 */
	@Bean
	public Advisor transactionAdvisor() {

		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(EXPRESSION);

		return new DefaultPointcutAdvisor(pointcut, transactionAdvice());

	}
// pointcut.setExpression(AOP_TRANSATION_EXPRESSION):AOP 포인트컷을 설정합니다. 여기서는 비즈니스 로직이 수행되는 모든 ServiceImpl 클래스의 모든 메소드를 지정했습니다.


}
