package com.hoperun.framework.exception;


/**
 * <br>
 * <b>功能：</b>HRSystemException<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class HRSystemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HRSystemException(String err){
		super(err);
	}
	
    public HRSystemException(Throwable cause) {
        super(cause);
    }
}
