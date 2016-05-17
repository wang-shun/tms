package com.hoperun.tms.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.tms.bean.base.WxConfig;
import com.hoperun.tms.service.base.WxConfigService;

@Controller
@RequestMapping("/api/weiChart")
public class WeiChartContorller {
	
	@Autowired
	WxConfigService wxConfigService;
	
	@RequestMapping(value="/config/{url}",method=RequestMethod.GET)
	public void getConfig(@PathVariable String url,HttpServletRequest request,HttpServletResponse response) throws Exception {
		WxConfig bean=wxConfigService.getWxConfig(url);
		HtmlUtil.writerJsonp(request,response,bean);
}
}
