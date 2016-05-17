package com.hoperun.tms.bean.base.extend;

import com.hoperun.framework.utils.DateUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Driver;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/13.
 */
public class ExDriver extends Driver {

    public final static String STATUS_ENABLED = "1";
    public final static String STATUS_DISABLED = "0";

    public final static String CAR_TYPE_TRUCK = "1";
    public final static String CAR_TYPE_POWERCAR = "2";


    private String cerExpireDateStr;

    public String getCerExpireDateStr() {
        return cerExpireDateStr;
    }

    public void setCerExpireDateStr(String cerExpireDateStr) {
        this.cerExpireDateStr = cerExpireDateStr;
    }

    public void setCerExpireDate(Date cerExpireDate) {
        setCerExpireDateStr(new SimpleDateFormat("yyyy-MM-dd").format(cerExpireDate));
        super.setCerExpireDate(cerExpireDate);
    }

    public void setFromRequestParams(HttpServletRequest request) throws Exception{
        setName(StringUtil.getNotNullStr(request.getParameter("name")));
        setSex(StringUtil.getNotNullStr(request.getParameter("sex")));
        setContact(StringUtil.getNotNullStr(request.getParameter("contact")));
        setdId(StringUtil.getNotNullStr(request.getParameter("dId")));
        setCerType(StringUtil.getNotNullStr(request.getParameter("cerType")));
        setCerNo(StringUtil.getNotNullStr(request.getParameter("cerNo")));
        setCerCarType(StringUtil.getNotNullStr(request.getParameter("cerCarType")));
        if(!StringUtil.isEmpty(request.getParameter("cerExpireDateStr"))){
            setCerExpireDate(DateUtil.parseFormatIso8601Date(request.getParameter("cerExpireDateStr")));
        }
        setStatus(StringUtil.getNotNullStr(request.getParameter("status")));
    }

    public void setFromMap(Map map) throws Exception{
        setId(map.get("id")==null?0:Long.parseLong(map.get("id").toString()));
        setName(map.get("name")==null?"":map.get("name").toString());
        String sex =map.get("sex")==null?"":map.get("sex").toString();
        if(sex.indexOf("-")>0){
            setSex(sex.split("-")[0]);
        }else{
            setSex(sex);
        }
        setContact(map.get("contact")==null?"":map.get("contact").toString());
        setdId(map.get("dId")==null?"":map.get("dId").toString());
        setCerType(map.get("cerType")==null?"":map.get("cerType").toString());
        setCerNo(map.get("cerNo")==null?"":map.get("cerNo").toString());
        String cerCarType = map.get("cerCarType")==null?"":map.get("cerCarType").toString();
        if(cerCarType.indexOf("-")>0){
            setCerCarType(cerCarType.split("-")[0]);
        }else{
            setCerCarType(cerCarType);
        }
        setCerExpireDate(map.get("cerExpireDateStr")==null?null: DateUtil.parseFormatIso8601Date(map.get("cerExpireDateStr").toString()));
        setStatus(map.get("status")==null?"1":(StringUtil.isEmpty(map.get("status").toString())?"1":map.get("status").toString()));
    }
}
