package com.hoperun.framework.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.hoperun.framework.utils.HtmlUtil;

/**
 * 
 * <br>
 * <b>功能：</b>CustomExceptionHandler<br>
 * <b>作者：</b>wang_jiahao<br>
 * <b>日期：</b>2015年6月5日 上午11:01:17<br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class CustomExceptionHandler implements HandlerExceptionResolver {
	private final static Logger LOGGER = Logger.getLogger(CustomExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) {

		LOGGER.error(exception.getMessage(), exception);
		HtmlUtil.writerFailJsonByErrorCode(request, response, exception.getMessage());
		return null;
	}

}