package com.hoperun.tms.controller.transportationPlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoperun.tms.bean.base.DriverExample;
import com.hoperun.tms.bean.base.VehicleExample;
import com.hoperun.tms.bean.customer.extend.ExDeliveryBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.tms.bean.base.Driver;
import com.hoperun.tms.bean.base.Vehicle;
import com.hoperun.tms.bean.customer.DeliveryBill;
import com.hoperun.tms.bean.customer.DeliveryBillTmp;
import com.hoperun.tms.bean.customer.DeliveryBillTmpExample;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.service.base.DriverService;
import com.hoperun.tms.service.base.VehicleService;
import com.hoperun.tms.service.customer.DeliveryBillService;
import com.hoperun.tms.service.customer.DeliveryBillTmpService;
import com.hoperun.tms.service.customer.WaybillService;

@Controller
@RequestMapping("/vehicleConfirmation")
public class VehicleConfirmationController {

    @Autowired
    DeliveryBillTmpService deliveryBillTmpService;
    
    @Autowired
	WaybillService  waybillService;
    
    @Autowired
    DeliveryBillService deliveryBillService;
    
    @Autowired
	DriverService  driverService;
    
    @Autowired
    VehicleService vehicleService;
    
    /**
     * 派车确认页面
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/query")
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	String driverId = request.getParameter("driverId");
    	
        int pageNumber = 1;
        int pageSize = 10;
        if(request.getParameter("page")!= null){
            pageNumber =  Integer.parseInt(request.getParameter("page").toString());
            pageSize = Integer.parseInt(request.getParameter("rows").toString());
        }else{
            pageSize=1000;
        }
        
        //根据司机id查找数据 
    	DeliveryBillTmpExample deliveryBillTmpExample = new DeliveryBillTmpExample();
    	 if(driverId != null && !"".equals(driverId)){
         	DeliveryBillTmpExample.Criteria deliveryBillcriteria = deliveryBillTmpExample.createCriteria();
         	deliveryBillcriteria.andDIdEqualTo(Long.valueOf(driverId));
         }
    	List<DeliveryBillTmp> deliveryBillTmpList =
				deliveryBillTmpService.getDeliveryBillTmpList(deliveryBillTmpExample);
    	
    	Map map = null;
    	
    	//查找具体的运单
        WaybillExample waybill = new WaybillExample();
    	if(deliveryBillTmpList != null && deliveryBillTmpList.size() > 0){
    		WaybillExample.Criteria  waybillcriteria = waybill.createCriteria();
        	List<String> wNoList = new ArrayList<String>();
        	for(DeliveryBillTmp d: deliveryBillTmpList){
        		wNoList.add(d.getwNo());
        	}
        	waybillcriteria.andNoIn(wNoList);
        	map = waybillService.getWaybillListPaginate(waybill, pageNumber, pageSize);
    	}
		
        HtmlUtil.writerSuccessJson(response,map);
    }

    @RequestMapping(value="/vehicleConfirmationOper")
    public void vehicleOper(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String oper = request.getParameter("oper");
        String ids[] = request.getParameter("id").split(",");
        List<Long> idList = new ArrayList<Long>();
        for(String id : ids){
        	idList.add(Long.valueOf(id));
        }
        if(ids.length > 0){
        	DeliveryBillTmpExample deliveryBillTmpExample = new DeliveryBillTmpExample();
    		DeliveryBillTmpExample.Criteria deliveryBillcriteria = deliveryBillTmpExample.createCriteria();
            deliveryBillcriteria.andIdIn(idList);
	        if("delete".equals(oper)){ //临时表删除数据
	        	deliveryBillTmpService.delDeliveryBillTmpById(deliveryBillTmpExample);
	        }else if("confrimVehicle".equals(oper)){ //确认派车
	        	List<DeliveryBillTmp> DeliveryBillTmpList = deliveryBillTmpService.getDeliveryBillTmpList(deliveryBillTmpExample);

	        	String driverId = request.getParameter("driverId");//司机id
	        	String vehicleId = request.getParameter("vehicleId");//车辆id

				Driver r = driverService.getDriverById(Long.valueOf(driverId));
				Vehicle v = vehicleService.queryById(Long.valueOf(vehicleId));

	        	if(r != null && v != null){
	        		for(DeliveryBillTmp deliveryBillTmp : DeliveryBillTmpList){
	        			DeliveryBill deliveryBill = new DeliveryBill();
	        			deliveryBill.setNo(deliveryBillTmp.getwNo());
	        			deliveryBill.setvId(v.getId());
	        			deliveryBill.setvNo(v.getvNo());
	        			deliveryBill.setdId(r.getId());
	        			deliveryBill.setdName(r.getName());
	        			deliveryBill.setStatus(ExDeliveryBill.STATUS_CREATED);
	        			deliveryBillService.insertDeliveryBill(deliveryBill);
	        			
	        			
	        			//运单更改状态
	        			ExWaybill waybill = new ExWaybill();
	        			waybill.setStatus(ExWaybill.STATUS_TRUCK_DELIVERING);
	        			
	        			WaybillExample waybillExample = new WaybillExample();
	        			waybillExample.createCriteria().andNoEqualTo(deliveryBillTmp.getwNo());
	        			
	        			waybillService.updateWaybill(waybill);
	        		}
	        		
	        		deliveryBillTmpService.delDeliveryBillTmpById(deliveryBillTmpExample); //完成后在临时表里面删除
	        	}
	        }
        }
        query(request, response);
    }

    /**
     * 页面上司机列表和车辆列表
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping(value = "/Options")
	public void getSenderList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DriverExample de = new DriverExample();
		de.createCriteria().andCerCarTypeEqualTo("1");
		List<Driver> driverList = driverService.getDriverList(de);

		VehicleExample ve = new VehicleExample();
		ve.createCriteria().andVTypeEqualTo("1").andStatusEqualTo("1");
		List<Vehicle> vehicleList = vehicleService.queryByList(ve);
		
		Map map = new HashMap();
		map.put("driverList", driverList);
		map.put("vehicleList", vehicleList);
		
		HtmlUtil.writerSuccessJson(response,map);
	}

}
