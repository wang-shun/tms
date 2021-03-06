/**
 * 
 */
/**
 * @author 16020025
 *
 */
package com.hoperun.tms.controller.financialSettlement;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoperun.framework.utils.DateUtil;
import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.WaybillExample.Criteria;
import com.hoperun.tms.service.customer.WaybillService;


@Controller
@RequestMapping("/receivable")
public class ReceivableController {
	
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
		String tFeeStatus = request.getParameter("tFeeStatus");
		String createdAtStartDate = request.getParameter("createdAtStartDate");
		String createdAtEndDate = request.getParameter("createdAtEndDate");
		String dName = request.getParameter("dName");
		WaybillExample waybillExample = new WaybillExample();
		Criteria criteria = waybillExample.createCriteria();
		if(!StringUtil.isEmpty(dName)){
			criteria.andDNameLike("%"+dName+"%");
		}
		if(!StringUtil.isEmpty(tFeeStatus)){
			criteria.andTFeeStatusEqualTo(tFeeStatus);
		}
		if(!StringUtil.isEmpty(createdAtStartDate)){
			criteria.andCreatedAtGreaterThanOrEqualTo(DateUtil.parseFormatIso8601Date(createdAtStartDate));
		}
		if(!StringUtil.isEmpty(createdAtEndDate)){
			criteria.andCreatedAtLessThanOrEqualTo(DateUtil.parseFormatIso8601Date(createdAtEndDate));
		}
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
	 * 运单查询
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryAll")
	public void queryAll(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String tFeeStatus = request.getParameter("tFeeStatus");
		String createdAtStartDate = request.getParameter("createdAtStartDate");
		String createdAtEndDate = request.getParameter("createdAtEndDate");
		String dName = request.getParameter("dName");
		WaybillExample waybillExample = new WaybillExample();
		Criteria criteria = waybillExample.createCriteria();
		if(!StringUtil.isEmpty(dName)){
			criteria.andDNameLike("%"+dName+"%");
		}
		if(!StringUtil.isEmpty(tFeeStatus)){
			criteria.andTFeeStatusEqualTo(tFeeStatus);
		}
		if(!StringUtil.isEmpty(createdAtStartDate)){
			criteria.andCreatedAtGreaterThanOrEqualTo(DateUtil.parseFormatIso8601Date(createdAtStartDate));
		}
		if(!StringUtil.isEmpty(createdAtEndDate)){
			criteria.andCreatedAtLessThanOrEqualTo(DateUtil.parseFormatIso8601Date(createdAtEndDate));
		}
		waybillExample.setOrderByClause("UPDATED_AT desc");
		Map map = waybillService.getWaybillList(waybillExample);
		HtmlUtil.writerSuccessJson(response,map);
	}
	
	/**
	 * 确认收款
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/confirmSettlementStatus")
	@ResponseBody
	public void confirmSettlementStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			String oper = request.getParameter("oper");
			String[] ids = request.getParameter("id").split(",");
			waybillService.confirmSettlementStatus(ids);
			HtmlUtil.writerSuccessJson(response,null);
		} catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response,e.getMessage());
		}
	}

}
