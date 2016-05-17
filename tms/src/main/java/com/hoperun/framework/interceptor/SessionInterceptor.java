package com.hoperun.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hoperun.framework.annotation.NotAuth;
import com.hoperun.framework.utils.SessionUtils;
import com.hoperun.system.bean.SystemUserEntity;

/**
 * <br>
 * <b>功能：</b>SessionInterceptor<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Sep 9, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = Logger.getLogger(SessionInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// log.debug("preHandle");
		HandlerMethod method = (HandlerMethod) handler;
		NotAuth notAuth = method.getMethod().getAnnotation(NotAuth.class);

		// String baseUri = request.getContextPath();
		// String path = request.getServletPath();

		// 获取当前登录用户信息
		SystemUserEntity user = SessionUtils.getUser(request);

		if (notAuth == null) {
			if (user == null) {
				LOGGER.debug("用户没有OpenId");
				// TODO 增加对另外一个请求类型的处理
				
			}
		} else {
			LOGGER.debug("不受权限控制的接口");
		}
		return super.preHandle(request, response, handler);
	}
}
