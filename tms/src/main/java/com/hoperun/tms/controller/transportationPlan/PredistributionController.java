package com.hoperun.tms.controller.transportationPlan;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.WaybillExample.Criteria;
import com.hoperun.tms.bean.customer.extend.ExDeliveryBill;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.service.base.DriverService;
import com.hoperun.tms.service.base.VehicleService;
import com.hoperun.tms.service.customer.DeliveryBillService;
import com.hoperun.tms.service.customer.DeliveryBillTmpService;
import com.hoperun.tms.service.customer.WaybillService;
import com.hoperun.tms.util.DateUtils;

@Controller
@RequestMapping("/predistribution")
public class PredistributionController {

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
     * 预分配查询
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/query")
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String rArea = request.getParameter("rArea");
		String deliveryStatus = request.getParameter("deliveryStatus");
		String deliveryNo = request.getParameter("deliveryNo");
		WaybillExample waybillExample = new WaybillExample();
		Criteria criteria = waybillExample.createCriteria();
		if(!StringUtil.isEmpty(rArea)){
			criteria.andRAreaEqualTo(rArea);
		}
		if("1".equals(deliveryStatus)){
			criteria.andDeliveryNoIsNotNull();
		}else if("2".equals(deliveryStatus)){
			criteria.andDeliveryNoIsNull();
		}
		if(StringUtil.isEmpty(deliveryNo)){
			criteria.andStatusEqualTo("10");
		}else{
			criteria.andDeliveryNoEqualTo(deliveryNo);
		}
		aaaa
		waybillExample.setOrderByClause("UPDATED_AT desc");
		int pageNumber = 1;
		int pageSize = 10;
		if(request.getParameter("page")!= null){
			pageNumber =  Integer.parseInt(request.getParameter("page").toString());
			pageSize = Integer.parseInt(request.getParameter("rows").toString());
		}else{
			pageSize=1000;
		}
		Map map = waybillService.getWaybillListPaginate(waybillExample, pageNumber, pageSize);
		HtmlUtil.writerSuccessJson(response,map);
    }

    @RequestMapping(value="/predistributionDeliveryBill")
    public void predistributionDeliveryBill(HttpServletRequest request,HttpServletResponse response) throws Exception {
	   try{
	    	String[] ids = request.getParameter("id").split(",");
	    	ExDeliveryBill exDeliveryBill = new ExDeliveryBill();
	        exDeliveryBill.setNo(DateUtils.curDateStr8() + DateUtils.curTimeStr6());
	        exDeliveryBill.setStatus(ExDeliveryBill.STATUS_CREATED);
	        
	        ExWaybill exWaybill = new ExWaybill();
	        exWaybill.setDeliveryId(exDeliveryBill.getId());
	        exWaybill.setDeliveryNo(exDeliveryBill.getNo());
	        
	        deliveryBillService.createPredistributionDeliveryBillEx(exDeliveryBill, ids);
	    	HtmlUtil.writerSuccessJson(response,null);
		} catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response,"预分配失败，原因："+e.getMessage());
		}
        
    }
    @RequestMapping(value="/cancelPredistribution")
    public void cancelPredistributionDeliveryBill(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	try{
    		String[] ids = request.getParameter("id").split(",");
    		
    		deliveryBillService.cancelPredistributionDeliveryBillEx(ids);
    		HtmlUtil.writerSuccessJson(response,null);
    	} catch (Exception e) {
    		e.printStackTrace();
    		HtmlUtil.writerFailJson(response,"撤销失败，原因："+e.getMessage());
    	}
    	
    }

}
