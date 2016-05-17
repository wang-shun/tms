package com.hoperun.tms.controller.base;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoperun.tms.bean.base.extend.ExSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Sender;
import com.hoperun.tms.bean.base.SenderExample;
import com.hoperun.tms.controller.common.ControllerUtil;
import com.hoperun.tms.service.base.SenderService;

/**
 * Created by 16030117 on 2016/3/31.
 */
@Controller
@RequestMapping("/sender")
public class SenderController {

    @Autowired
    SenderService senderService;
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
        String name = request.getParameter("name");
        SenderExample e = new SenderExample();
        SenderExample.Criteria c =
                e.createCriteria().andStatusEqualTo("1");
        if(!StringUtil.isEmpty(name)){
            c.andNameLike("%"+name+"%");
        }
		e.setOrderByClause("UPDATED_AT desc");

		Map map = senderService.getDriverListPaginate(e, pageNumber, pageSize);
        HtmlUtil.writerSuccessJson(response,map);
    }

    @RequestMapping(value="/oper")
    public void senderOper(HttpServletRequest request,HttpServletResponse response) throws Exception {
        try {
			String oper = request.getParameter("oper");
			if("add".equals(oper)){
			    ExSender exSender = new ExSender();
			    exSender.fromRequestParams(request);
			    exSender.setStatus(ExSender.STATUS_ENABLED);
			    senderService.add(exSender);
			}else if("delete".equals(oper)){
			    String[] ids = request.getParameter("id").split(",");
				senderService.disable(ids);
			}else if("update".equals(oper)){
				ExSender exSender = new ExSender();
				exSender.fromRequestParams(request);
				exSender.setId(Long.parseLong(request.getParameter("id")));
			    senderService.updateById(exSender);
			}
			HtmlUtil.writerSuccessJson(response);
		} catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response, e.getMessage());
		}
    }
    
	@RequestMapping(value = "/senderOptions")
	public void getSenderList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		List<Map> senderList = senderService.queryForOptions();
		Map map = new HashMap();
		map.put("senderList", senderList);
		HtmlUtil.writerSuccessJson(response,map);
	}

	@RequestMapping(value = "/loadSenderInfo")
	public void loadSenderInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		Sender sender = senderService.queryById(Long.parseLong(id));
		Map map = new HashMap();
		map.put("sender", sender);
		HtmlUtil.writerSuccessJson(response,map);
	}
}
