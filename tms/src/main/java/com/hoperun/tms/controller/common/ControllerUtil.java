package com.hoperun.tms.controller.common;

import com.hoperun.framework.base.BaseBean;
import com.hoperun.framework.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/1.
 */
public class ControllerUtil{

    /**
     * 将params传给requestAttrs
     * @param request
     * @throws Exception
     */
    public static <T extends BaseBean> void paramsToAttrs(
            HttpServletRequest request) throws Exception{
        Iterator<Map.Entry<String, String>> it =
                request.getParameterMap().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = it.next();
            request.setAttribute(
                    entry.getKey().toString(), entry.getValue());
        }
    }
}
