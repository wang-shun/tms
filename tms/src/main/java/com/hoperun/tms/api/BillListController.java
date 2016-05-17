package com.hoperun.tms.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.DriverExample;
import com.hoperun.tms.bean.base.Route;
import com.hoperun.tms.bean.base.RouteExample;

import com.hoperun.tms.constants.ExWaybillLogConstants;
import com.hoperun.tms.service.customer.WayBilllLogService;
import com.hoperun.tms.service.customer.YimiWaybillService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.JacksonJsonUtil;
import com.hoperun.tms.bean.base.Carrier;
import com.hoperun.tms.bean.base.Driver;
import com.hoperun.tms.bean.customer.DeliveryBill;
import com.hoperun.tms.bean.customer.DeliveryBillExample;
import com.hoperun.tms.bean.customer.Waybill;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.WaybillExample.Criteria;
import com.hoperun.tms.bean.customer.WaybillLog;
import com.hoperun.tms.bean.customer.WaybillLogExample;
import com.hoperun.tms.bean.customer.extend.ExDeliveryBill;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.service.base.DriverService;
import com.hoperun.tms.service.base.RouteService;
import com.hoperun.tms.service.customer.DeliveryBillService;
import com.hoperun.tms.service.customer.WaybillService;
import com.hoperun.tms.util.DateUtils;

/**
 * 大车接口
 *
 * @author jerome
 */
@Controller
@RequestMapping("/api/wayBillQuery")
public class BillListController {
    @Autowired
    WaybillService waybillService;

    @Autowired
    DriverService driverService;

    @Autowired
    DeliveryBillService deliveryBillService;

    @Autowired
    RouteService routeService;

    @Autowired
    WayBilllLogService waybillLogService;

    @Autowired
    YimiWaybillService yimiWaybillService;

    /**
     * 根据手机号、状态查询运单
     * (status 00 创建 10 确认 20 大车运输 30 小车运输 40 签收)
     * (sacnStatus 00 未扫描 10 大车扫描未确认 20 扫描确认)
     *
     * @param mobile
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/queryWaybill/{mobile}/{status}/{scanStatus}", method = RequestMethod.GET)
    public void queryWaybill(@PathVariable String mobile, @PathVariable String status, @PathVariable String scanStatus, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            WaybillExample waybillExample = new WaybillExample();
            Criteria criteria = waybillExample.createCriteria();
            criteria.andScanTelEqualTo(mobile);
            criteria.andStatusEqualTo(status);
            criteria.andScanStatusEqualTo(scanStatus);
            map.put("waybillList", waybillService.getAll(waybillExample));
            map.put("status", 0);
            map.put("msg", "查询成功!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "查询失败!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }
        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 根据手机号查询运单(大车)
     *
     * @param mobile
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/queryAllWaybill/{mobile}", method = RequestMethod.GET)
    public void queryAllWaybill(@PathVariable String mobile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            WaybillExample waybillExample = new WaybillExample();
            Criteria criteria = waybillExample.createCriteria();
            criteria.andScanTelEqualTo(mobile);
            map.put("waybillList", waybillService.getAll(waybillExample));
            map.put("status", 0);
            map.put("msg", "查询成功!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "查询失败!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }
        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 大车扫码(00 创建 10 确认 20 大车运输 30 小车运输 40 签收)
     *
     * @param mobile
     * @param no
     * @param request
     * @param response
     * @throws Exception
     */
    
    @RequestMapping(value = "/Scan/{mobile}/{type}/{no}/{timestamp}", method = RequestMethod.GET)
    public void Scan(@PathVariable String mobile,@PathVariable String type, @PathVariable String no, @PathVariable String timestamp, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            WaybillExample waybillExample = new WaybillExample();
            Criteria criteria = waybillExample.createCriteria();
			//运单
            if (type.trim().equals("1")) {
				criteria.andNoEqualTo(no);
			}else{
				//订单
				criteria.andTicketNoEqualTo(no);
            }

            List<ExWaybill> list = waybillService.getAll(waybillExample);
            ExWaybill exWaybill_query = new ExWaybill();
            if (list.size() > 0) {
                exWaybill_query = list.get(0);
                map.put("area", exWaybill_query.getdCounty());//获取城区'
                if (Integer.parseInt(exWaybill_query.getScanStatus()) >= 10 || !exWaybill_query.getStatus().equals(ExWaybill.STATUS_CONFIRMED)) {
                    map.put("status", -1);
                    map.put("msg", "此单据[" + no + "]已扫过，状态为：" + exWaybill_query.getScanStatus());
                    HtmlUtil.writerJsonp(request, response, map);
                    return;
                }
            } else {
                map.put("status", -1);
                map.put("msg", "此单据" + no + "不存在!");
                HtmlUtil.writerJsonp(request, response, map);
                return;
            }

            criteria.andStatusEqualTo(ExWaybill.STATUS_CONFIRMED);
            criteria.andScanStatusEqualTo(ExWaybill.UNSCANED);
            ExWaybill exWaybill = new ExWaybill();
            exWaybill.setScanStatus(ExWaybill.SCANED_NOT_CONFIRM);
            exWaybill.setScanTel(mobile);

            WaybillLog waybillLog = new WaybillLog();
            waybillLog.setwId(exWaybill.getId());
            waybillLog.setwNo(exWaybill_query.getNo());
            waybillLog.setwLog("单据操作:大车扫码,单号【" + no + "】,扫码手机:" + mobile + ",路线：上海" + exWaybill_query.getdArea() + exWaybill_query.getdPoint() + ",收货方:" + exWaybill_query.getrName());
            waybillLog.setStatus("1");
            waybillLog.setwStatus("1");
            waybillService.sacnUpdate(exWaybill, waybillExample, waybillLog);

            //获取条数
            WaybillExample waybillExample_1 = new WaybillExample();
            Criteria criteria_1 = waybillExample_1.createCriteria();
            criteria_1.andScanTelEqualTo(mobile);
            criteria_1.andStatusEqualTo(ExWaybill.STATUS_CONFIRMED);
            criteria_1.andScanStatusEqualTo(ExWaybill.SCANED_NOT_CONFIRM);
            map.put("wayBillCount", waybillService.countWayBill(waybillExample_1));
            map.put("status", 0);
            map.put("msg", "扫码成功!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "扫码失败!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }

        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 运单扫码回退
     *
     * @param mobile
     * @param no
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/ScanRollback/{mobile}/{no}/{timestamp}", method = RequestMethod.GET)
    public void ScanRollback(@PathVariable String mobile, @PathVariable String no, @PathVariable String timestamp, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            WaybillExample waybillExample = new WaybillExample();
            Criteria criteria = waybillExample.createCriteria();
            criteria.andNoEqualTo(no);

            List<ExWaybill> list = waybillService.getAll(waybillExample);
            if (list.size() > 0) {
                ExWaybill exWaybill = list.get(0);
                if (!exWaybill.getScanStatus().equals("10")) {
                    map.put("status", -1);
                    map.put("msg", "运单状态为" + exWaybill.getScanStatus() + "不能撤销!");
                    HtmlUtil.writerJsonp(request, response, map);
                    return;
                }
            } else {
                map.put("status", -1);
                map.put("msg", "此运单!" + no + "不存在!");
                HtmlUtil.writerJsonp(request, response, map);
                return;
            }

            criteria.andStatusEqualTo(ExWaybill.STATUS_CONFIRMED);
            criteria.andScanStatusEqualTo(ExWaybill.SCANED_NOT_CONFIRM);
            ExWaybill exWaybill = new ExWaybill();
            exWaybill.setScanStatus(ExWaybill.UNSCANED);
            exWaybill.setScanTel(mobile);

            WaybillLogExample waybillLogExample = new WaybillLogExample();
            com.hoperun.tms.bean.customer.WaybillLogExample.Criteria criteria_log = waybillLogExample.createCriteria();
            criteria_log.andWNoEqualTo(no);
            criteria_log.andWStatusEqualTo(ExWaybill.BILLLOG_1); //大车扫码
            waybillService.sacnRollBack(exWaybill, waybillExample, waybillLogExample);

            map.put("status", 0);
            map.put("msg", "撤销成功!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "撤销失败!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }

        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 查询集散点列表
     *
     * @param mobile
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/route/{timestamp}", method = RequestMethod.GET)
    public void route(@PathVariable String timestamp,HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            RouteExample routeExample = new RouteExample();
            RouteExample.Criteria criteria = routeExample.createCriteria();
            List<Route> list = routeService.getRouteList(routeExample);
            map.put("routebill", list);
            map.put("status", 0);
            map.put("msg", "获取干线!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "获取干线!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }
        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 生成配送单(00 创建 10 确认 20 大车运输 30 小车运输 40 签收)
     *
     * @param mobile
     * @param id
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/creatDebill/{mobile}/{route}", method = RequestMethod.GET)
    public void creatDeliveryBill(@PathVariable String mobile, @PathVariable String route, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            //返写运单信息及状态
            WaybillExample waybillExample = new WaybillExample();
            Criteria criteria = waybillExample.createCriteria();
            criteria.andScanStatusEqualTo(ExWaybill.SCANED_NOT_CONFIRM);
            criteria.andStatusEqualTo(ExWaybill.STATUS_CONFIRMED);
            criteria.andScanTelEqualTo(mobile);

            List<ExWaybill> list = waybillService.getAll(waybillExample);
            if (list.size() <= 0) {
                map.put("status", -1);
                map.put("msg", "无运单信息，无法生成配送单信息!");
                HtmlUtil.writerJsonp(request, response, map);
                return;
            }

            List<WaybillLog> waybillLog_list = new ArrayList<WaybillLog>();
            for (ExWaybill exWaybill : list) {
                //写入日志
                WaybillLog waybillLog = new WaybillLog();
                waybillLog.setwId(exWaybill.getId());
                waybillLog.setwNo(exWaybill.getNo());
                waybillLog.setwLog("生成配送单,操作手机:" + mobile + "");
                waybillLog.setStatus("1");
                waybillLog.setwStatus(ExWaybill.BILLLOG_2);
                waybillLog_list.add(waybillLog);
            }

            //生成配送单
            Driver driver = getDriverId(mobile);
            ExDeliveryBill exDeliveryBill = new ExDeliveryBill();
            exDeliveryBill.setNo(DateUtils.curDateStr8() + DateUtils.curTimeStr6());
            exDeliveryBill.setdId(driver.getId());
            exDeliveryBill.setdName(driver.getName());
            exDeliveryBill.setvId(driver.getId());
            exDeliveryBill.setvNo(driver.getCerNo());
            if (!StringUtils.isEmpty(route)) {
                RouteExample routeExample = new RouteExample();
                com.hoperun.tms.bean.base.RouteExample.Criteria criteria_1 = routeExample.createCriteria();
                criteria_1.andIdEqualTo(Long.parseLong(route));
                List<Route> routeObs = routeService.getRouteList(routeExample);
                if (routeObs.size() > 0) {
                    exDeliveryBill.setrTerm(routeObs.get(0).getName());
                    exDeliveryBill.setDeliveryTerm(routeObs.get(0).getTermination());
                }
            }
            exDeliveryBill.setStatus(ExDeliveryBill.STATUS_CREATED);

            ExWaybill exWaybill = new ExWaybill();
            exWaybill.setScanStatus(ExWaybill.SCANED_AND_CONFIRMED);
            exWaybill.setStatus(ExWaybill.STATUS_TRUCK_DELIVERING);
            exWaybill.setDeliveryId(exDeliveryBill.getId());
            exWaybill.setDeliveryNo(exDeliveryBill.getNo());
            deliveryBillService.createDeliveryBillEx(exDeliveryBill, exWaybill, waybillExample, waybillLog_list);

            map.put("status", 0);
            map.put("msg", "生成配送单[" + exDeliveryBill.getId() + "]成功!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "生成配送单失败!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }
        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 根据手机、状态查询配送单接口(00 创建  10 确认  20 上车   30 下车)
     *
     * @param mobile
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/queryDeliverybill/{mobile}/{status}", method = RequestMethod.GET)
    public void queryDeliverybill(@PathVariable String mobile, @PathVariable String status, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            DeliveryBillExample deliveryBillExample = new DeliveryBillExample();
            com.hoperun.tms.bean.customer.DeliveryBillExample.Criteria criteria = deliveryBillExample.createCriteria();

            Driver driver = getDriverId(mobile);
            criteria.andDIdEqualTo(driver.getId());
            criteria.andStatusEqualTo(status);
            List<DeliveryBill> list = deliveryBillService.getDeliveryBillList(deliveryBillExample);
            map.put("deliverybill", list);
            map.put("deliverybillCount", list.size());
            map.put("status", 0);
            map.put("msg", "配送单查询成功!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "配送单查询失败!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }
        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 根据手机查询配送单接口
     *
     * @param mobile
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/queryAllDeliverybill/{mobile}", method = RequestMethod.GET)
    public void queryAllDeliverybill(@PathVariable String mobile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            DeliveryBillExample deliveryBillExample = new DeliveryBillExample();
            com.hoperun.tms.bean.customer.DeliveryBillExample.Criteria criteria = deliveryBillExample.createCriteria();
            Driver driver = getDriverId(mobile);
            criteria.andDIdEqualTo(driver.getId());
            List<DeliveryBill> list = deliveryBillService.getDeliveryBillList(deliveryBillExample);

            map.put("deliverybill", list);
            map.put("deliverybillCount", list.size());
            map.put("status", 0);
            map.put("msg", "配送单查询成功!");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "配送单查询失败!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }
        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 查询已扫运单数量(00 创建 10 确认 20 预分配 21 预分配确认 30 在途 40 签收)
     *
     * @param mobile
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/countWayBill/{mobile}/{status}/{scanStatus}", method = RequestMethod.GET)
    public void countWayBill(@PathVariable String mobile, @PathVariable String status, @PathVariable String scanStatus, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            WaybillExample waybillExample = new WaybillExample();
            Criteria criteria = waybillExample.createCriteria();
            criteria.andScanTelEqualTo(mobile);
            criteria.andStatusEqualTo(status);
            criteria.andScanStatusEqualTo(scanStatus);
            int count = waybillService.countWayBill(waybillExample);
            map.put("wayBillCount", count);
            map.put("status", 0);
            map.put("msg", "运单查询成功!");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "运单查询失败!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }
        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 大车下车扫码确认,更新状态,返回配送单信息
     *
     * @param deliverybillNo
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/confirmDeliverybill/{deliverybillNo}/{timestamp}", method = RequestMethod.GET)
    public void confirmDeliverybill(@PathVariable String deliverybillNo, @PathVariable String timestamp, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        try {
            DeliveryBillExample deliveryBillExample = new DeliveryBillExample();
            com.hoperun.tms.bean.customer.DeliveryBillExample.Criteria criteria = deliveryBillExample.createCriteria();
            criteria.andNoEqualTo(deliverybillNo);
            DeliveryBill deliveryBill = new DeliveryBill();
            List<DeliveryBill> deliveryBillList = deliveryBillService.getDeliveryBillList(deliveryBillExample);
            if (deliveryBillList.size() > 0) {
                deliveryBill = deliveryBillList.get(0);
                if (deliveryBill.getStatus().equals(ExWaybill.STATUS_CONFIRMED)) {
                    map.put("status", -1);
                    map.put("msg", "已经确认过该配送单!" + deliveryBill.getNo());
                    HtmlUtil.writerJsonp(request, response, map);
                    return;
                }
                deliveryBill.setStatus(ExWaybill.STATUS_CONFIRMED);
                deliveryBill.setArriveBy(new Date());

                //写入日志
                WaybillExample waybillExample = new WaybillExample();
                Criteria criteria_log = waybillExample.createCriteria();
                criteria_log.andDeliveryNoEqualTo(deliveryBill.getNo());
                List<ExWaybill> list = waybillService.getAll(waybillExample);
                List<WaybillLog> WaybillLog_list = new ArrayList<WaybillLog>();
                for (ExWaybill exWaybill : list) {
                    WaybillLog waybillLog = new WaybillLog();
                    waybillLog.setwId(exWaybill.getId());
                    waybillLog.setwNo(exWaybill.getNo());
                    waybillLog.setwLog("大车下车配送单确认,配送单号:" + deliverybillNo + "操作人:" + deliveryBill.getdName() + " " + deliveryBill.getDeliveryOrigin() + " " + deliveryBill.getDeliveryTerm());
                    waybillLog.setStatus("1");
                    waybillLog.setwStatus(ExWaybill.BILLLOG_3);
                    WaybillLog_list.add(waybillLog);
                }
                deliveryBillService.updateDeliveryBillForApi(deliveryBill, deliveryBillExample, WaybillLog_list);
            }

            map.put("deliverybill", deliveryBill);
            map.put("status", 0);
            map.put("msg", "下车扫码确认成功!");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            map.put("status", -1);
            map.put("msg", "下车扫码确认失败!" + e.getMessage());
            HtmlUtil.writerJsonp(request, response, map);
            return;
        }
        HtmlUtil.writerJsonp(request, response, map);
    }

    /**
     * 根据电话获取司机信息
     *
     * @param mobile
     * @return
     * @throws Exception
     */
    private Driver getDriverId(String moblie) throws Exception {
        DriverExample e = new DriverExample();
        e.createCriteria().andContactEqualTo(moblie);
        List<Driver> list = driverService.getDriverList(e);
        if (list.size() == 1) {
            Driver driver = (Driver) list.get(0);
            return driver;
        } else {
            throw new Exception(" 该手机绑定的司机信息不正确 ，返回条数：" + list.size());
        }
    }

    /**
     * 根据运单号返回履历信息（含大车、小车信息）
     * @param wNo
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/queryLogs/{wNo}", method = RequestMethod.GET)
    public void queryLogs(@PathVariable String wNo, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if(StringUtil.isEmpty(wNo)){
            throw new Exception("运单号为空，无法获取跟踪信息。");
        }
        Map resultMap = waybillLogService.getLogsInfo(wNo);
        HtmlUtil.writerSuccessJson(response, resultMap);
    }

    /**
     * 一米运单导入接口
     * */
    @RequestMapping(value = "/importWaybill", method = {RequestMethod.POST})
    public void importWaybill(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
        	String dataList = request.getParameter("dataList");
			List<LinkedHashMap> waybillList = (List<LinkedHashMap>) JacksonJsonUtil.jsonToBean(dataList, new ArrayList().getClass());
            yimiWaybillService.importWaybill(waybillList);
            HtmlUtil.writerSuccessJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            HtmlUtil.writerFailJson(response, e.getMessage());
        }
    }
}
