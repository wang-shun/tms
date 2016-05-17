package com.hoperun.tms.bean.authority;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class User implements Serializable{
	
	private Integer id;
	
	private String login_name;
	
	private String password;
	
	private String vsername;
	
	private String mobile;
	
	private String email;
	
	private Date create_time;
	
	private Date lastLoginTime;
	
	private String lastLoginIp;
	
	/** 登录盐 */
	private String salt;
	
	public String getCredentialsSalt() {
        return login_name + salt;
    }

	private List<Menu> MenuList;

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
	 * @return the login_name
	 */
	public String getLogin_name() {
		return login_name;
	}

	/**
	 * @param login_name the login_name to set
	 */
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the vsername
	 */
	public String getVsername() {
		return vsername;
	}

	/**
	 * @param vsername the vsername to set
	 */
	public void setVsername(String vsername) {
		this.vsername = vsername;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the lastLoginTime
	 */
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime the lastLoginTime to set
	 */
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the lastLoginIp
	 */
	public String getLastLoginIp() {
		return lastLoginIp;
	}

	/**
	 * @param lastLoginIp the lastLoginIp to set
	 */
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the menuList
	 */
	public List<Menu> getMenuList() {
		return MenuList;
	}

	/**
	 * @param menuList the menuList to set
	 */
	public void setMenuList(List<Menu> menuList) {
		MenuList = menuList;
	}
	

}
