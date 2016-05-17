package com.hoperun.framework.db;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * <br>
 * <b>功能：</b>集成Spring抽象类的实现类，实现数据源的动态变化<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> May 20, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	private final static Logger log = Logger.getLogger(DynamicDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		log.debug("get datasource from DynamicDataSourceHolder.");
		return DynamicDataSourceHolder.getDataSouce();
	}

}
