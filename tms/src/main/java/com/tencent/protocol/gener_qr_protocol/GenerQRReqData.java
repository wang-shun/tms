package com.tencent.protocol.gener_qr_protocol;

/**
 * <br>
 * <b>功能：</b>GenerQRReqData<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Dec 4, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tencent.common.Configure;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;

/**
 * 请求统一支付API生成支付二维码需要提交的数据
 */
public class GenerQRReqData {

	// 每个字段具体的意思请查看API文档
	//
	private String appid = "";
	// 交易类型
	private String trade_type = "NATIVE";
	// 本机的Ip
	private String spbill_create_ip = "";
	// 商户根据自己业务传递的参数 必填
	private String product_id = "";
	// 要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
	private String body = "";
	// 商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
	private String out_trade_no = "";
	// 订单总金额，单位为“分”，只能整数
	private int total_fee = 0;
	// 支付成功后，回调地址
	private String notify_url = "";
	// 商户号
	private String mch_id = "";
	// 随机数
	private String nonce_str = "";
	// 根据微信签名规则，生成签名
	private String sign = "";

	/**
	 * 
	 * @param productId
	 *            商户根据自己业务传递的参数 必填
	 * @param body
	 *            要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
	 * @param outTradeNo
	 *            商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
	 * @param totalFee
	 *            订单总金额，单位为“分”，只能整数
	 * @param notifyUrl
	 *            支付成功后，回调地址
	 */
	public GenerQRReqData(String productId, String body, String outTradeNo,
			int totalFee) {
		// 微信分配的公众号ID（开通公众号之后可以获取到）
		setAppid(Configure.getInstance().getAppID());
		// 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		setMch_id(Configure.getInstance().getMchID());
		// 订单生成的机器IP
		setSpbill_create_ip(Configure.getInstance().getIp());
		// 支付成功后，回调地址
		setNotify_url(Configure.getInstance().getQrNotifyUrl());

		// 商户根据自己业务传递的参数 必填
		setProduct_id(productId);

		// 要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
		setBody(body);

		// 商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
		setOut_trade_no(outTradeNo);

		// 订单总金额，单位为“分”，只能整数
		setTotal_fee(totalFee);

		// 随机字符串，不长于32 位
		setNonce_str(RandomStringGenerator.getRandomStringByLength(32));

		// 根据API给的签名规则进行签名
		setSign(Signature.getSign(toMap()));// 把签名数据设置到Sign这个属性中

	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

}
