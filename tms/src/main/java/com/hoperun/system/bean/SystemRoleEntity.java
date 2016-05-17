package com.hoperun.system.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoperun.framework.base.BaseBean;

/**
 * 表示系统角色的类。
 * 
 * @author ye_wenchen
 */
public class SystemRoleEntity extends BaseBean
{
    // 角色唯一标识。
    private Integer roleId;

    // 角色名称。
    private String name;

    // 备注。
    private String remark;

    // 角色类型，1 总部，2 门店。
    private Integer roleType;

    // 创建者唯一标识。
    private Integer creator;

    // 创建时间。
    private String createTime;

    // 修改者唯一标识。
    private Integer updator;

    // 修改时间。
    private String updateTime;

    public Integer getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Integer roleId)
    {
        this.roleId = roleId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getRoleType()
    {
        return roleType;
    }

    public void setRoleType(Integer roleType)
    {
        this.roleType = roleType;
    }

    @JsonIgnore
    public Integer getCreator()
    {
        return creator;
    }

    public void setCreator(Integer creator)
    {
        this.creator = creator;
    }

    @JsonIgnore
    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    @JsonIgnore
    public Integer getUpdator()
    {
        return updator;
    }

    public void setUpdator(Integer updator)
    {
        this.updator = updator;
    }

    @JsonIgnore
    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
}