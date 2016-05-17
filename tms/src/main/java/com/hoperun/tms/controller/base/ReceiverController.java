/**
 * 基础数据-收货方地址导入
 *
 * @author 15120071
 * 2016-3-21
 */
package com.hoperun.tms.controller.base;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.JacksonJsonUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Receiver;
import com.hoperun.tms.bean.base.ReceiverExample;
import com.hoperun.tms.bean.base.extend.ExReceiver;
import com.hoperun.tms.service.base.ReceiverService;


@Controller
@RequestMapping("/receiver")
public class ReceiverController {

    @Autowired
    ReceiverService receiverService;

    /**
     * 收货方地址导入页面
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/query")
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        ReceiverExample e = new ReceiverExample();
        ReceiverExample.Criteria criteria = e.createCriteria();
        criteria.andStatusEqualTo(ExReceiver.STATUS_ENABLED);
        if (!StringUtil.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
            request.setAttribute("name", name);
        }
        int pageNumber = 1;
        int pageSize = 10;
        if (request.getParameter("page") != null) {
            pageNumber = Integer.parseInt(request.getParameter("page").toString());
            pageSize = Integer.parseInt(request.getParameter("rows").toString());
        } else {
            pageSize = 1000;
        }
        e.setOrderByClause("UPDATED_AT desc");
        Map map = receiverService.getReceiverListPaginate(e, pageNumber, pageSize);
//		Map map = new HashMap();
//		List<Receiver> receiverList = receiverService.getReceiverList(receiver);
//		map.put("receiverList", receiverList);
        HtmlUtil.writerSuccessJson(response, map);
    }

    /**
     * 收货方地址导入页面
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryAll")
    public String receiver(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        ReceiverExample e = new ReceiverExample();
        ReceiverExample.Criteria criteria = e.createCriteria();
        if (!StringUtil.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
            request.setAttribute("name", name);
        }
        List<Receiver> dlist = receiverService.getReceiverList(e);
        request.setAttribute("receiverList", dlist);
        return "/base/receiver";
    }

    /**
     * 用户操作
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/receiverOper")
    @ResponseBody
    public void receiverOper(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String oper = request.getParameter("oper");
            if("receiverAdd".equals(oper)){
                ExReceiver exReceiver = new ExReceiver();
                exReceiver.setFromRequestParams(request);
                exReceiver.setStatus(ExReceiver.STATUS_ENABLED);
                receiverService.insertReceiver(exReceiver);
            }else if("receiverDel".equals(oper)){
                String[] ids = request.getParameter("id").split(",");
                receiverService.disable(ids);
            }else if("receiverUpdate".equals(oper)){
                ExReceiver exReceiver = new ExReceiver();
                exReceiver.setFromRequestParams(request);
                exReceiver.setId(Long.parseLong(request.getParameter("id")));
                receiverService.updateById(exReceiver);
            }
            HtmlUtil.writerSuccessJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            HtmlUtil.writerFailJson(response, e.getMessage());
        }
    }

    @RequestMapping(value = "/receiverUpdate")
    @ResponseBody
    public void receiverUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
			String dataList = request.getParameter("dataList");
			List<LinkedHashMap> receiverList = (List<LinkedHashMap>) JacksonJsonUtil.jsonToBean(dataList, new ArrayList().getClass());
			receiverService.updateBatch(receiverList);
			HtmlUtil.writerSuccessJson(response);
		} catch (Exception e) {
			e.printStackTrace();
			HtmlUtil.writerFailJson(response, e.getMessage());
		}
    }

    @RequestMapping(value = "/receiverOptions", method = RequestMethod.POST)
    public void getReceiverList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map> receiverList = receiverService.queryForOptions();
        Map map = new HashMap();
        map.put("receiverList", receiverList);
        HtmlUtil.writerSuccessJson(response, map);
    }

    @RequestMapping(value = "/receiverUpload")
    public String receiverUpload(@RequestParam(value = "fileBuildInfo", required = false) MultipartFile buildInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			receiverService.exceltodb(buildInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "redirect:../html/tmsbase/receiver.html?errorMessage="+URLEncoder.encode(e.getMessage(),"UTF-8");
		}
        return "redirect:../html/tmsbase/receiver.html";
    }

    @RequestMapping(value = "/loadReceiverInfo")
    public void loadSenderInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Receiver receiver = receiverService.queryById(Long.parseLong(id));
        Map map = new HashMap();
        map.put("receiver", receiver);
        HtmlUtil.writerSuccessJson(response,map);
    }
}
