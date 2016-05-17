package com.hoperun.tms.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;

import com.hoperun.tms.bean.authority.User;
import com.hoperun.tms.util.Constant;


/**
 * 
 * <pre>
 * 
 * </pre>
 *
 * @author 15120071
 * @version 2016-2-3
 */
public class SysUserFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest req, ServletResponse resp, Object mappedValue) throws Exception {
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) resp;
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if (null == user) { //未登录
            if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){ //如果是ajax请求响应头会有，x-requested-with  
            	 response.setContentType("text/html;charset=utf-8");
            	 response.addHeader("returnUrl", "/html/main/login.html");
            }else{
            	request.getSession().setAttribute("returnUrl", request.getRequestURI());
    			response.sendRedirect("/html/main/login.html");
            }  
            return false;
        }else  request.setAttribute(Constant.SESSIONKEY_USER, user);
        
        return true;
    }
}
