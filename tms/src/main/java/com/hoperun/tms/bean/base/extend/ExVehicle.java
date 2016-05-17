package com.hoperun.tms.bean.base.extend;

import com.hoperun.framework.utils.DateUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Vehicle;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/13.
 */
public class ExVehicle extends Vehicle{

    public final static String STATUS_ENABLED = "1";
    public final static String STATUS_DISABLED = "2";
    public final static String STATUS_MAINTAIN = "3";

    public final static String TRUCK = "1";
    public final static String POWER_CAR = "2";

    private String regDateStr;

    public String getRegDateStr() {
        return regDateStr;
    }

    public void setRegDateStr(String regDateStr) {
        this.regDateStr = regDateStr;
    }

    public void setRegDate(Date regDate) {
        super.setRegDate(regDate);
        setRegDateStr(new SimpleDateFormat("yyyy-MM-dd").format(regDate));
    }

    public void fromRequestParams(HttpServletRequest request) throws Exception{
        setNo((StringUtil.getNotNullStr(request.getParameter("no"))));
        setvNo(StringUtil.getNotNullStr(request.getParameter("vNo")));
        setvType(StringUtil.getNotNullStr(request.getParameter("vType")));
        setDriverType(StringUtil.getNotNullStr(request.getParameter("driverType")));
        setDeliveryType(StringUtil.getNotNullStr(request.getParameter("deliveryType")));
        setRemark(StringUtil.getNotNullStr(request.getParameter("remark")));
        setStatus(StringUtil.getNotNullStr(request.getParameter("status")));
        setRegDateStr(StringUtil.getNotNullStr(request.getParameter("regDateStr")));
        setRegDate(new SimpleDateFormat("yyyy-MM-dd").parse(getRegDateStr()));
        try{
            if(!StringUtil.isEmpty(request.getParameter("vSize"))){
                setvSize(Long.parseLong(StringUtil.getNotNullStr(request.getParameter("vSize"))));
            }
        }catch (Exception e){
            setvSize(0L);
        }
        try{
            if(!StringUtil.isEmpty(request.getParameter("vWeight"))){
                setvWeight(Long.parseLong(StringUtil.getNotNullStr(request.getParameter("vWeight"))));
            }
        }catch (Exception e){
            setvWeight(0L);
        }
        try{
            if(!StringUtil.isEmpty(request.getParameter("regDate"))){
                setRegDate(DateUtil.parseFormatIso8601Date(request.getParameter("regDate")));
            }
        }catch (Exception e){

        }
        try{
            if(!StringUtil.isEmpty(request.getParameter("id"))){
                setId(Long.parseLong(StringUtil.getNotNullStr(request.getParameter("id"))));
            }
        }catch (Exception e){

        }
    }

    public void fromMap(Map map) throws Exception{
        setId(map.get("id")==null?0:Long.parseLong(map.get("id").toString()));
        setNo(map.get("no")==null?"":map.get("no").toString());
        setvNo(map.get("vNo")==null?"":map.get("vNo").toString());
        setvType(map.get("vType")==null?"":map.get("vType").toString());
        setvSize(map.get("vSize")==null?0:Long.parseLong(map.get("vSize").toString()));
        setvWeight(map.get("vWeight")==null?0:Long.parseLong(map.get("vWeight").toString()));
        setDriverType(map.get("driverType")==null?"":map.get("driverType").toString());
        setDeliveryType(map.get("deliveryType")==null?"":map.get("deliveryType").toString());
        setRegDateStr(map.get("regDateStr")==null?"":map.get("regDateStr").toString());
        setRemark(map.get("remark")==null?"":map.get("remark").toString());
        setStatus(map.get("status")==null?"":map.get("status").toString());
    }
}
