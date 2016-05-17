package com.hoperun.tms.controller.base;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.VehicleDriverExample;
import com.hoperun.tms.bean.base.extend.ExVehicleDriver;
import com.hoperun.tms.controller.common.ControllerUtil;
import com.hoperun.tms.service.base.VehicleDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by 16030117 on 2016/3/31.
 */
@Controller
@RequestMapping("/vehicleDriver")
public class VehicleDriverController {

    @Autowired
    VehicleDriverService vehicleDriverService;
    /**
     * 司机管理页面
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ControllerUtil.paramsToAttrs(request);
        int pageNumber = 1;
        int pageSize = 10;
        if(request.getParameter("page")!= null){
            pageNumber =  Integer.parseInt(request.getParameter("page").toString());
            pageSize = Integer.parseInt(request.getParameter("rows").toString());
        }else{
            pageSize=1000;
        }
        VehicleDriverExample e = new VehicleDriverExample();
        VehicleDriverExample.Criteria criteria = e.createCriteria();
        criteria.andStatusEqualTo(ExVehicleDriver.STATUS_ENABLED);
        String vNo = request.getParameter("vNo");
        String dName = request.getParameter("dName");
        if (!StringUtil.isEmpty(vNo)) {
            criteria.andVNoLike("%"+vNo+"%");
        }
        if (!StringUtil.isEmpty(dName)) {
            criteria.andDNameLike("%"+dName+"%");
        }
        e.setOrderByClause("UPDATED_AT desc");

		Map map = vehicleDriverService.getDriverListPaginate(e, pageNumber, pageSize);
        HtmlUtil.writerSuccessJson(response,map);
    }

    @RequestMapping(value="/oper")
    public void vehicleOper(HttpServletRequest request,HttpServletResponse response) throws Exception {
        try {
			String oper = request.getParameter("oper");
			if("add".equals(oper)){
			    ExVehicleDriver exVehicleDriver = new ExVehicleDriver();
			    exVehicleDriver.fromRequestParams(request);
                exVehicleDriver.setStatus(ExVehicleDriver.STATUS_ENABLED);
			    vehicleDriverService.add(exVehicleDriver);
			}else if("delete".equals(oper)){
			    String[] ids = request.getParameter("id").split(",");
                vehicleDriverService.disable(ids);
			}else if("update".equals(oper)){
                VehicleDriverExample e = new VehicleDriverExample();
                e.createCriteria().andIdEqualTo(Long.parseLong(request.getParameter("id")));
				ExVehicleDriver exVehicleDriver = new ExVehicleDriver();
			    exVehicleDriver.fromRequestParams(request);
			    vehicleDriverService.updateById(exVehicleDriver);
			}
			HtmlUtil.writerSuccessJson(response);
		} catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response, e.getMessage());
		}
    }
}
