package com.hoperun.framework.sms;

import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.hoperun.framework.utils.StringUtil;

/**
 * <br>
 * <b>功能：</b>发送短消息到用户短信平台的客户端方法<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Dec 3, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public final class SMSClient {

	private final static String RESULT_MSG_SUCCESS = "发送成功";

	private final static Logger LOGGER = Logger.getLogger(SMSClient.class);

	private static final QName SERVICE_NAME = new QName("http://tempuri.org/",
			"Service1");

	private SMSClient() {
	}

	public static void main(String[] args) {
		java.lang.String _sendSMS_phone = "13501884463";
		java.lang.String _sendSMS_sTxt = "111111111，222222222，333333333，444444444";
		try {
			send(_sendSMS_phone, _sendSMS_sTxt);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param phone
	 *            接收短信的手机号码，只能是一个
	 * @param message
	 *            接受的短信消息,最长131个汉字
	 * @throws java.lang.Exception
	 */
	public static boolean send(final String phone, final String message)
			throws java.lang.Exception {
		URL wsdlURL = Service1.WSDL_LOCATION;

		Service1 ss = new Service1(wsdlURL, SERVICE_NAME);
		Service1Soap port = ss.getService1Soap();

		{
			LOGGER.debug("Invoking sendSMS...");
			if(StringUtil.isEmpty(phone)){
				throw new Exception("手机号不能为空");
			}
			java.lang.String _sendSMS_phone = phone;
			java.lang.String _sendSMS_sTxt = message;
			java.lang.String _sendSMS__return = port.sendSMS(_sendSMS_phone,
					_sendSMS_sTxt);
			LOGGER.debug("sendSMS.result=" + _sendSMS__return);
			if (RESULT_MSG_SUCCESS.equals(_sendSMS__return)) {
				return true;
			}
			return false;

		}
	}

}
