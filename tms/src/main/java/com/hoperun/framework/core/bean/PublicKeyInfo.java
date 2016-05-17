package com.hoperun.framework.core.bean;

/**
 * 
 * <br>
 * <b>功能：</b>PublicKeyInfo<br>
 * <b>作者：</b>wang_jiahao<br>
 * <b>日期：</b>2015年6月8日 下午5:02:40<br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class PublicKeyInfo {
	//
	String exponent;
	//
	String modulus;

	/**
	 * 
	 * @param exponent
	 * @param modulus
	 */
	public PublicKeyInfo(String exponent, String modulus) {
		this.exponent = exponent;
		this.modulus = modulus;
	}

	public String getExponent() {
		return exponent;
	}

	public void setExponent(String exponent) {
		this.exponent = exponent;
	}

	public String getModulus() {
		return modulus;
	}

	public void setModulus(String modulus) {
		this.modulus = modulus;
	}
}
