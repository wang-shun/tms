package com.hoperun.tms.bean.base.extend;

import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Receiver;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 16030117 on 2016/4/13.
 */
public class ExReceiver extends Receiver{

    public final static String STATUS_ENABLED = "1";
    public final static String STATUS_DISABLED = "0";

    public void setFromRequestParams(HttpServletRequest request){
        setName(StringUtil.getNotNullStr(request.getParameter("name")));
        setArea(StringUtil.getNotNullStr(request.getParameter("area")));
        setContact(StringUtil.getNotNullStr(request.getParameter("contact")));
        setTel(StringUtil.getNotNullStr(request.getParameter("tel")));
        setAddress(StringUtil.getNotNullStr(request.getParameter("address")));
    }
}
