package com.hoperun.framework.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>BaseBean<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class BaseBean implements Serializable{


	private String order;

	private String sort;

	@JsonIgnore
	public String getOrder() {
		return order;
	}
	
	@JsonProperty("order")
	public void setOrder(String order) {
		this.order = order;
	}

	@JsonIgnore
	public String getSort() {
		return sort;
	}
	
	@JsonProperty("sort")
	public void setSort(String sort) {
		this.sort = sort;
	}

	@JsonIgnore
	public String getOrderCondition() {
		String orderCondition = null;
		if (sort != null && !"".equals(sort))
			orderCondition = " order by " + sort + " " + order;
		return orderCondition;
	}

	/**
	 * 状态枚举
	 * 
	 * @author Wang Jiahao
	 * 
	 */
	public static enum STATE {
		ENABLE(0, "可用"), DISABLE(1, "禁用");
		private int key;
		private String value;

		private STATE(int key, String value) {
			this.key = key;
			this.value = value;
		}

		public static STATE get(int key) {
			STATE[] values = STATE.values();
			for (STATE object : values) {
				if (object.key == key) {
					return object;
				}
			}
			return null;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * 删除枚举
	 * 
	 * @author lu
	 * 
	 */
	public static enum DELETED {
		NO(0, "未删除"), YES(1, "已删除");
		private int key;
		private String value;

		private DELETED(int key, String value) {
			this.key = key;
			this.value = value;
		}

		public static DELETED get(int key) {
			DELETED[] values = DELETED.values();
			for (DELETED object : values) {
				if (object.key == key) {
					return object;
				}
			}
			return null;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
