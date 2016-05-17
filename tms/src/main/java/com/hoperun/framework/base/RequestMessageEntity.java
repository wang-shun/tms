package com.hoperun.framework.base;

/**
 * 
 * <br>
 * <b>功能：</b>RequestMessageEntity<br>
 * <b>作者：</b>wang_jiahao<br>
 * <b>日期：</b>2015年6月5日 下午3:42:17<br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class RequestMessageEntity<T> {
	private int page;
	private int row;
	private T data;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
