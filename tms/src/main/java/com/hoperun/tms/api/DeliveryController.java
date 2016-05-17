package com.hoperun.tms.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.tms.bean.base.Driver;
import com.hoperun.tms.bean.base.DriverExample;
import com.hoperun.tms.bean.customer.Waybill;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.WaybillExample.Criteria;
import com.hoperun.tms.bean.customer.WaybillLog;
import com.hoperun.tms.bean.customer.WaybillLogExample;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.service.base.DriverService;
import com.hoperun.tms.service.customer.WaybillService;

/**
 * 小车接口
 * @author jerome
 *
 */
@Controller
@RequestMapping("/api/DeliverQuery")
public class DeliveryController {

		@Autowired
		WaybillService  waybillService;
		
		@Autowired
		DriverService  driverService;
		
		/**
		 * (status 00 创建 10 确认 20 大车运输 30 小车运输 40 签收)
		 * (sacnStatus 00 未扫描 10 大车扫描未确认 20 扫描确认)
		 * @param mobile
		 * @param id
		 * @param request
		 * @param response
		 * @throws Exception
		 */
		@RequestMapping(value="/Scan/{mobile}/{no}/{timestamp}",method=RequestMethod.GET)
		public void Scan(@PathVariable String mobile,@PathVariable String no,@PathVariable String timestamp,HttpServletRequest request,HttpServletResponse response) throws Exception {
			Map map = new HashMap();
			try {
				
				WaybillExample waybillExample = new WaybillExample();
				Criteria criteria = waybillExample.createCriteria();
				criteria.andNoEqualTo(no);
				ExWaybill exWaybill = new ExWaybill();
			    List<ExWaybill> list = waybillService.getAll(waybillExample);
			    if(list.size()>0){
			    	exWaybill = list.get(0);
			    	map.put("area", exWaybill.getdCounty());//获取城区'
			    	if(Integer.parseInt(exWaybill.getStatus())>=30){
			    		map.put("status", -1);
						map.put("msg", "此运单[" + no + "]已经处理过，状态为：" + exWaybill.getStatus());
						HtmlUtil.writerJsonp(request,response, map);
						return;
			    	}

			    }else{
			    	map.put("status", -1);
					map.put("msg", "无此运单!" + no);
					HtmlUtil.writerJsonp(request,response, map);
					return;
			    }

		    	//小车扫码状态更新
				exWaybill.setStatus(ExWaybill.STATUS_POWERCAR_DELIVERING);
				Driver driver = getDriverId(mobile);
				exWaybill.setvId(driver.getId());
				exWaybill.setvNo(driver.getCerNo());
				//exWaybill.setvDriverId(driver.getId());
				exWaybill.setvDriverName(driver.getName());
				exWaybill.setvScanTel(mobile);
				exWaybill.setSignTime(null);
				exWaybill.setUpdatedAt(new Date());
				
				WaybillLog waybillLog = new WaybillLog();
				waybillLog.setwId(exWaybill.getId());
				waybillLog.setwNo(exWaybill.getNo());
				waybillLog.setwLog("运单操作:小车扫码,单号【"+no+"】,操作人:" + exWaybill.getvDriverName() + exWaybill.getvScanTel() + ",路线：上海" + exWaybill.getdArea() + exWaybill.getdPoint() + ",收货方:" + exWaybill.getrName() + exWaybill.getrTel() + ",收货地:" + exWaybill.getrAddress());
				waybillLog.setStatus("1");
				waybillLog.setwStatus("4");
				waybillService.sacnUpdate(exWaybill,waybillExample,waybillLog);
				
				//获取运单信息
				map.put("exWaybill", exWaybill);
				
				//获取条数
				WaybillExample waybillExample_1 = new WaybillExample();
				Criteria criteria_1 = waybillExample_1.createCriteria();
				criteria_1.andVScanTelEqualTo(mobile);
				criteria_1.andStatusEqualTo(ExWaybill.STATUS_POWERCAR_DELIVERING);
				map.put("wayBillCount", waybillService.countWayBill(waybillExample_1));
				map.put("status", 0);
				map.put("msg", "小车运单扫码成功!");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", -1);
				map.put("msg", "小车运单扫码失败!" + e.getMessage());
				HtmlUtil.writerJsonp(request,response, map);
				return;
			}
			
			HtmlUtil.writerJsonp(request,response,map);
		}
		
		/**
		 * 小车运单扫码回退
		 * @param mobile
		 * @param no
		 * @param request
		 * @param response
		 * @throws Exception
		 */
		@RequestMapping(value="/ScanRollback/{mobile}/{no}/{timestamp}",method=RequestMethod.GET)
		public void ScanRollback(@PathVariable String mobile,@PathVariable String no,@PathVariable String timestamp,HttpServletRequest request,HttpServletResponse response) throws Exception {
			Map map = new HashMap();
			try {
				WaybillExample waybillExample = new WaybillExample();
				Criteria criteria = waybillExample.createCriteria();
				criteria.andNoEqualTo(no);
				
			    List<ExWaybill> list = waybillService.getAll(waybillExample);
			    if(list.size()>0){
			    	ExWaybill exWaybill = list.get(0);
			    	if(!exWaybill.getStatus().equals(ExWaybill.STATUS_POWERCAR_DELIVERING)){
			    		map.put("status", -1);
						map.put("msg", "运单状态不能撤销处理!" + exWaybill.getStatus());
						HtmlUtil.writerJsonp(request,response, map);
						return;
			    	}
			    }else{
			    	map.put("status", -1);
					map.put("msg", "无此运单!" + no);
					HtmlUtil.writerJsonp(request,response, map);
					return;
			    }

				criteria.andStatusEqualTo(ExWaybill.STATUS_POWERCAR_DELIVERING);
				ExWaybill exWaybill = new ExWaybill();
				exWaybill.setStatus(ExWaybill.STATUS_TRUCK_DELIVERING);
				exWaybill.setvScanTel(mobile);
				
				WaybillLogExample waybillLogExample = new WaybillLogExample();
				com.hoperun.tms.bean.customer.WaybillLogExample.Criteria criteria_log = waybillLogExample.createCriteria();
				criteria_log.andWNoEqualTo(no);
				criteria_log.andWStatusEqualTo(ExWaybill.BILLLOG_4);
				waybillService.sacnRollBack(exWaybill,waybillExample,waybillLogExample);

				map.put("status", 0);
				map.put("msg", "撤销成功!");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", -1);
				map.put("msg", "撤销失败!" + e.getMessage());
				HtmlUtil.writerJsonp(request,response, map);
				return;
			}
			
			HtmlUtil.writerJsonp(request,response,map);
		}
		
		/**
		 * 大车下车
		 * @param mobile
		 * @param id
		 * @param request
		 * @param response
		 * @throws Exception
		 */
		@RequestMapping(value="/confirmWayBill/{mobile}/{wayBillNo}/{deliveryTerm}/{timestamp}",method=RequestMethod.GET)
		public void confirmWayBill(@PathVariable String mobile,@PathVariable String wayBillNo,@PathVariable String deliveryTerm,@PathVariable String timestamp,HttpServletRequest request,HttpServletResponse response) throws Exception {
			Map map = new HashMap();
			try {
				WaybillExample waybillExample_2 = new WaybillExample();
				Criteria criteria_2 = waybillExample_2.createCriteria();
				criteria_2.andNoEqualTo(wayBillNo);
				ExWaybill exWaybill_2 = new ExWaybill();
			    List<ExWaybill> list_2 = waybillService.getAll(waybillExample_2);
			    if(list_2.size()>0){
			    	exWaybill_2 = list_2.get(0);
			    	if(Integer.parseInt(exWaybill_2.getStatus())>=25){
			    		map.put("status", -1);
						map.put("msg", "此运单[" + wayBillNo + "]已经处理过，状态为：" + exWaybill_2.getStatus());
						HtmlUtil.writerJsonp(request,response, map);
						return;
			    	}
			    }else{
			    	map.put("status", -1);
					map.put("msg", "此运单" + wayBillNo + "不在下车签收流程!");
					HtmlUtil.writerJsonp(request,response, map);
					return;
			    }
			    
				WaybillExample waybillExample = new WaybillExample();
				Criteria criteria = waybillExample.createCriteria();
		    	criteria.andNoEqualTo(wayBillNo);
				ExWaybill exWaybill = new ExWaybill();
				exWaybill.setStatus(ExWaybill.STATUS_XC);
				exWaybill.setSignTime(new Date());//写入签收时间
				
				WaybillLog waybillLog = new WaybillLog();
				waybillLog.setwId(exWaybill_2.getId());
				waybillLog.setwNo(wayBillNo);
				waybillLog.setwLog("大车下车,到达集散点:"+deliveryTerm+",单号:" + wayBillNo);
				waybillLog.setStatus("1");
				waybillLog.setwStatus(ExWaybill.BILLLOG_3);
				waybillLog.setCreatedAt(new Date());
				waybillLog.setUpdatedAt(new Date());
				waybillService.sacnUpdate(exWaybill, waybillExample, waybillLog);
				
				//获取运单信息
				List<ExWaybill> list = waybillService.getAll(waybillExample);
				if(list.size()>0){
					ExWaybill exWaybill_1 = (ExWaybill)list.get(0);
					map.put("area", exWaybill_1.getdCounty());//获取城区
					
					//发送签收短信
//					SMSClient.send(exWaybill_1.getdTel(), "你发送的快件['"+ exWaybill_1.getOrderNo() +"']已被签收!");//发件人
//					SMSClient.send(exWaybill_1.getrTel(), "快件['"+ exWaybill_1.getOrderNo() +"']已被签收!");//收件人
				}
					
				map.put("status", 0);
				map.put("msg", "签收成功!");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", -1);
				map.put("msg", "签收失败!" + e.getMessage());
				HtmlUtil.writerJsonp(request,response, map);
				return;
			}
			
			HtmlUtil.writerJsonp(request,response,map);
		}
		
		/**
		 * 签收确认
		 * @param mobile
		 * @param id
		 * @param request
		 * @param response
		 * @throws Exception
		 */
		@RequestMapping(value="/Sign/{mobile}/{wayBillNo}",method=RequestMethod.GET)
		public void Sign(@PathVariable String mobile,@PathVariable String wayBillNo,HttpServletRequest request,HttpServletResponse response) throws Exception {
			Map map = new HashMap();
			try {
				WaybillExample waybillExample_2 = new WaybillExample();
				Criteria criteria_2 = waybillExample_2.createCriteria();
				criteria_2.andNoEqualTo(wayBillNo);
				criteria_2.andVScanTelEqualTo(mobile);
				ExWaybill exWaybill_2 = new ExWaybill();
			    List<ExWaybill> list_2 = waybillService.getAll(waybillExample_2);
			    if(list_2.size()>0){
			    	exWaybill_2 = list_2.get(0);
			    	if(Integer.parseInt(exWaybill_2.getStatus())>=40){
			    		map.put("status", -1);
						map.put("msg", "此运单[" + wayBillNo + "]已经处理过，状态为：" + exWaybill_2.getStatus());
						HtmlUtil.writerJsonp(request,response, map);
						return;
			    	}
			    }else{
			    	map.put("status", -1);
					map.put("msg", "此运单" + wayBillNo + "不在签收流程!");
					HtmlUtil.writerJsonp(request,response, map);
					return;
			    }
			    
				WaybillExample waybillExample = new WaybillExample();
				Criteria criteria = waybillExample.createCriteria();
				criteria.andVScanTelEqualTo(mobile);
				criteria.andNoEqualTo(wayBillNo);
				ExWaybill exWaybill = new ExWaybill();
				exWaybill.setStatus(ExWaybill.STATUS_DELIVERED);
				exWaybill.setSignTime(new Date());//写入签收时间
				
				WaybillLog waybillLog = new WaybillLog();
				waybillLog.setwId(exWaybill_2.getId());
				waybillLog.setwNo(wayBillNo);
				waybillLog.setwLog("运单签收确认,单号:" + wayBillNo);
				waybillLog.setStatus("1");
				waybillLog.setwStatus(ExWaybill.BILLLOG_5);
				waybillLog.setCreatedAt(new Date());
				waybillLog.setUpdatedAt(new Date());
				waybillService.sacnUpdate(exWaybill, waybillExample, waybillLog);
				
				//获取运单信息
				List<ExWaybill> list = waybillService.getAll(waybillExample);
				if(list.size()>0){
					ExWaybill exWaybill_1 = (ExWaybill)list.get(0);
					map.put("exWaybill", exWaybill_1);
					
					//发送签收短信
//					SMSClient.send(exWaybill_1.getdTel(), "你发送的快件['"+ exWaybill_1.getOrderNo() +"']已被签收!");//发件人
//					SMSClient.send(exWaybill_1.getrTel(), "快件['"+ exWaybill_1.getOrderNo() +"']已被签收!");//收件人
				}
				
				//获取条数
				WaybillExample waybillExample_1 = new WaybillExample();
				Criteria criteria_1 = waybillExample_1.createCriteria();
				criteria_1.andVScanTelEqualTo(mobile);
				criteria_1.andStatusEqualTo(ExWaybill.STATUS_DELIVERED);
				map.put("wayBillCount", waybillService.countWayBill(waybillExample_1));
				map.put("status", 0);
				map.put("msg", "签收成功!");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", -1);
				map.put("msg", "签收失败!" + e.getMessage());
				HtmlUtil.writerJsonp(request,response, map);
				return;
			}
			
			HtmlUtil.writerJsonp(request,response,map);
		}
		
		private Driver getDriverId(String moblie)throws Exception{
			DriverExample e = new DriverExample();
			e.createCriteria().andContactEqualTo(moblie);
			List<Driver> list = driverService.getDriverList(e);
			if(list.size()==1){
				Driver driver = (Driver)list.get(0);
				return driver;
			}else{
				throw new Exception(" 该手机绑定的司机信息不正确 ，返回条数：" + list.size());
			}
		}
		
		
}
