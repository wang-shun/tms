package com.hoperun.framework.log.bean;

import java.io.Serializable;

/**
 * <br>
 * <b>功能：</b>EnterpriseUserLogConfig<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
@SuppressWarnings("serial")
public class EnterpriseUserLogConfig implements Serializable {

	private Integer id;
	private String requestMapping;
	private String moduleKey;
	private String actionType;
	private String actionDesc;
	private Integer enableUserLog;
	private Integer enableUserLogParams;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequestMapping() {
		return requestMapping;
	}

	public void setRequestMapping(String requestMapping) {
		this.requestMapping = requestMapping;
	}

	public String getModuleKey() {
		return moduleKey;
	}

	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public Integer getEnableUserLog() {
		return enableUserLog;
	}

	public void setEnableUserLog(Integer enableUserLog) {
		this.enableUserLog = enableUserLog;
	}

	public Integer getEnableUserLogParams() {
		return enableUserLogParams;
	}

	public void setEnableUserLogParams(Integer enableUserLogParams) {
		this.enableUserLogParams = enableUserLogParams;
	}
}
