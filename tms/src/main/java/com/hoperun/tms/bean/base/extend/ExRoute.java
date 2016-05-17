package com.hoperun.tms.bean.base.extend;

import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Route;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/13.
 */
public class ExRoute extends Route{

    public final static String STATUS_ENABLED = "1";
    public final static String STATUS_DISABLED = "0";

    public void setFromRequestParams(HttpServletRequest request){
        setName(StringUtil.getNotNullStr(request.getParameter("name")));
        setTermination(StringUtil.getNotNullStr(request.getParameter("termination")));
        setCity(StringUtil.getNotNullStr(request.getParameter("city")));
        setArea(StringUtil.getNotNullStr(request.getParameter("area")));
        setRemark(StringUtil.getNotNullStr(request.getParameter("remark")));
    }

    public void setFromMap(Map map) throws Exception{
        setId(map.get("id")==null?0:Long.parseLong(map.get("id").toString()));
        setName(map.get("name")==null?"":map.get("name").toString());
        setTermination(map.get("termination")==null?"":map.get("termination").toString());
        setCity(map.get("city")==null?"":map.get("city").toString());
        setArea(map.get("area")==null?"":map.get("area").toString());
        setRemark(map.get("remark")==null?"":map.get("remark").toString());
    }
}
