package com.tencent.business;

import org.slf4j.LoggerFactory;

import com.hoperun.framework.exception.ServiceException;
import com.tencent.common.Log;
import com.tencent.common.Signature;
import com.tencent.common.Util;
import com.tencent.protocol.gener_qr_protocol.GenerQRReqData;
import com.tencent.protocol.gener_qr_protocol.GenerQRResData;
import com.tencent.service.UnifiedOrderService;

/**
 * <br>
 * <b>功能：</b>扫码支付<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Dec 4, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class GenerQRBusiness {

	public GenerQRBusiness() throws IllegalAccessException,
			ClassNotFoundException, InstantiationException {
		unifiedOrderService = new UnifiedOrderService();
	}

	// 打log用
	private static Log log = new Log(
			LoggerFactory.getLogger(GenerQRBusiness.class));

	private UnifiedOrderService unifiedOrderService;

	/**
	 * 直接执行被扫支付业务逻辑（包含最佳实践流程）
	 *
	 * @param scanPayReqData
	 *            这个数据对象里面包含了API要求提交的各种数据字段
	 * @param resultListener
	 *            商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
	 * @throws Exception
	 */
	public String getQR(GenerQRReqData generQRReqData) throws Exception {

		// --------------------------------------------------------------------
		// 构造请求“被扫支付API”所需要提交的数据
		// --------------------------------------------------------------------

		String outTradeNo = generQRReqData.getOut_trade_no();

		// 接受API返回
		String payServiceResponseString;

		long costTimeStart = System.currentTimeMillis();

		log.i("支付API返回的数据如下：");
		payServiceResponseString = unifiedOrderService.request(generQRReqData);

		long costTimeEnd = System.currentTimeMillis();
		long totalTimeCost = costTimeEnd - costTimeStart;
		log.i("api请求总耗时：" + totalTimeCost + "ms");

		// 打印回包数据
		log.i(payServiceResponseString);

		// 将从API返回的XML数据映射到Java对象
		GenerQRResData generQRResData = (GenerQRResData) Util.getObjectFromXML(
				payServiceResponseString, GenerQRResData.class);

		if (generQRResData == null || generQRResData.getReturn_code() == null) {
			log.e("【支付失败】支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
			throw new ServiceException(
					"【支付失败】支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
		}

		if (generQRResData.getReturn_code().equals("FAIL")) {
			// 注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
			log.e("【支付失败】支付API系统返回失败，请检测Post给API的数据是否规范合法");
			throw new ServiceException("【支付失败】支付API系统返回失败，请检测Post给API的数据是否规范合法");
		} else {
			log.i("支付API系统成功返回数据");
			// --------------------------------------------------------------------
			// 收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
			// --------------------------------------------------------------------
			if (!Signature
					.checkIsSignValidFromResponseString(payServiceResponseString)) {
				log.e("【支付失败】支付请求API返回的数据签名验证失败，有可能数据被篡改了");
				throw new ServiceException("【支付失败】支付请求API返回的数据签名验证失败，有可能数据被篡改了");
			}

			// 获取错误码
			String errorCode = generQRResData.getErr_code();
			// 获取错误描述
			String errorCodeDes = generQRResData.getErr_code_des();

			if (generQRResData.getResult_code().equals("SUCCESS")) {
				//
				return generQRResData.getCode_url();
			} else {
				// 出现业务错误
				log.i("业务返回失败");
				log.i("err_code:" + errorCode);
				log.i("err_code_des:" + errorCodeDes);
				throw new ServiceException("业务返回失败。err_code：" + errorCode
						+ "err_code_des:" + errorCodeDes);
			}
		}
	}

	public UnifiedOrderService getUnifiedOrderService() {
		return unifiedOrderService;
	}

	public void setUnifiedOrderService(UnifiedOrderService unifiedOrderService) {
		this.unifiedOrderService = unifiedOrderService;
	}

	public static void main(String[] args) {
		try {
			GenerQRBusiness generQRBusiness = new GenerQRBusiness();

			/**
			 * @param productId
			 *            商户根据自己业务传递的参数 必填
			 * @param body
			 *            要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
			 * @param outTradeNo
			 *            商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
			 * @param totalFee
			 *            订单总金额，单位为“分”，只能整数
			 */
			GenerQRReqData generQRReqData = new GenerQRReqData("productid",
					"body", "outtradeno", 1);
			String result = generQRBusiness.getQR(generQRReqData);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
