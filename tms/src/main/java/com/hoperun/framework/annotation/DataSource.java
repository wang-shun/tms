package com.hoperun.framework.annotation;

import java.lang.annotation.ElementType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <br>
 * <b>功能：</b>注解，添加在Mapping方法的上方，用于标记进行分库查询的数据源名称，该名称在Spring配置中进行设置<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> May 20, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSource {
	String value();
}
