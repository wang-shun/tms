package com.hoperun.tms.controller.TransportationExecution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.customer.extend.ExDeliveryBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.tms.bean.base.Driver;
import com.hoperun.tms.bean.base.DriverExample;
import com.hoperun.tms.bean.base.Vehicle;
import com.hoperun.tms.bean.base.VehicleExample;
import com.hoperun.tms.bean.customer.DeliveryBill;
import com.hoperun.tms.bean.customer.DeliveryBillExample;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.service.base.DriverService;
import com.hoperun.tms.service.base.VehicleService;
import com.hoperun.tms.service.customer.DeliveryBillService;
import com.hoperun.tms.service.customer.WaybillService;

@Controller
@RequestMapping("/deliveryBill")
public class DeliveryBillController {

    @Autowired
    WaybillService waybillService;

    @Autowired
    DeliveryBillService deliveryBillService;

    @RequestMapping(value = "/query")
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pageNumber = 1;
        int pageSize = 10;
        if (request.getParameter("page") != null) {
            pageNumber = Integer.parseInt(request.getParameter("page").toString());
            pageSize = Integer.parseInt(request.getParameter("rows").toString());
        } else {
            pageSize = 1000;
        }
        DeliveryBillExample e = new DeliveryBillExample();
        DeliveryBillExample.Criteria criteria = e.createCriteria();
        String vNo = request.getParameter("vNo");
        String dName = request.getParameter("dName");
        String status = request.getParameter("status");

        if (!StringUtil.isEmpty(vNo)) {
            criteria.andVNoLike("%"+vNo+"%");
        }
        if (!StringUtil.isEmpty(dName)) {
            criteria.andDNameLike("%"+dName+"%");
        }
        if(!StringUtil.isEmpty(status)){
            String statusArray[] = status.split(",");
            List<String> statusList = Arrays.asList(statusArray);
            criteria.andStatusIn(statusList);
        }
        Map map = deliveryBillService.getDeliveryBillListPaginate(e, pageNumber, pageSize);
        HtmlUtil.writerSuccessJson(response, map);
    }

    /**
     * 配送单明细查询
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/queryDetail")
    public void queryDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        DeliveryBill deliveryBill = deliveryBillService.getDeliveryBillById(Long.parseLong(id));
        WaybillExample waybillExample = new WaybillExample();
        waybillExample.createCriteria().andDeliveryNoEqualTo(deliveryBill.getNo());
        List<ExWaybill> waybillList = waybillService.selectByExample(waybillExample);
        Map map = new HashMap();
        map.put("deliveryBill", deliveryBill);
        map.put("waybillList", waybillList);
        HtmlUtil.writerSuccessJson(response, map);
    }
}
