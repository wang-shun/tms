package com.hoperun.tms.controller.transportationPlan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Driver;
import com.hoperun.tms.bean.base.DriverExample;
import com.hoperun.tms.bean.base.DriverExample.Criteria;
import com.hoperun.tms.bean.customer.DeliveryBill;
import com.hoperun.tms.bean.customer.DeliveryBillExample;
import com.hoperun.tms.bean.customer.Waybill;
import com.hoperun.tms.bean.customer.WaybillLog;
import com.hoperun.tms.bean.customer.WaybillLogExample;
import com.hoperun.tms.bean.customer.WaybillPowercar;
import com.hoperun.tms.bean.customer.WaybillPowercarExample;
import com.hoperun.tms.constants.ExWaybillLogConstants;
import com.hoperun.tms.service.base.DriverService;
import com.hoperun.tms.service.customer.DeliveryBillService;
import com.hoperun.tms.service.customer.WayBilllLogService;
import com.hoperun.tms.service.customer.WaybillPowercarService;
import com.hoperun.tms.service.customer.WaybillService;

@Controller
@RequestMapping("/waybillTransport")
public class WaybillTransportDetailController {
    
    @Autowired
	WaybillService  waybillService;
    
    @Autowired
    WayBilllLogService  waybillLogService;
    
    @Autowired
    WaybillPowercarService  wayBillPowercarService;
    
    @Autowired
    DeliveryBillService  deliveryBillService;
    
    @Autowired
    DriverService  driverService;
    
    /**
     * 查询运单运输详情
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/queryDetail")
    public void queryDetail(HttpServletRequest request, HttpServletResponse response,@RequestBody Map map) throws Exception {
    	String id = (String) map.get("id");
		Waybill waybill = waybillService.selectWaybillById(Long.parseLong(id));
    	WaybillLogExample waybillEx = new WaybillLogExample();
		WaybillLogExample.Criteria criteria = waybillEx.createCriteria();
		criteria.andWNoEqualTo(waybill.getNo());
		criteria.andStatusEqualTo("1");
		waybillEx.setOrderByClause("ID");
    	List<WaybillLog> waybillLogList= waybillLogService.selectWaybillLogByWno(waybillEx);
		if(waybillLogList.isEmpty()){
			throw new Exception("运单尚无任何跟踪信息。");
		}
    	String wStatus = waybillLogList.get(waybillLogList.size()-1).getwStatus();
    	Map resultMap = new HashMap();
    	resultMap.put("waybillLogList", waybillLogList);
    	resultMap.put("waybill", waybill);
    	if(!ExWaybillLogConstants.W_STATUS_CONFIRMED.equals(wStatus) 
    			&& !ExWaybillLogConstants.W_STATUS_CART_RECEIVE.equals(wStatus)
    			&& !ExWaybillLogConstants.W_STATUS_CANCEL.equals(wStatus)){
    		queryDriverInfo(waybill.getScanTel(), resultMap);
    	}
    	/*else if(ExWaybillLogConstants.W_STATUS_ELECTROMBILE_RECEIVE.equals(wStatus) ||ExWaybillLogConstants.W_STATUS_SIGN.equals(wStatus)){
    		queryDeliveryBill(waybill.getDeliveryNo(), resultMap);
    		//queryWaybillPowercar(no, resultMap);
    	}*/
        HtmlUtil.writerSuccessJson(response,resultMap);
    }

	private void andWNoEqualTo(String no) {
		// TODO Auto-generated method stub
		
	}

	private void queryWaybillPowercar(String no, Map resultMap) throws Exception {
		WaybillPowercarExample wbpEx = new WaybillPowercarExample();
		WaybillPowercarExample.Criteria wbpExCriteria = wbpEx.createCriteria();
		wbpExCriteria.andWNoEqualTo(no);
		List<WaybillPowercar> waybillPowercarList= wayBillPowercarService.selectWaybillPowercarByWno(wbpEx);
		if(!waybillPowercarList.isEmpty()){
			resultMap.put("waybillPowercar", waybillPowercarList.get(0));
		}else{
			resultMap.put("waybillPowercar", null);
		}
	}

	private void queryDriverInfo(String iphoneNum,Map resultMap) throws Exception {
		if(StringUtil.isEmpty(iphoneNum)){
			throw new Exception("司机手机号码不存在。");
		}
		Map driverMap = driverService.getDriverAndVno(iphoneNum);
		if(driverMap!=null){
			resultMap.put("driver", driverMap);
		}else{
			resultMap.put("driver", null);
		}
	}
	private void queryDeliveryBill(String no, Map resultMap) throws Exception {
		if(StringUtil.isEmpty(no)){
			throw new Exception("配送单号为空，无法获取跟踪信息。");
		}
		DeliveryBillExample dbEx = new DeliveryBillExample();
		DeliveryBillExample.Criteria dbExCriteria = dbEx.createCriteria();
		dbExCriteria.andNoEqualTo(no);
		List<DeliveryBill> deliveryBillList = deliveryBillService.getDeliveryBillList(dbEx);
		if(!deliveryBillList.isEmpty()){
			resultMap.put("deliveryBill", deliveryBillList.get(0));
		}else{
			resultMap.put("deliveryBill", null);
		}
	}
    

}
