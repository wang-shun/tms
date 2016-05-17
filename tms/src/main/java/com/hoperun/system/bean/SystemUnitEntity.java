package com.hoperun.system.bean;

import com.hoperun.framework.base.BaseBean;

/**
 * <br>
 * <b>功能：</b>系统单位实体<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Mar 30, 2016 <br>
 * <b>版权所有：<b>版权所有(C) 2016，www.hoperun.com<br>
 */
public class SystemUnitEntity extends BaseBean {
	private Integer unitId;
	private String unitKey;
	private String nameCN;
	private String nameEN;

	public String getUnitKey() {
		return unitKey;
	}

	public void setUnitKey(String unitKey) {
		this.unitKey = unitKey;
	}

	public String getNameCN() {
		return nameCN;
	}

	public void setNameCN(String nameCN) {
		this.nameCN = nameCN;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

}