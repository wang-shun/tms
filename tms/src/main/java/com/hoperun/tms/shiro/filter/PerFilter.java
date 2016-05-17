package com.hoperun.tms.shiro.filter;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoperun.tms.bean.authority.Menu;


/**
 * <pre>
 *
 * </pre>
 *
 * @author 15120071
 * @version 2016-2-3
 */
public class PerFilter extends PathMatchingFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("unchecked")
    @Override
    protected boolean onPreHandle(final ServletRequest request, final ServletResponse response, final Object mappedValue) throws Exception {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Map<String, Menu> map = (Map<String, Menu>) req.getSession().getAttribute("menuMap");
        String servletPath = req.getPathInfo();
        logger.debug("请求:" + servletPath);
        if ( null != map && (map.containsKey(servletPath) || map.containsKey(servletPath.replaceAll("/html", "..")))) {
            logger.debug("校验请求:" + servletPath);
//            SecurityUtils.getSubject().checkPermissions(servletPath);
            logger.debug("拥有[" + servletPath + "]权限");
            return true;
        }

      
    if (req.getHeader("x-requested-with") != null && req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) { // 如果是ajax请求响应头会有，x-requested-with
//     // resp.setContentType("text/html;charset=utf-8");
//     // resp.setHeader("errorCode", ErrorCode.USER_UN_LOGIN.code.toString());
//     // resp.addHeader("errorMessage", ErrorCode.USER_UN_LOGIN.value);
//     // resp.addHeader("returnUrl", UriConstant.USER.SPACE + "/toLogin");
    	 return true;
////     } else {
//         // req.getSession().setAttribute("returnUrl", req.getRequestURI());
//         // resp.sendRedirect(UriConstant.USER.SPACE + "/toLogin");
     }
//    req.getSession().setAttribute("returnUrl", req.getRequestURI());
//    resp.sendRedirect("/html/main/login.html");
        return true;
    }
}
