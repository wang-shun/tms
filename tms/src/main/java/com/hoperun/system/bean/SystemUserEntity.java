package com.hoperun.system.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hoperun.framework.base.BaseBean;
import com.hoperun.framework.constants.SystemConstants;

/**
 * 表示员工基本信息实例的类。
 * 
 * @author ye_wenchen
 */
public class SystemUserEntity extends BaseBean implements Cloneable
{
    // 员工唯一标识。
    private Integer userId;

    // 员工中文名。
    private String nameCn;

    // 员工英文名。
    private String nameEn;

    // 登录账号。
    private String account;

    // 账号密码。
    private String password;

    // 工号。
    private String workNo;

    // 身份证号。
    private String identificationNo;

    // 手机号。
    private String mobile;

    // 电话号码。
    private String telephone;

    // 性别。男：1，女：0。
    private Integer gender;

    // 生日。
    private String birthday;

    // 是否启用。已启用：1，未启用：0。
    private Integer enabled;

    // 创建者唯一标识。
    private Integer creator;

    // 创建时间。
    private String createTime;

    // 修改者唯一标识。
    private Integer updator;

    // 修改时间。
    private String updateTime;

    // 备注。
    private String remark;

    // 是否已删除。已删除：1， 未删除：0。
    private Integer deleted;

    // 是否是超级管理员。是 ：1， 否：0。
    private Integer superAdmin = SystemConstants.YesOrNo.NO.getCode();

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getNameCn()
    {
        return nameCn;
    }

    public void setNameCn(String nameCn)
    {
        this.nameCn = nameCn;
    }

    public String getNameEn()
    {
        return nameEn;
    }

    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getWorkNo()
    {
        return workNo;
    }

    public void setWorkNo(String workNo)
    {
        this.workNo = workNo;
    }

    public String getIdentificationNo()
    {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo)
    {
        this.identificationNo = identificationNo;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public Integer getGender()
    {
        return gender;
    }

    public void setGender(Integer gender)
    {
        this.gender = gender;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    @JsonIgnore
    public Integer getCreator()
    {
        return creator;
    }

    @JsonProperty("creator")
    public void setCreator(Integer creator)
    {
        this.creator = creator;
    }
    
    @JsonIgnore
    public String getCreateTime()
    {
        return createTime;
    }

    @JsonProperty("createTime")
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    @JsonIgnore
    public Integer getUpdator()
    {
        return updator;
    }

    @JsonProperty("updator")
    public void setUpdator(Integer updator)
    {
        this.updator = updator;
    }

    @JsonIgnore
    public String getUpdateTime()
    {
        return updateTime;
    }

    @JsonProperty("updateTime")
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @JsonIgnore
    public Integer getDeleted()
    {
        return deleted;
    }

    @JsonProperty("deleted")
    public void setDeleted(Integer deleted)
    {
        this.deleted = deleted;
    }
    
    @JsonIgnore
    public Integer getSuperAdmin()
    {
        return superAdmin;
    }

    @JsonProperty("superAdmin")
    public void setSuperAdmin(Integer superAdmin)
    {
        this.superAdmin = superAdmin;
    }
    
    @JsonIgnore
    public boolean isSuper(){
    	return this.superAdmin.equals(SystemConstants.YesOrNo.YES.getCode());
    }

    public Object clone()
    {
        SystemUserEntity o = null;
        try
        {
            o = (SystemUserEntity) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return o;
    }
}