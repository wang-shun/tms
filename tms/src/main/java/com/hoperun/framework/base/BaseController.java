package com.hoperun.framework.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.hoperun.framework.utils.HtmlUtil;

/**
 * <br>
 * <b>功能：</b>BaseController<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class BaseController {
	public final static String SUCCESS = "success";

	public final static String MSG = "msg";

	public final static String DATA = "data";

	public final static String LOGOUT_FLAG = "logoutFlag";

	@Autowired
	private MessageSource msg;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	}

	/**
	 * 获取IP地址
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 *
	 * 提示成功信息
	 *
	 * @param message
	 *
	 */
	public void sendSuccessMessage(HttpServletResponse response) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setSuccess(true);
		HtmlUtil.writerJson(response, responseMessage);
	}

	/**
	 *
	 * 提示成功信息
	 *
	 * @param message
	 *
	 */
	public void sendSuccessMessage(HttpServletResponse response, Object object) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setSuccess(true);
		responseMessage.setData(object);
		HtmlUtil.writerJson(response, responseMessage);
	}

	/**
	 *
	 * 提示失败信息
	 *
	 * @param message
	 *
	 */
	public void sendFailureMessageByCode(HttpServletRequest request,
			HttpServletResponse response, String errorCode) {
		HtmlUtil.writerFailJsonByErrorCode(request, response, errorCode);
	}

}
