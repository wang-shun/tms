package com.hoperun.framework.db;

import org.apache.log4j.Logger;

/**
 * <br>
 * <b>功能：</b>在调用Mapping之前AOP方法会将合适的数据源设置在该Holder类中，执行期间从这里获取设置的数据源<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> May 20, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class DynamicDataSourceHolder {
	private final static Logger logger = Logger
			.getLogger(DynamicDataSourceHolder.class);

	public static final ThreadLocal<String> holder = new ThreadLocal<String>();

	public static void putDataSource(String name) {
		logger.debug("put datasource name:" + name);
		holder.set(name);
	}

	public static String getDataSouce() {
		logger.debug("get datasource from ThreadLocal variable.");
		return holder.get();
	}
}