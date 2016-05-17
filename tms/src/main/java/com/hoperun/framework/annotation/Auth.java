package com.hoperun.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <br>
 * <b>功能：</b>Auth<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface Auth {
	/**
	 * 是否验证登录 true=验证 ,false = 不验证
	 * 
	 * @return
	 */
	public boolean verifyLogin() default true;

	/**
	 * 验证key
	 * 
	 * @return
	 */
	public String verifyOperateKey() default "";

	/**
	 * 是否总部权限
	 * 
	 * @return
	 */
	public boolean isHQOperate() default true;

}
