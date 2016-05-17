package com.hoperun.tms.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoperun.tms.bean.base.DriverExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.tms.bean.base.Driver;
import com.hoperun.tms.service.base.DriverService;

@Controller
@RequestMapping("/api/becomeSilent")
public class BecomeSilentController {

	@Autowired
	DriverService  driverService;
	
	@RequestMapping(value="/becomeSilent/{phone}/{timestamp}",method=RequestMethod.GET)
	public void query(@PathVariable String phone,@PathVariable String timestamp,HttpServletRequest request,HttpServletResponse response) throws Exception {
		DriverExample e=new DriverExample();
		e.createCriteria().andContactEqualTo(phone);
		List<Driver> rlist=driverService.getDriverList(e);
		Driver driver = new Driver();
		if(null!=rlist && rlist.size()>0){
			driver=rlist.get(0);
		}
	   	HtmlUtil.writerJsonp(request,response,driver);
}
}
