package com.tencent.service;

import com.tencent.common.Configure;
import com.tencent.protocol.gener_qr_protocol.GenerQRReqData;

/**
 * <br>
 * <b>功能：</b>微信统一下单API,添加的目的是为了支持扫码支付<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Dec 4, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class UnifiedOrderService extends BaseService {

	public UnifiedOrderService() throws IllegalAccessException,
			InstantiationException, ClassNotFoundException {
		super(Configure.UNIFIED_ORDER_API);
	}

	/**
	 * 请求支付服务
	 * 
	 * @param generQRReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的数据
	 * @throws Exception
	 */
	public String request(GenerQRReqData generQRReqData) throws Exception {

		// --------------------------------------------------------------------
		// 发送HTTPS的Post请求到API地址
		// --------------------------------------------------------------------
		String responseString = sendPost(generQRReqData);

		return responseString;
	}
}
