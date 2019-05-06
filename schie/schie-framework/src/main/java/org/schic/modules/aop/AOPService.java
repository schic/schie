package org.schic.modules.aop;

import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @Description: aop
 * @author Caiwb
 * @date 2019年5月5日 下午4:51:33
 */
@Aspect
@Component
public class AOPService {
	private static Logger logger = LoggerFactory.getLogger(AOPService.class);
	// defined aop pointcut
	//@Pointcut("execution(* com.company.project.modules.*.*(..))")
	//@Pointcut("execution(* com.company.project.modules..*.*(..))")
	/*任意公共方法的执行：
	execution(public * *(..))
	任何一个以“set”开始的方法的执行：
	execution(* set*(..))
	AccountService 接口的任意方法的执行：
	execution(* com.xyz.service.AccountService.*(..))
	定义在service包里的任意方法的执行：
	execution(* com.xyz.service.*.*(..))
	定义在service包和所有子包里的任意类的任意方法的执行：
	execution(* com.xyz.service..*.*(..))
	定义在pointcutexp包和所有子包里的JoinPointObjP2类的任意方法的执行：
	execution(* com.test.spring.aop.pointcutexp..JoinPointObjP2.*(..))
	最靠近(..)的为方法名,靠近.*(..))的为类名或者接口名,如上例的JoinPointObjP2.*(..))*/
	@Pointcut("execution(* com.company.project.modules.*.*.*.*(..))")
	public void controllerLog() {
	}

	// log all of controller
	@Before("controllerLog()")
	public void before(JoinPoint joinPoint) {
		logger.debug("{} | AOP Before:{},method:{}", new Date(),
				joinPoint.getSignature().getDeclaringType(),
				joinPoint.getSignature().getName());
	}

	// result of return
	@AfterReturning(pointcut = "controllerLog()", returning = "retVal")
	public void after(JoinPoint joinPoint, Object retVal) {
		logger.debug("{} | AOP AfterReturning:Object", new Date());
	}

}
