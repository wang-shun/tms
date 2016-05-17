/**
 * 基础数据-司机管理
 * @author 15120071
 * 2016-3-21
 */
package com.hoperun.tms.controller.customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoperun.framework.utils.DateUtil;
import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.JacksonJsonUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.WaybillExample.Criteria;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.service.customer.WaybillService;
import com.hoperun.tms.util.PrintBill;


@Controller
@RequestMapping("/waybill")
public class WaybillController {
	
	private final static Logger LOGGER = Logger.getLogger(WaybillController.class);
	@Autowired
	WaybillService  waybillService;
	
	/**
	 * 运单查询
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/query")
	public void query(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String no = request.getParameter("no");
		String orderNo = request.getParameter("orderNo");
		String dName = request.getParameter("dName");
		String deliveryType = request.getParameter("deliveryType");
		String status = request.getParameter("status");
		String deliveryStartDate = request.getParameter("deliveryStartDate");
		String deliveryEndDate = request.getParameter("deliveryEndDate");
		String rName = request.getParameter("rName");
		WaybillExample waybillExample = new WaybillExample();
		Criteria criteria = waybillExample.createCriteria();
		if(!StringUtil.isEmpty(no)){
			criteria.andNoEqualTo(no);
		}
		if(!StringUtil.isEmpty(orderNo)){
			criteria.andOrderNoEqualTo(orderNo);
		}
		if(!StringUtil.isEmpty(dName)){
			criteria.andDNameLike("%"+dName+"%");
		}
		if(!StringUtil.isEmpty(deliveryType)){
			criteria.andDeliveryTypeEqualTo(deliveryType);
		}
		if(!StringUtil.isEmpty(rName)){
			criteria.andRNameLike("%"+rName+"%");
		}
		if(!StringUtil.isEmpty(status)){
			String statusArray[] = status.split(",");
			List<String> statusList = Arrays.asList(statusArray);
			criteria.andStatusIn(statusList);
		}
		if(!StringUtil.isEmpty(deliveryStartDate)){
			criteria.andDeliveryDateGreaterThanOrEqualTo(DateUtil.parseFormatIso8601Date(deliveryStartDate));
		}
		if(!StringUtil.isEmpty(deliveryEndDate)){
			criteria.andDeliveryDateLessThanOrEqualTo(DateUtil.parseFormatIso8601Date(deliveryEndDate));
		}
		criteria.andSourceEqualTo("2");//来源是小农
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

	/**
	 * 用户操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/waybillOper")
	@ResponseBody
	public void waybillOper(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String oper = request.getParameter("oper");
		Map map = request.getParameterMap();
		if("updateById".equals(oper)){
			ExWaybill exWaybill = new ExWaybill();
			exWaybill.setFromRequestParams(request);
			waybillService.updateWaybill(exWaybill);
		}
		query(request, response);
	}
	
	/**
	 * 订单确认和作废
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/waybillConfirmOrCancel")
	@ResponseBody
	public void waybillConfirmOrCancel(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			String oper = request.getParameter("oper");
			String[] ids = request.getParameter("id").split(",");
			if ("waybillConfirm".equals(oper)) {
				waybillService.confirmWaybills(ids);
			} else if ("waybillCancel".equals(oper)) {
				waybillService.disableWaybills(ids);
			}
			HtmlUtil.writerSuccessJson(response,null);
		} catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response,e.getMessage());
		}
	}

	@RequestMapping(value="/waybillUpdate")
	@ResponseBody
	public void waybillUpdate(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String rowDatas = request.getParameter("rowDatas");
		Map<String,List<Map>> map = (Map<String,List<Map>>)JacksonJsonUtil.jsonToBean(rowDatas, new HashMap().getClass());
		List<Map> list =map.get("rowDatas");
		for (Map map2 : list) {
			ExWaybill exWaybill = new ExWaybill();
			exWaybill.setFromMap(map2);
			waybillService.updateWaybill(exWaybill);
		}
		query(request, response);
	}

	@RequestMapping(value="/saveOrUpdate")
	@ResponseBody
	public void saveOrUpdate(HttpServletRequest request,HttpServletResponse response,@RequestBody ExWaybill exWaybill) throws Exception {
		try {
			waybillService.insertOrUpdateWaybill(exWaybill);
			Map map = new HashMap();
			map.put("id", exWaybill.getId());
			map.put("no", exWaybill.getNo());
			HtmlUtil.writerSuccessJson(response,map);
		} catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response, "保存出错："+e.getMessage());
		}
	}

	/**
	 * 查询一个运单号详情
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/queryDetail")
	@ResponseBody
	public void queryWaybillDetail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		ExWaybill exWaybill = waybillService.getWaybillById(Long.parseLong(id));
		Map map = new HashMap();
		map.put("waybill", exWaybill);
		HtmlUtil.writerSuccessJson(response,map);
	}
	
	/**
	 * 查询一个运单号详情
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/printbill")
	@ResponseBody
	public void printBill(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String ids = request.getParameter("idstr");
		LOGGER.info("=======================打印参数:" + ids);
		if(ids != null && !"".equals(ids)){
			String idArray[] = ids.split(",");
			List<Long> idList = new ArrayList<Long>();
			for(String id:idArray){
				idList.add(Long.valueOf(id));
			}
			
			WaybillExample waybill = new WaybillExample();
			waybill.createCriteria().andIdIn(idList);
			List<ExWaybill> waybillExs = waybillService.getAll(waybill);
			for(ExWaybill w: waybillExs){
				LOGGER.info("=============start w.no:" + w.getNo());
				try {
					PrintBill printBill = new PrintBill();
					LOGGER.info("==============printBill go" );
					printBill.printBill(w);
				} catch (Exception e) {
					LOGGER.info("================error print:" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
 }
