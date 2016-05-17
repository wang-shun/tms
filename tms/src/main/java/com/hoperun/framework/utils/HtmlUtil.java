package com.hoperun.framework.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.support.RequestContext;

import com.hoperun.framework.base.ResponseMessage;
import com.mysql.jdbc.StringUtils;

/**
 * <br>
 * <b>功能：</b>HtmlUtil<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class HtmlUtil {

	private static final Logger logger = Logger.getLogger(HtmlUtil.class);

	/**
	 * Utility class, create a private constructor to hide the implicit public
	 * one
	 */
	private HtmlUtil() {
	}

	/**
	 * @param response
	 * @param jsonStr
	 * @throws Exception
	 */
	public synchronized static void writerJson(HttpServletResponse response, String jsonStr) {
		writer(response, jsonStr);
	}

	public synchronized static void writerJson(HttpServletResponse response, Object object) {
		try {
			response.setContentType("application/json");
			writer(response, JacksonJsonUtil.beanToJson(object));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public synchronized static void writerSuccessJson(HttpServletResponse response) {
		try {
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setSuccess(true);
			responseMessage.setData("");
			response.setContentType("application/json");
			writer(response, JacksonJsonUtil.beanToJson(responseMessage));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public synchronized static void writerSuccessJson(HttpServletResponse response, Object object) {
		try {
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setSuccess(true);
			responseMessage.setData(object);

			response.setContentType("application/json");
			// writer(response, JSONUtil.toJSONString(responseMessage));
			writer(response, JacksonJsonUtil.beanToJson(responseMessage));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * author 徐寅
	 * @param request
	 * @param response
	 * @param object
	 */
	public synchronized static void writerJsonp(HttpServletRequest request,HttpServletResponse response, Object object) {
		try {
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setSuccess(true);
			responseMessage.setData(object);

			response.setContentType("application/json");
			String json=JacksonJsonUtil.beanToJson(responseMessage);
			String callback  = request.getParameter("callback") ;
			if(null!=callback && !callback.trim().equals("")){//跨域请求
				writer(response,callback+"("+json+");");
			}else{//本域访问
				writer(response, json);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public synchronized static void writerFailJson(HttpServletResponse response) {
		try {
			writerFailJson(response, "");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 根据键值对返回错误消息，用于国际化
	 * 
	 * @param request
	 *            用于获取用户所属Locale
	 * @param response
	 *            用于返回消息
	 * @param errorMessage
	 *            错误消息的key
	 * @param args
	 *            消息中使用到的参数
	 */
	public synchronized static void writeFallJsonByKey(HttpServletRequest request, HttpServletResponse response,
			String errorMessage, String[] args) {
		// 如果消息不为空，则先去参数列表中判断是否有对应的value，如果没有的话
		if (!StringUtils.isNullOrEmpty(errorMessage)) {
			try {
				errorMessage = SpringContextUtil.getApplicationContext().getMessage(errorMessage, args,
						request.getLocale());
			} catch (Exception e) {
				// e.printStackTrace();
				// 获取对应消息产生错误，直接使用原message
			}
		}
		writerFailJson(response, errorMessage);

	}

	public synchronized static void writerFailJson(HttpServletResponse response, String errorMessage) {

		try {
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setSuccess(false);
			responseMessage.setErrorMessage(errorMessage);
			response.setContentType("application/json");
			writer(response, JacksonJsonUtil.beanToJson(responseMessage));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public synchronized static void writerFailJsonp(HttpServletRequest request,HttpServletResponse response, String errorMessage) {

		try {
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setSuccess(false);
			responseMessage.setErrorMessage(errorMessage);
			response.setContentType("application/json");
			String callback  = request.getParameter("callback") ;
			String json=JacksonJsonUtil.beanToJson(responseMessage);
			writer(response,callback+"("+json+");");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public synchronized static void writerFailJsonByErrorCode(HttpServletRequest request, HttpServletResponse response,
			String errorCode) {
		try {
			String errorMessage = "";
			try {
				errorMessage = new RequestContext(request).getMessage(errorCode);
			} catch (NoSuchMessageException e) {
				errorMessage = errorCode;
			}

			writerFailJson(response, errorMessage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param url
	 */
	public synchronized static void writerRedirectJsonByUrl(HttpServletRequest request, HttpServletResponse response,
			String url) {
		try {
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setSuccess(true);
			responseMessage.setErrorMessage(url);
			response.setContentType("application/json");
			writer(response, JacksonJsonUtil.beanToJson(responseMessage));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * <br>
	 * <b>功能：</b>输出HTML代码<br>
	 * <b>作者：</b>王嘉豪<br>
	 * <b>日期：</b> Dec 14, 2011 <br>
	 * 
	 * @param response
	 * @param htmlStr
	 * @throws Exception
	 */
	public synchronized static void writerHtml(HttpServletResponse response, String htmlStr) {
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		writer(response, htmlStr);
	}

	private synchronized static void writer(HttpServletResponse response, String str) {
		try {
			// 设置页面不缓存
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = null;
			out = response.getWriter();
			out.print(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
