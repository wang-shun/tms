package com.hoperun.tms.bean.authority;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色类
 * @author 15120071
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenu implements Serializable{
	
	private Integer id;
	
	private Integer menu_id;
	
	private Integer role_id;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the menu_id
	 */
	public Integer getMenu_id() {
		return menu_id;
	}

	/**
	 * @param menu_id the menu_id to set
	 */
	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	/**
	 * @return the role_id
	 */
	public Integer getRole_id() {
		return role_id;
	}

	/**
	 * @param role_id the role_id to set
	 */
	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

}
