package com.tencent.service;

import org.apache.log4j.Logger;

import com.hoperun.framework.exception.ServiceException;
import com.tencent.common.Configure;
import com.tencent.common.Util;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;

/**
 * User: rizenguo Date: 2014/10/29 Time: 16:04
 */
public class PayQueryService extends BaseService {
	
	private final static Logger LOGGER = Logger.getLogger(PayQueryService.class);
	
	public PayQueryService() throws IllegalAccessException,
			InstantiationException, ClassNotFoundException {
		super(Configure.PAY_QUERY_API);
	}

	/**
	 * 请求支付查询服务
	 * 
	 * @param scanPayQueryReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @return API返回的XML数据
	 * @throws Exception
	 */
	public String request(ScanPayQueryReqData scanPayQueryReqData)
			throws Exception {		

		// --------------------------------------------------------------------
		// 发送HTTPS的Post请求到API地址
		// --------------------------------------------------------------------
		String responseString = sendPost(scanPayQueryReqData);
		LOGGER.debug(responseString);
		return responseString;
	}

	/**
	 * 
	 * @param scanPayQueryReqData
	 * @return
	 * @throws ServiceException
	 */
	public ScanPayQueryResData requestObject(
			ScanPayQueryReqData scanPayQueryReqData) throws ServiceException {
		ScanPayQueryResData scanPayQueryResData;
		try {
			String payQueryServiceResponseString = request(scanPayQueryReqData);
			// 将从API返回的XML数据映射到Java对象
			scanPayQueryResData = (ScanPayQueryResData) Util.getObjectFromXML(
					payQueryServiceResponseString, ScanPayQueryResData.class);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		if (scanPayQueryResData == null
				|| scanPayQueryResData.getReturn_code() == null) {
			throw new ServiceException("支付订单查询请求逻辑错误，请仔细检测传过去的每一个参数是否合法");
		}

		if (scanPayQueryResData.getReturn_code().equals("FAIL")) {
			// 注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
			throw new ServiceException("支付订单查询API系统返回失败，失败信息为："
					+ scanPayQueryResData.getReturn_msg());
		} else {
			if (scanPayQueryResData.getResult_code().equals("SUCCESS")) {// 业务层成功
				return scanPayQueryResData;
			} else {
				throw new ServiceException("查询出错，错误码："
						+ scanPayQueryResData.getErr_code() + "     错误信息："
						+ scanPayQueryResData.getErr_code_des());
			}
		}
	}

	/**
	 * 
	 * @param scanPayQueryReqData
	 * @return
	 * @throws ServiceException
	 */
	public boolean isPaySuccess(ScanPayQueryReqData scanPayQueryReqData)
			throws ServiceException {
		ScanPayQueryResData scanPayQueryResData = requestObject(scanPayQueryReqData);
		if (scanPayQueryResData.getTrade_state().equals("SUCCESS")) {
			// 表示查单结果为“支付成功”
			return true;
		} else {
			// 支付不成功
			return false;
		}
	}

}
