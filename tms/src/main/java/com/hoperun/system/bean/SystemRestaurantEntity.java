package com.hoperun.system.bean;

import java.util.Date;

import com.hoperun.framework.base.BaseBean;

/**
 * 门店基本信息的实体类。
 * 
 * @author huang_tao1
 */
public class SystemRestaurantEntity extends BaseBean
{
    /*
     * 门店ID<主键>
     */
    private Integer restaurantId;
    /*
     * 门店树ID<外键>
     */
    private Integer rtreeId;
    /*
     * 管理员<外键>
     */
    private Integer manager;
    /*
     * 门店中文名
     */
    private String nameCN;
    /*
     * 门店联系人
     */
    private String linkMan;
    /*
     * 联系电话
     */
    private String linkTele;
    /*
     * 中文地址
     */
    private String addressCN;
    /*
     * 是否已删除。（已删除：1， 未删除：0）
     */
    private Integer deleted = 0;
    /*
     * 创建人,唯一标识
     */
    private Integer creator;
    /*
     * 创建时间
     */
    private Date createTime;
    /*
     * 更新人，唯一标识
     */
    private Integer updator;
    /*
     * 更新时间
     */
    private Date updateTime;
    /*
     * 门店英文名
     */
    private String nameEn;
    /*
     * 门店编码
     */
    private String code;
    /*
     * 门店简称
     */
    private String shortName;
    /*
     * 英文地址
     */
    private String addressEn;
    /*
     * 邮政编码
     */
    private String zipCode;
    /*
     * 备注信息
     */
    private String remark;

    public Integer getRestaurantId()
    {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId)
    {
        this.restaurantId = restaurantId;
    }

    public Integer getRtreeId()
    {
        return rtreeId;
    }

    public void setRtreeId(Integer rtreeId)
    {
        this.rtreeId = rtreeId;
    }

    public Integer getManager()
    {
        return manager;
    }

    public void setManager(Integer manager)
    {
        this.manager = manager;
    }

    public String getNameCN()
    {
        return nameCN;
    }

    public void setNameCN(String nameCN)
    {
        this.nameCN = nameCN;
    }

    public String getLinkMan()
    {
        return linkMan;
    }

    public void setLinkMan(String linkMan)
    {
        this.linkMan = linkMan;
    }

    public String getLinkTele()
    {
        return linkTele;
    }

    public void setLinkTele(String linkTele)
    {
        this.linkTele = linkTele;
    }

    public String getAddressCN()
    {
        return addressCN;
    }

    public void setAddressCN(String addressCN)
    {
        this.addressCN = addressCN;
    }

    public Integer getDeleted()
    {
        return deleted;
    }

    public void setDeleted(Integer deleted)
    {
        this.deleted = deleted;
    }

    public Integer getCreator()
    {
        return creator;
    }

    public void setCreator(Integer creator)
    {
        this.creator = creator;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Integer getUpdator()
    {
        return updator;
    }

    public void setUpdator(Integer updator)
    {
        this.updator = updator;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getNameEn()
    {
        return nameEn;
    }

    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getAddressEn()
    {
        return addressEn;
    }

    public void setAddressEn(String addressEn)
    {
        this.addressEn = addressEn;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}