package com.hoperun.tms.bean.base.extend;

import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Carrier;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/13.
 */
public class ExCarrier extends Carrier{

    public final static String STATUS_ENABLED = "1";
    public final static String STATUS_DISABLED = "0";

    public void setFromRequestParams(HttpServletRequest request){
        setName(StringUtil.getNotNullStr(request.getParameter("name")));
        setType(StringUtil.getNotNullStr(request.getParameter("type")));
        setContactPerson(StringUtil.getNotNullStr(request.getParameter("contactPerson")));
        setContactTel(StringUtil.getNotNullStr(request.getParameter("contactTel")));
        setAddress(StringUtil.getNotNullStr(request.getParameter("address")));
        setcNo(StringUtil.getNotNullStr(request.getParameter("cNo")));
    }

    public void setFromMap(Map map) throws Exception{
        setId(map.get("id")==null?0:Long.parseLong(map.get("id").toString()));
        setName(map.get("name")==null?"":map.get("name").toString());
        String type = map.get("type")==null?"":map.get("type").toString();
        if(type.indexOf("-")>0){
            setType(type.split("-")[0]);
        }else{
            setType(type);
        }
        setContactPerson(map.get("contactPerson")==null?"":map.get("contactPerson").toString());
        setContactTel(map.get("contactTel")==null?"":map.get("contactTel").toString());
        setAddress(map.get("address")==null?"":map.get("address").toString());
        setcNo(map.get("cNo")==null?"":map.get("cNo").toString());
    }
}
