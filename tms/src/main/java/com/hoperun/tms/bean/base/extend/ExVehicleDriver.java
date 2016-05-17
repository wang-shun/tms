package com.hoperun.tms.bean.base.extend;

import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.VehicleDriver;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/13.
 */
public class ExVehicleDriver extends VehicleDriver{
    public final static String STATUS_ENABLED = "1";
    public final static String STATUS_DISABLED = "0";


    public void fromRequestParams(HttpServletRequest request){
        try{
            if(!StringUtil.isEmpty(request.getParameter("id"))){
                setId(Long.parseLong(StringUtil.getNotNullStr(request.getParameter("id"))));
            }
        }catch (Exception e){}
        try{
            if(!StringUtil.isEmpty(request.getParameter("vId"))){
                setvId(Long.parseLong(StringUtil.getNotNullStr(request.getParameter("vId"))));
            }
        }catch (Exception e){}
        try{
            if(!StringUtil.isEmpty(request.getParameter("dId"))){
                setdId(Long.parseLong(StringUtil.getNotNullStr(request.getParameter("dId"))));
            }
        }catch (Exception e){}
    }

    public void fromMap(Map map) throws Exception{
        try{
            if(map.get("id")==null && !StringUtil.isEmpty(map.get("id").toString())){
                setId(Long.parseLong(StringUtil.getNotNullStr(map.get("id"))));
            }
        }catch (Exception e){}
        try{
            if(map.get("vId")==null && !StringUtil.isEmpty(map.get("vId").toString())){
                setvId(Long.parseLong(StringUtil.getNotNullStr(map.get("vId"))));
            }
        }catch (Exception e){}
        try{
            if(map.get("dId")==null && !StringUtil.isEmpty(map.get("dId").toString())){
                setdId(Long.parseLong(StringUtil.getNotNullStr(map.get("dId"))));
            }
        }catch (Exception e){}
    }
}
