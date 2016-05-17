package com.hoperun.framework.log;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.hoperun.framework.log.bean.EnterpriseUserLogConfig;

/**
 * <br>
 * <b>功能：</b>读取userlog_action_config.properties &
 * userlog_module_config.properties 两个文件，将用户行为和系统模块进行分类。<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class UserlogConfig {

	private static Logger logger = Logger.getLogger(UserlogConfig.class);

	private Properties userlogModuleProperties;
	private Properties userlogActionProperties;

	private Map<String, EnterpriseUserLogConfig> targetMapping;

	public Map<String, EnterpriseUserLogConfig> getTargetMapping() {
		return this.targetMapping;
	}

	public Properties getUserlogModuleProperties() {
		return userlogModuleProperties;
	}

	public void setUserlogModuleProperties(Properties userlogModuleProperties) {
		this.userlogModuleProperties = userlogModuleProperties;
	}

	public Properties getUserlogActionProperties() {
		return userlogActionProperties;
	}

	public void setUserlogActionProperties(Properties userlogActionProperties) {
		this.userlogActionProperties = userlogActionProperties;
	}

	public void init() {
		targetMapping = loadMapping();
	}

	public boolean enableUserLog(String mapping) {
		if (mapping == null || targetMapping == null)
			return false;
		return targetMapping.containsKey(mapping);
	}

	public boolean enableUserLogParams(String mapping) {
		if (mapping == null || targetMapping == null)
			return false;
		if (!targetMapping.containsKey(mapping))
			return false;
		EnterpriseUserLogConfig config = targetMapping.get(mapping);
		return config.getEnableUserLogParams().intValue() == 1;
	}

	public void reloadConfig() {
		targetMapping = loadMapping();
	}

	private Map<String, EnterpriseUserLogConfig> loadMapping() {
		try {
			// return userLogConfigService.listEnabledUserLogConfig();
			return null;
		} catch (Exception e) {
			logger.error("init user log error:", e);
			return new HashMap<String, EnterpriseUserLogConfig>();
		}
	}
}
