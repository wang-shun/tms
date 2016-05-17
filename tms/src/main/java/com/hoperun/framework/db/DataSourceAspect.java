package com.hoperun.framework.db;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.hoperun.framework.annotation.DataSource;

/**
 * <br>
 * <b>功能：</b>设置数据源的AOP方法,在调用Mapping类的数据库操作方法之前根据注解的设置，将对应的数据源防止在Holder中，
 * 具体的执行依赖于Spring中的AOP设置<br>
 * <p>
 * <!-- 配置数据库注解aop --> <aop:aspectj-autoproxy></aop:aspectj-autoproxy> <bean
 * id="multiDataSourceAspect" class="com.hoperun.framework.db.DataSourceAspect"
 * /> <aop:config> <aop:aspect id="c" ref="multiDataSourceAspect"> <aop:pointcut
 * id="tx" expression="execution(* com.hoperun.*.mapper.*.*(..))" /> <aop:before
 * pointcut-ref="tx" method="before" /> </aop:aspect> </aop:config> <!--
 * 配置数据库注解aop --> <br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> May 20, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class DataSourceAspect {
	private final static Logger logger = Logger
			.getLogger(DataSourceAspect.class);

	public void before(JoinPoint point) {
		Object target = point.getTarget();
		String method = point.getSignature().getName();

		Class<?>[] classz = target.getClass().getInterfaces();

		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
				.getMethod().getParameterTypes();
		try {
			Method m = classz[0].getMethod(method, parameterTypes);
			if (m != null && m.isAnnotationPresent(DataSource.class)) {
				DataSource data = m.getAnnotation(DataSource.class);
				DynamicDataSourceHolder.putDataSource(data.value());
				logger.debug(m.getName() + "----------->" + data.value());
			} else {
				logger.debug(m.getName() + "----------->" + "Nothing");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}