package com.hoperun.tms.service.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Vehicle;
import com.hoperun.tms.bean.base.VehicleExample;
import com.hoperun.tms.bean.base.extend.ExVehicle;
import com.hoperun.tms.mapper.base.extend.ExVehicleMapper;
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
public class VehicleService {
    @Autowired
    private ExVehicleMapper exVehicleMapper;

    public Vehicle queryById(Long id) {
        return exVehicleMapper.selectByPrimaryKey(id);
    }

    public List<Vehicle> queryByList(VehicleExample e) {
        return exVehicleMapper.selectByExample(e);
    }

    public Map getVehicleListPaginate(VehicleExample e, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        Page startPage = PageHelper.startPage(pageNumber, pageSize);
        List<ExVehicle> vehicleList = exVehicleMapper.selectByExample(e);
        Long totalrecords = startPage.getTotal();
        map.put("vehicleList", vehicleList);
        map.put("currpage", pageNumber);
        map.put("totalpages", totalrecords == 0 ? 1 : (totalrecords - 1) / pageSize + 1);
        map.put("totalrecords", totalrecords);
        return map;
    }

    public void add(Vehicle vehicle) throws Exception {
        uniqueValidate(vehicle);
        nullableValidate(vehicle);
        vehicle.setCreatedBy("ceshi");
        vehicle.setCreatedAt(new Date());
        vehicle.setUpdatedBy("ceshi");
        vehicle.setUpdatedAt(new Date());
        exVehicleMapper.insert(vehicle);
    }

    public void disable(String[] ids) throws Exception {
        for(String id : ids){
            if (null == id) {
                throw new Exception("车辆信息删除失败，未正确获取车辆ID");
            }
            Vehicle vehicle = new Vehicle();
            vehicle.setStatus(ExVehicle.STATUS_DISABLED);
            vehicle.setUpdatedBy("ceshi");
            vehicle.setUpdatedAt(new Date());
            vehicle.setId(Long.parseLong(id));
            exVehicleMapper.updateByPrimaryKeySelective(vehicle);
        }
    }

    public void updateMultiVehicles(Vehicle vehicle, VehicleExample e) throws Exception {
        vehicle.setUpdatedBy("ceshi");
        vehicle.setUpdatedAt(new Date());
        exVehicleMapper.updateByExampleSelective(vehicle, e);
    }

    /**
     * vehicle必须包含主键ID
     * @param vehicle
     * @throws Exception
     */
    public void updateById(Vehicle vehicle) throws Exception {
        if (null == vehicle.getId()) {
            throw new Exception("车辆信息更新失败，未正确获取车辆ID");
        }
        uniqueValidate(vehicle);
        nullableValidate(vehicle);
        vehicle.setUpdatedBy("ceshi");
        vehicle.setUpdatedAt(new Date());
        exVehicleMapper.updateByPrimaryKeySelective(vehicle);
    }

    private void uniqueValidate(Vehicle vehicle) throws Exception {
        VehicleExample e = new VehicleExample();
        VehicleExample.Criteria c1 = e.createCriteria();
        VehicleExample.Criteria c2 = e.or();
        c1.andVNoEqualTo(vehicle.getvNo()).andStatusEqualTo(ExVehicle.STATUS_ENABLED);
        c2.andNoEqualTo(vehicle.getNo()).andStatusEqualTo(ExVehicle.STATUS_ENABLED);
        if (null != vehicle.getId()) {
            c1.andIdNotEqualTo(vehicle.getId());
            c2.andIdNotEqualTo(vehicle.getId());
        }
        List<Vehicle> vehicles = exVehicleMapper.selectByExample(e);
        if (!vehicles.isEmpty()) {
            for(Vehicle exsitedVehicle : vehicles){
                if (!StringUtil.isEmpty(exsitedVehicle.getvNo())
                        && exsitedVehicle.getvNo().equals(vehicle.getvNo())) {
                    throw new Exception("车牌号:"+vehicle.getvNo()+"在车辆列表中已存在。");
                } else if (!StringUtil.isEmpty(exsitedVehicle.getNo())
                        && exsitedVehicle.getNo().equals(vehicle.getNo())) {
                    throw new Exception("车辆代码:"+vehicle.getNo()+"在车辆列表中已存在。");
                }
            }
        }
    }

    private void nullableValidate(Vehicle vehicle) throws Exception {
        if(ExVehicle.TRUCK.equals(vehicle.getvType())){
            if(null == vehicle.getvWeight()){
                throw new Exception("卡车信息缺少载货重量。");
            }
        }
    }
}
