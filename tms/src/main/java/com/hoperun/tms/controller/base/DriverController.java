/**
 * 基础数据-司机管理
 * @author 15120071
 * 2016-3-21
 */
package com.hoperun.tms.controller.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoperun.tms.bean.base.DriverExample;
import com.hoperun.tms.bean.base.extend.ExDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoperun.framework.redis.User;
import com.hoperun.framework.redis.UserDAOImpl;
import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.JacksonJsonUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Driver;
import com.hoperun.tms.service.base.DriverService;


@Controller
@RequestMapping("/driver")
public class DriverController {
	
	@Autowired
	DriverService  driverService;
	
	@Autowired
	public UserDAOImpl userDAOImpl;
	
	/**
	 * 司机管理页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/query")
	public void query(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		String name = request.getParameter("name");
		DriverExample e = new DriverExample();
		DriverExample.Criteria criteria = e.createCriteria();
		criteria.andStatusEqualTo("1");
		if(!StringUtil.isEmpty(name)){
			criteria.andNameLike("%"+name+"%");
		}
		int pageNumber = 1;
		int pageSize = 10;
		if(request.getParameter("page")!= null){
			pageNumber =  Integer.parseInt(request.getParameter("page").toString());
			pageSize = Integer.parseInt(request.getParameter("rows").toString());
		}else{
			pageSize=1000;
		}
		e.setOrderByClause("UPDATED_AT desc");
		Map map = driverService.getDriverListPaginate(e, pageNumber, pageSize);
		HtmlUtil.writerSuccessJson(response,map);
	}
	/**
	 * 司机管理页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryAll")
	public String driver(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String name = request.getParameter("name");
		DriverExample e = new DriverExample();
		DriverExample.Criteria criteria = e.createCriteria();
		if(!StringUtil.isEmpty(name)){
			criteria.andNameLike("%"+name+"%");
			request.setAttribute("name", name);
		}
		List<Driver> dlist = driverService.getDriverList(e);
		request.setAttribute("driverList", dlist);
		return "/base/driver";
	}
	/**
	 * 用户操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/driverOper")
	@ResponseBody
	public void driverOper(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String oper = request.getParameter("oper");
		try {
			if("driverAdd".equals(oper)){
				ExDriver exDriver = new ExDriver();
				exDriver.setFromRequestParams(request);
				if("2".equals(exDriver.getCerCarType())){
					exDriver.setCerType("");
				}
				exDriver.setStatus(ExDriver.STATUS_ENABLED);//有效
				driverService.insertDriver(exDriver);
			}else if("driverDel".equals(oper)){
				String[] ids = request.getParameter("id").split(",");
				driverService.disable(ids);
			}else if("driverUpdate".equals(oper)){
				String id = request.getParameter("id");
				ExDriver exDriver = new ExDriver();
				exDriver.setFromRequestParams(request);
				exDriver.setId(Long.parseLong(id));
				if("2".equals(exDriver.getCerCarType())){
					exDriver.setCerType("");
				}
				exDriver.setStatus(ExDriver.STATUS_ENABLED);//有效
				driverService.updateById(exDriver);
			}
			HtmlUtil.writerSuccessJson(response);
		} catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response, e.getMessage());
		}
	}

    @RequestMapping(value = "/driverUpdate")
    @ResponseBody
    public void driverUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
			String dataList = request.getParameter("dataList");
			List<LinkedHashMap> receiverList = (List<LinkedHashMap>) JacksonJsonUtil.jsonToBean(dataList, new ArrayList().getClass());
			driverService.updateBatch(receiverList);
			HtmlUtil.writerSuccessJson(response);
		} catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response, e.getMessage());
		}
    }
	
	@RequestMapping(value = "/driverOptions")
	public void getDriverList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String status = request.getParameter("status");
		DriverExample e = new DriverExample();
		e.setOrderByClause("NAME");
		if (!StringUtil.isEmpty(status)) {
			e.createCriteria().andStatusEqualTo(ExDriver.STATUS_ENABLED);
		}
		List<Driver> driverList = driverService.getDriverList(e);
		Map map = new HashMap();
		map.put("driverList", driverList);
		HtmlUtil.writerSuccessJson(response,map);
	}
 }
