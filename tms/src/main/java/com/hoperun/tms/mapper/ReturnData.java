package com.hoperun.tms.mapper;

import java.io.Serializable;

/**
 * 与前台交互的返回数据
 * 
 * @author 王朝峰
 * @version 2016-1-20
 * 
 */
public class ReturnData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3483565335266804443L;

	public ReturnData(){
		
	}
	
	public ReturnData(boolean  status, Object data){
		this.data = data;
		this.setStatus(status);
	}
	
	/** 状态  */
	private  boolean  status; //1为success 2为失败
	
	/** 附带数据 */
	private  Object data;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
