package com.hoperun.tms.bean.authorityDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单页面展示类
 * @author 15120071
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
	
	private Integer id;
	
	private String description;
	
	private String name;
	
	private String url;
	
	private Boolean isParent;
	
	@JsonProperty("pId") 
	private Integer pId;
	
	private String sn;
	
	private Boolean checked;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the isParent
	 */
	public Boolean getIsParent() {
		return isParent;
	}

	/**
	 * @param isParent the isParent to set
	 */
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	/**
	 * @return the pId
	 */
	public Integer getpId() {
		return pId;
	}

	/**
	 * @param pId the pId to set
	 */
	public void setpId(Integer pId) {
		this.pId = pId;
	}

	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	

}
