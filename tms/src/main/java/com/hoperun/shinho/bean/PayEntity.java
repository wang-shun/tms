package com.hoperun.shinho.bean;

import com.hoperun.framework.constants.SystemConstants;

/**
 * <br>
 * <b>功能：</b>支付实体<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Dec 8, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class PayEntity {

	/**
	 * 支付类型 <br>
	 * <b>功能：</b>PayType<br>
	 * <b>作者：</b>wang_jiahao<br>
	 * <b>日期：</b>2015年12月8日 下午2:33:32<br>
	 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
	 */
	public enum PayType {
		ALIPAY, WXPAY, UNIONPAY;
	}

	public static void main(String[] args) {
		System.out.println(PayType.ALIPAY.name());
	}

	public PayEntity(String orderId, String payType) {
		this.orderId = orderId;
		if (PayType.ALIPAY.name().equalsIgnoreCase(payType)) {
			this.payType = PayType.ALIPAY;
		} else if (PayType.WXPAY.name().equalsIgnoreCase(payType)) {
			this.payType = PayType.WXPAY;
		} else if (PayType.UNIONPAY.name().equalsIgnoreCase(payType)) {
			this.payType = PayType.UNIONPAY;
		}
		this.orderName = SystemConstants.getInstance().getPAY_ORDER_NAME()
				+ this.orderId;
	}

	// 订单号 必填
	private String orderId;
	// 订单名称 必填
	private String orderName;
	// 金额 必填（以分为单位）
	private int total_fee;
	// 订单描述
	private String orderDesc = SystemConstants.getInstance()
			.getPAY_ORDER_DESC();

	private PayType payType;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

}
