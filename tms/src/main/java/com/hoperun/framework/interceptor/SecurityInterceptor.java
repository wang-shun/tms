package com.hoperun.framework.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hoperun.framework.annotation.Auth;
import com.hoperun.framework.utils.SessionUtils;
import com.hoperun.system.bean.SystemOperateEntity;
import com.hoperun.system.bean.SystemUserEntity;

/**
 * <br>
 * <b>功能：</b>控制用户权限的拦截器类<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Sep 9, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = Logger.getLogger(SecurityInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// log.debug("preHandle");
		HandlerMethod method = (HandlerMethod) handler;
		Auth auth = method.getMethod().getAnnotation(Auth.class);

		// String baseUri = request.getContextPath();
		// String path = request.getServletPath();

		// 获取当前登录用户信息
		SystemUserEntity user = SessionUtils.getUser(request);
		if (auth != null) {
			if (auth.verifyLogin()) {
				if(user==null){
					// TODO 当前用户没有登录系统
					LOGGER.error("用户没有登录系统。");
					return false;
				}
				// 需要验证用户是否已经登录
				if (!"".equals(auth.verifyOperateKey())) {
					if (auth.isHQOperate()) {
						// 总部权限
						List<SystemOperateEntity> operateList = SessionUtils.getHeadquarterOperate(request);
						for (SystemOperateEntity entity : operateList) {
							if (auth.verifyOperateKey().equals(entity.getKeyName())) {
								break;
							}
						}
						// TODO 用户没有权限
						LOGGER.error("用户：" + user.getAccount() + "没有" + auth.verifyOperateKey() + "的使用权限。");
						return false;
					} else {
						String operates = (String) SessionUtils.getCurrentRestaurantOperate(request);
						if (operates.indexOf(auth.verifyOperateKey()) == -1) {
							// TODO 用户没有权限
							LOGGER.error("用户：" + user.getAccount() + "没有" + auth.verifyOperateKey() + "的使用权限。");
							return false;
						}
						// 非总部权限
					}
				}
			}
		} else {
			LOGGER.debug("不受权限控制的接口");
		}
		return super.preHandle(request, response, handler);
	}
}
