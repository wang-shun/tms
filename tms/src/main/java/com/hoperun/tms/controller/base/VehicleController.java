package com.hoperun.tms.controller.base;

import com.hoperun.framework.utils.DateUtil;
import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Vehicle;
import com.hoperun.tms.bean.base.VehicleExample;
import com.hoperun.tms.bean.base.extend.ExVehicle;
import com.hoperun.tms.service.base.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 16030117 on 2016/3/31.
 */
@Controller
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;
    /**
     * 司机管理页面
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = 1;
        int pageSize = 10;
        if(request.getParameter("page")!= null){
            pageNumber =  Integer.parseInt(request.getParameter("page").toString());
            pageSize = Integer.parseInt(request.getParameter("rows").toString());
        }else{
            pageSize=1000;
        }
        VehicleExample e = new VehicleExample();
        setQueryConditions(request, e);

		Map map = vehicleService.getVehicleListPaginate(e, pageNumber, pageSize);
        HtmlUtil.writerSuccessJson(response,map);
    }

    private void setQueryConditions(HttpServletRequest request, VehicleExample e) throws Exception {
        VehicleExample.Criteria criteria = e.createCriteria();
        String vNo = request.getParameter("vNo");
        String vType = request.getParameter("vType");
        String status = request.getParameter("status");
        status = "X".equals(status) ? "" : (StringUtil.isEmpty(status) ? ExVehicle.STATUS_ENABLED : status);
        String driverType = request.getParameter("driverType");
        String regStartDate = request.getParameter("regStartDate");
        String regEndDate = request.getParameter("regEndDate");

        if (!StringUtil.isEmpty(vNo)) {
            criteria.andVNoLike("%"+vNo+"%");
        }
        if (!StringUtil.isEmpty(vType)) {
            criteria.andVTypeEqualTo(vType);
        }
        if(!StringUtil.isEmpty(vType)){
            criteria.andVTypeEqualTo(vType);
        }
        if (!StringUtil.isEmpty(status)) {
            criteria.andStatusEqualTo(status);
        }
        if (!StringUtil.isEmpty(driverType)) {
            criteria.andDriverTypeEqualTo(driverType);
        }
        if(!StringUtil.isEmpty(regStartDate)){
            criteria.andRegDateGreaterThanOrEqualTo(DateUtil.parseFormatIso8601Date(regStartDate));
        }
        if(!StringUtil.isEmpty(regEndDate)){
            criteria.andRegDateLessThanOrEqualTo(DateUtil.parseFormatIso8601Date(regEndDate));
        }
        e.setOrderByClause("UPDATED_AT desc");
    }

    @RequestMapping(value="/vehicleOper")
    public void vehicleOper(HttpServletRequest request,HttpServletResponse response) throws Exception {
       try{
	    	String oper = request.getParameter("oper");
	        if("add".equals(oper)){
	            ExVehicle exVehicle = new ExVehicle();
	            exVehicle.fromRequestParams(request);
	            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	            exVehicle.setRegDate(sdf.parse(exVehicle.getRegDateStr()));
	            vehicleService.add(exVehicle);
	        }else if("delete".equals(oper)){
	            String[] ids = request.getParameter("id").split(",");
                vehicleService.disable(ids);
	        }else if("update".equals(oper)){
	            ExVehicle exVehicle = new ExVehicle();
	            exVehicle.fromRequestParams(request);
                exVehicle.setId(Long.parseLong(request.getParameter("id")));
                vehicleService.updateById(exVehicle);
	        }
	        HtmlUtil.writerSuccessJson(response);
	    } catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response, e.getMessage());
		}
    }

    @RequestMapping(value = "/vehicleOptions")
    public void getVehicleList(
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        String status = request.getParameter("status");
        VehicleExample e = new VehicleExample();
        e.setOrderByClause("V_NO");
        if(!StringUtil.isEmpty(status)){
            e.createCriteria().andStatusEqualTo(ExVehicle.STATUS_ENABLED);
        }
        List<Vehicle> vehicleList = vehicleService.queryByList(e);
        Map map = new HashMap();
        map.put("vehicleList", vehicleList);
        HtmlUtil.writerSuccessJson(response,map);
    }
}
