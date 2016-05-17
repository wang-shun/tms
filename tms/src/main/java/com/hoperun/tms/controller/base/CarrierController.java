/**
 * 基础数据-司机管理
 *
 * @author 15120071
 * 2016-3-21
 */
package com.hoperun.tms.controller.base;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoperun.tms.bean.base.CarrierExample;
import com.hoperun.tms.bean.base.extend.ExCarrier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.service.base.CarrierService;


@Controller
@RequestMapping("/carrier")
public class CarrierController {

    @Autowired
    CarrierService carrierService;

    /**
     * 司机管理页面
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/query")
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        CarrierExample carrierExample = new CarrierExample();
        CarrierExample.Criteria criteria = carrierExample.createCriteria();
        criteria.andStatusEqualTo("1");
        if (!StringUtil.isEmpty(name)) {
            criteria.andNameLike("%"+name+"%");
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
        carrierExample.setOrderByClause("UPDATED_AT desc");
        Map map = carrierService.getCarrierListPaginate(carrierExample, pageNumber, pageSize);
        HtmlUtil.writerSuccessJson(response, map);
    }

    /**
     * 用户操作
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/carrierOper")
    @ResponseBody
    public void carrierOper(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String oper = request.getParameter("oper");
            if ("carrierAdd".equals(oper)) {
                ExCarrier exCarrier = new ExCarrier();
                exCarrier.setFromRequestParams(request);
                exCarrier.setStatus("1");
                carrierService.insertCarrier(exCarrier);
            } else if ("carrierDel".equals(oper)) {
                String[] ids = request.getParameter("id").split(",");
                carrierService.disable(ids);
            } else if ("carrierUpdate".equals(oper)) {
                ExCarrier exCarrier = new ExCarrier();
                exCarrier.setFromRequestParams(request);
                exCarrier.setId(Long.parseLong(request.getParameter("id")));
                carrierService.updateById(exCarrier);
            }
            HtmlUtil.writerSuccessJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            HtmlUtil.writerFailJson(response, e.getMessage());
        }
    }
}
