package com.hoperun.framework.exception;


/**
 * <br>
 * <b>功能：</b>LoginException<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class LoginException extends HRSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginException(String err){
		super(err);
	}
}
