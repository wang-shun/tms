package com.hoperun.tms.controller.customer;

import com.hoperun.framework.utils.DateUtil;
import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.service.customer.WaybillService;
import com.hoperun.tms.service.customer.YimiWaybillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/8.
 */
@Controller
@RequestMapping("/yimiWaybill")
public class YimiWaybillController {

    @Autowired
    WaybillService waybillService;
    
    @Autowired
    YimiWaybillService yimiWaybillService;

    @RequestMapping(value="/query")
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = 1;
        int pageSize = 10;
        if(request.getParameter("page")!= null){
            pageNumber =  Integer.parseInt(request.getParameter("page").toString());
            pageSize = Integer.parseInt(request.getParameter("rows").toString());
        }else{
            pageSize=1000;
        }
        Map map = waybillService.getWaybillListPaginate(
                getQueryExample(request), pageNumber, pageSize);
        HtmlUtil.writerSuccessJson(response,map);
    }

    @RequestMapping(value = "/dNameOptions")
    public void getDNameOptions(HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<Map<String, String>> dNameOptions =
                waybillService.queryDNamesBySource(ExWaybill.SOURCE_YIMI_BILL);
        Map map = new HashMap();
        map.put("dNameOptions", dNameOptions);
        HtmlUtil.writerSuccessJson(response,map);
    }

    @RequestMapping(value = "/oper")
    public void deleteYimiWaybill(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String oper = request.getParameter("oper");
        try {
            if("delete".equals(oper)){
                String[] ids = request.getParameter("id").split(",");
                waybillService.disableWaybills(ids);
            }
            HtmlUtil.writerSuccessJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            HtmlUtil.writerFailJson(response, e.getMessage());
        }
    }

    private WaybillExample getQueryExample(HttpServletRequest request) throws Exception {
        WaybillExample example = new WaybillExample();
        example.setOrderByClause("UPDATED_AT desc");
        WaybillExample.Criteria criteria = example.createCriteria();
        criteria.andSourceEqualTo(ExWaybill.SOURCE_YIMI_BILL);

        String no = request.getParameter("no");
        String orderNo = request.getParameter("orderNo");
        String dName = request.getParameter("dName");
        String deliveryType = request.getParameter("deliveryType");
        String deliveryStartDate = request.getParameter("deliveryStartDate");
        String deliveryEndDate = request.getParameter("deliveryEndDate");
        String rName = request.getParameter("rName");
        String status = request.getParameter("status");
        if(!StringUtil.isEmpty(no)){
            criteria.andNoEqualTo(no);
        }
        if(!StringUtil.isEmpty(orderNo)){
            criteria.andOrderNoEqualTo(orderNo);
        }
        if(!StringUtil.isEmpty(dName)){
            criteria.andDNameLike("%"+dName+"%");
        }
        if(!StringUtil.isEmpty(deliveryType)){
            criteria.andDeliveryTypeEqualTo(deliveryType);
        }
        if(!StringUtil.isEmpty(rName)){
            criteria.andRNameLike("%"+rName+"%");
        }
        if(!StringUtil.isEmpty(status)){
            String statusArray[] = status.split(",");
            List<String> statusList = Arrays.asList(statusArray);
            criteria.andStatusIn(statusList);
        }
        if(!StringUtil.isEmpty(deliveryStartDate)){
            criteria.andDeliveryDateGreaterThanOrEqualTo(DateUtil.parseFormatIso8601Date(deliveryStartDate));
        }
        if(!StringUtil.isEmpty(deliveryEndDate)){
            criteria.andDeliveryDateLessThanOrEqualTo(DateUtil.parseFormatIso8601Date(deliveryEndDate));
        }
        return example;
    }
}
