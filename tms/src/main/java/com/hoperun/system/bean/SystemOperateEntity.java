package com.hoperun.system.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hoperun.framework.base.BaseBean;

/**
 * <br>
 * <b>功能：</b>表示系统可以进行的操作实例的类<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Mar 23, 2016 <br>
 * <b>版权所有：<b>版权所有(C) 2016，www.hoperun.com<br>
 */
public class SystemOperateEntity extends BaseBean
{
    private Integer operateId;
    private Integer parentId;
    private String groupName;
    private String name;
    private String keyName;
    private Integer operateType;
    private String remark;
    private String url;

    // 用于查询使用，但是不作为VO转换成json显示
    private Integer roleId;

    public Integer getOperateId()
    {
        return operateId;
    }

    public void setOperateId(Integer operateId)
    {
        this.operateId = operateId;
    }

    public Integer getParentId()
    {
        return parentId;
    }

    public void setParentId(Integer parentId)
    {
        this.parentId = parentId;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getOperateType()
    {
        return operateType;
    }

    public void setOperateType(Integer operateType)
    {
        this.operateType = operateType;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @JsonIgnore
    public Integer getRoleId()
    {
        return roleId;
    }

    @JsonProperty("roleId")
    public void setRoleId(Integer roleId)
    {
        this.roleId = roleId;
    }

    public String getKeyName()
    {
        return keyName;
    }

    public void setKeyName(String keyName)
    {
        this.keyName = keyName;
    }

    @Override
    public boolean equals(Object obj)
    {

        if (obj instanceof SystemOperateEntity)
        {

            SystemOperateEntity p = (SystemOperateEntity) obj;

            return (operateId.equals(p.operateId));

        }

        return super.equals(obj);

    }

    @Override
    public int hashCode()
    {
        return operateId.hashCode() * 13;
    }
}