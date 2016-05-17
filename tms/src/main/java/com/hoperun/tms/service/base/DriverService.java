package com.hoperun.tms.service.base;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hoperun.framework.utils.JacksonJsonUtil;
import com.hoperun.framework.utils.SessionUtils;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.DriverExample;
import com.hoperun.tms.bean.base.extend.ExDriver;
import com.hoperun.tms.mapper.base.extend.ExDriverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.base.Driver;

@Service
public class DriverService {

    @Autowired
    private ExDriverMapper exDriverMapper;

    public List<Driver> getDriverList(DriverExample e) {
        return exDriverMapper.selectByExample(e);
    }
    public Map getDriverAndVno(String phoneNum) {
    	return exDriverMapper.selectDriverAndVno(phoneNum);
    }

    public Map getDriverListPaginate(DriverExample e, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        Page startPage = PageHelper.startPage(pageNumber, pageSize);
        List<ExDriver> driverList = exDriverMapper.selectByExample(e);
        Long totalrecords = startPage.getTotal();
        map.put("driverList", driverList);
        map.put("currpage", pageNumber);
        map.put("totalpages", totalrecords == 0 ? 1 : (totalrecords - 1) / pageSize + 1);
        map.put("totalrecords", totalrecords);
        return map;
    }

    public int updateMutiDrivers(Driver d, DriverExample e) throws Exception {
        d.setCreatedAt(new Date());
        d.setCreatedBy(SessionUtils.getUserInfo());
        d.setUpdatedAt(new Date());
        d.setUpdatedBy(SessionUtils.getUserInfo());
        return exDriverMapper.updateByExampleSelective(d, e);
    }

    /**
     * 司机信息修改
     * driver必须包含主键ID
     *
     * @param driver
     * @throws Exception
     */
    public void updateById(Driver driver) throws Exception {
        if (null == driver.getId()) {
            throw new Exception("司机信息修改失败，未正确获取司机ID");
        }
        uniqueValidate(driver);
        nullableValidate(driver);
        driver.setUpdatedBy("ceshi");
        driver.setUpdatedAt(new Date());
        exDriverMapper.updateByPrimaryKeySelective(driver);
    }
	public void updateBatch(List<LinkedHashMap> receiverList) throws Exception {
		for (int i = 0; i < receiverList.size(); i++) {
			String json = JacksonJsonUtil.beanToJson(receiverList.get(i));
			Driver driver = (Driver) JacksonJsonUtil.jsonToBean(json, new Driver().getClass());
			try {
				updateById(driver);
			} catch (Exception e) {
				 throw new Exception("第【"+(i+1)+"】条更新出错，"+e.getMessage());
			}
		}
	}

    public void insertDriver(Driver driver) throws Exception {
        uniqueValidate(driver);
        nullableValidate(driver);

        driver.setCreatedBy("ceshi");
        driver.setCreatedAt(new Date());
        driver.setUpdatedBy("ceshi");
        driver.setUpdatedAt(new Date());
        exDriverMapper.insert(driver);
    }

    /**
     * 司机信息作废
     * driver必须包含主键ID
     * @throws Exception
     */
    public void disable(String[] ids) throws Exception {
        for (String id : ids) {
            ExDriver exDriver = new ExDriver();
            exDriver.setId(Long.parseLong(id));
            exDriver.setStatus(ExDriver.STATUS_DISABLED);
            exDriver.setUpdatedBy("ceshi");
            exDriver.setUpdatedAt(new Date());
            if (null == exDriver.getId()) {
                throw new Exception("司机信息删除失败，未正确获取司机ID");
            }
            exDriverMapper.updateByPrimaryKeySelective(exDriver);
        }
    }

    public Driver getDriverById(Long id) {
        return exDriverMapper.selectByPrimaryKey(id);
    }

    private void uniqueValidate(Driver driver) throws Exception {
        DriverExample e = new DriverExample();
        DriverExample.Criteria c1 = e.createCriteria();
        DriverExample.Criteria c2 = e.or();

        c1.andDIdEqualTo(driver.getdId()).andStatusEqualTo(ExDriver.STATUS_ENABLED);
        c2.andContactEqualTo(driver.getContact()).andStatusEqualTo(ExDriver.STATUS_ENABLED);
        if (null != driver.getId()) {
            c1.andIdNotEqualTo(driver.getId());
            c2.andIdNotEqualTo(driver.getId());
        }

        if(ExDriver.CAR_TYPE_TRUCK.equals(driver.getCerCarType())){
            DriverExample.Criteria c3 = e.or();
            c3.andCerNoEqualTo(driver.getCerNo()).andStatusEqualTo(ExDriver.STATUS_ENABLED);
            if (null != driver.getId()) {
                c3.andIdNotEqualTo(driver.getId());
            }
        }

        List<Driver> drivers = exDriverMapper.selectByExample(e);
        if (!drivers.isEmpty()) {
            for (Driver existedDriver : drivers) {
                if (!StringUtil.isEmpty(existedDriver.getdId())
                        && existedDriver.getdId().equals(driver.getdId())) {
                    throw new Exception("身份证号:" + driver.getdId() + "在司机列表中已存在。");
                } else if (!StringUtil.isEmpty(existedDriver.getContact())
                        && existedDriver.getContact().equals(driver.getContact())) {
                    throw new Exception("联系电话:" + driver.getContact() + "在司机列表中已存在。");
                } else if (!StringUtil.isEmpty(existedDriver.getCerNo())
                        && existedDriver.getCerNo().equals(driver.getCerNo())) {
                    throw new Exception("驾驶证号:" + driver.getCerNo() + "在司机列表中已存在。");
                }
            }
        }
    }
    
    private void nullableValidate(Driver driver) throws Exception {
    	if(ExDriver.CAR_TYPE_TRUCK.equals(driver.getCerCarType())){
    		if(StringUtil.isEmpty(driver.getCerType())){
    			throw new Exception("客车的驾驶证类型不能为空");
    		}
    		if(driver.getCerExpireDate()==null){
    			throw new Exception("卡车的驾驶证到期日不能为空");
    		}
    		if(StringUtil.isEmpty(driver.getCerNo())){
    			throw new Exception("卡车的驾驶证号码不能为空");
    		}
    	}
    }
}
