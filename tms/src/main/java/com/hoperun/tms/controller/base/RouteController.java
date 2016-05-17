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

import com.hoperun.tms.bean.base.RouteExample;
import com.hoperun.tms.bean.base.extend.ExRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.service.base.RouteService;


@Controller
@RequestMapping("/route")
public class RouteController {

    @Autowired
    RouteService routeService;

    /**
     * 司机管理页面
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/query")
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        String termination = request.getParameter("termination");
        RouteExample e = new RouteExample();
        RouteExample.Criteria criteria = e.createCriteria();
        criteria.andStatusEqualTo("1");
        if (!StringUtil.isEmpty(name)) {
            criteria.andNameLike("%"+name+"%");
        }
        if (!StringUtil.isEmpty(termination)) {
            criteria.andTerminationLike("%"+termination+"%");
        }
        e.setOrderByClause("UPDATED_AT desc");
        int pageNumber = 1;
        int pageSize = 10;
        if (request.getParameter("page") != null) {
            pageNumber = Integer.parseInt(request.getParameter("page").toString());
            pageSize = Integer.parseInt(request.getParameter("rows").toString());
        } else {
            pageSize = 1000;
        }
        Map map = routeService.getRouteListPaginate(e, pageNumber, pageSize);
        HtmlUtil.writerSuccessJson(response, map);
    }

    /**
     * 用户操作
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/routeOper")
    @ResponseBody
    public void routeOper(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String oper = request.getParameter("oper");
            if ("routeAdd".equals(oper)) {
                ExRoute exRoute = new ExRoute();
                exRoute.setFromRequestParams(request);
                exRoute.setCity("上海");
                exRoute.setStatus(ExRoute.STATUS_ENABLED);
                routeService.insertRoute(exRoute);
            } else if ("routeDel".equals(oper)) {
                String[] ids = request.getParameter("id").split(",");
                routeService.disable(ids);
            } else if ("routeUpdate".equals(oper)) {
                ExRoute exRoute = new ExRoute();
                exRoute.setId(Long.parseLong(request.getParameter("id")));
                exRoute.setFromRequestParams(request);
                exRoute.setCity("上海");
                routeService.updateById(exRoute);
            }
            HtmlUtil.writerSuccessJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            HtmlUtil.writerFailJson(response, e.getMessage());
        }
    }
}
