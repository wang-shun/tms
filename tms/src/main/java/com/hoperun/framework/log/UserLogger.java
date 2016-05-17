package com.hoperun.framework.log;

public interface UserLogger {
	public boolean enableUserLog(String mapping);
	public boolean enableUserLogParams(String mapping);
	public void writeUserLog(String mapping,int userid,int result,String requestParams);
}
