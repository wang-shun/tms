package com.hoperun.tms.service.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.base.Driver;
import com.hoperun.tms.bean.base.Vehicle;
import com.hoperun.tms.bean.base.VehicleDriver;
import com.hoperun.tms.bean.base.VehicleDriverExample;
import com.hoperun.tms.bean.base.extend.ExVehicleDriver;
import com.hoperun.tms.mapper.base.DriverMapper;
import com.hoperun.tms.mapper.base.VehicleDriverMapper;
import com.hoperun.tms.mapper.base.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 16030117 on 2016/3/31.
 */
@Service
public class VehicleDriverService {
    @Autowired
    private VehicleDriverMapper vehicleDriverMapper;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    public Vehicle queryById(Long id){
        return vehicleDriverMapper.selectByPrimaryKey(id);
    }

    public Map getDriverListPaginate(VehicleDriverExample e, int pageNumber, int pageSize){
        Map<String,Object> map = new HashMap<>();
        Page startPage = PageHelper.startPage(pageNumber, pageSize);
        List<VehicleDriver> vehicleDriverList = vehicleDriverMapper.selectByExample(e);
        Long totalrecords = startPage.getTotal();
        map.put("vehicleDriverList", vehicleDriverList);
        map.put("currpage", pageNumber);
        map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
        map.put("totalrecords", totalrecords);
        return map;
    }

    public void add(VehicleDriver vehicleDriver) throws Exception{
        addInfobyId(vehicleDriver);
        uniqueValidate(vehicleDriver);
        vehicleDriver.setCreatedBy("ceshi");
        vehicleDriver.setCreatedAt(new Date());
        vehicleDriver.setUpdatedBy("ceshi");
        vehicleDriver.setUpdatedAt(new Date());
        vehicleDriverMapper.insert(vehicleDriver);
    }

    private void addInfobyId(VehicleDriver vehicleDriver){
        Vehicle vehicle = vehicleMapper.selectByPrimaryKey(vehicleDriver.getvId());
        if(null != vehicle){
            vehicleDriver.setvNo(vehicle.getvNo());
        }
        Driver driver = driverMapper.selectByPrimaryKey(vehicleDriver.getdId());
        if(null != driver){
            vehicleDriver.setdName(driver.getName());
        }
    }

    public void updateMultiVehicleDrivers(VehicleDriver t, VehicleDriverExample e) throws Exception {
        vehicleDriverMapper.updateByExampleSelective(t, e);
    }

    /**
     * vehicleDriver必须包含主键ID
     * @param vehicleDriver
     * @throws Exception
     */
    public void updateById(VehicleDriver vehicleDriver) throws Exception{
        if (null == vehicleDriver.getId()) {
            throw new Exception("车辆与司机信息修改失败，未正确获取ID");
        }
        addInfobyId(vehicleDriver);
        uniqueValidate(vehicleDriver);
        vehicleDriver.setUpdatedBy("ceshi");
        vehicleDriver.setUpdatedAt(new Date());
        vehicleDriverMapper.updateByPrimaryKeySelective(vehicleDriver);
    }

    public void disable(String[] ids) throws Exception{
        for(String id : ids){
            if (null == id) {
                throw new Exception("车辆与司机信息修改失败，未正确获取ID");
            }
            VehicleDriver vehicleDriver = new VehicleDriver();
            vehicleDriver.setStatus(ExVehicleDriver.STATUS_DISABLED);
            vehicleDriver.setUpdatedBy("ceshi");
            vehicleDriver.setUpdatedAt(new Date());
            vehicleDriver.setId(Long.parseLong(id));
            vehicleDriverMapper.updateByPrimaryKeySelective(vehicleDriver);
        }
    }

    private void uniqueValidate(VehicleDriver vehicleDriver) throws Exception{
        VehicleDriverExample e = new VehicleDriverExample();
        VehicleDriverExample.Criteria c = e.createCriteria();
        c.andVIdEqualTo(vehicleDriver.getvId()).andStatusEqualTo(ExVehicleDriver.STATUS_ENABLED);
        if (null != vehicleDriver.getId()) {
            c.andIdNotEqualTo(vehicleDriver.getId());
        }
        List<VehicleDriver> ExistedVehicleDrivers = vehicleDriverMapper.selectByExample(e);
        if (!ExistedVehicleDrivers.isEmpty()) {
            throw new Exception("车牌号:"+vehicleDriver.getvNo()+
                    " 与司机:"+vehicleDriver.getdName()+" 的对应关系已存在。");
        }
    }
}
