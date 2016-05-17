package com.hoperun.tms.bean.base.extend;

import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Sender;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/13.
 */
public class ExSender extends Sender {

    public final static String STATUS_ENABLED = "1";
    public final static String STATUS_DISABLED = "0";

    public void fromRequestParams(HttpServletRequest request) throws Exception{
        setName(StringUtil.getNotNullStr(request.getParameter("name")));
        setContact(StringUtil.getNotNullStr(request.getParameter("contact")));
        setTel(StringUtil.getNotNullStr(request.getParameter("tel")));
        setPoint(StringUtil.getNotNullStr(request.getParameter("point")));
        setArea(StringUtil.getNotNullStr(request.getParameter("area")));
        setAddress(StringUtil.getNotNullStr(request.getParameter("address")));
        setWechatNo(StringUtil.getNotNullStr(request.getParameter("wechatNo")));
        setProvince(StringUtil.getNotNullStr(request.getParameter("province")));
        setCity(StringUtil.getNotNullStr(request.getParameter("city")));
        setCounty(StringUtil.getNotNullStr(request.getParameter("county")));
    }

    public void fromMap(Map map) throws Exception{
        setId(map.get("id")==null?0:Long.parseLong(map.get("id").toString()));
        setName(StringUtil.getNotNullStr(map.get("name")));
        setContact(StringUtil.getNotNullStr(map.get("contact")));
        setTel(StringUtil.getNotNullStr(map.get("tel")));
        setPoint(StringUtil.getNotNullStr(map.get("point")));
        setArea(StringUtil.getNotNullStr(map.get("area")));
        setAddress(StringUtil.getNotNullStr(map.get("address")));
        setWechatNo(StringUtil.getNotNullStr(map.get("wechatNo")));
        setProvince(StringUtil.getNotNullStr(map.get("province")));
        setCity(StringUtil.getNotNullStr(map.get("city")));
        setCounty(StringUtil.getNotNullStr(map.get("county")));
    }
}
