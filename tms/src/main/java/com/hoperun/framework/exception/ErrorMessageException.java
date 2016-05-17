package com.hoperun.framework.exception;


/**
 * <br>
 * <b>功能：</b>ErrorMessageException<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Mar 29, 2016 <br>
 * <b>版权所有：<b>版权所有(C) 2016，www.hoperun.com<br>
 */
public class ErrorMessageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrorMessageException(String err){
		super(err);
	}
	
    public ErrorMessageException(Throwable cause) {
        super(cause);
    }
}
