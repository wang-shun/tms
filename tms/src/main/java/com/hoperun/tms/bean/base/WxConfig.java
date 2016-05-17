package com.hoperun.tms.bean.base;

public class WxConfig {

	private String appId;
	private String noncestr;
	private String timestamp;
	private String signature;
	
	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * @return the noncestr
	 */
	public String getNoncestr() {
		return noncestr;
	}

	/**
	 * @param noncestr the noncestr to set
	 */
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
}
