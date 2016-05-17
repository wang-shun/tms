package com.hoperun.tms.bean.customer.extend;

import com.hoperun.framework.utils.DateUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.customer.Waybill;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/11.
 */
public class ExWaybill extends Waybill {

    public static final String SOURCE_YIMI_BILL = "1";
    public static final String SOURCE_FARM = "2";

    public static final String STATUS_CREATED = "00";
    public static final String STATUS_CONFIRMED = "10";
    public static final String STATUS_PREDISTRIBUTION = "15";
    public static final String STATUS_TRUCK_DELIVERING = "20";
    public static final String STATUS_XC = "25";  
    public static final String STATUS_POWERCAR_DELIVERING = "30";
    public static final String STATUS_DELIVERED = "40";
    public static final String STATUS_CANCELED = "90";

    public static final String UNSCANED = "00";
    public static final String SCANED_NOT_CONFIRM = "10";
    public static final String SCANED_AND_CONFIRMED = "20";

    public static final String BY_THE_MONTH = "1";
    public static final String PAY_CASH = "2";

    public static final String UNLIQUIDATED = "1";  //未结算
    public static final String LIQUIDATED = "2";    //已结算

    public static final String BILLLOG_1 = "1";
    public static final String BILLLOG_2 = "2";
    public static final String BILLLOG_3 = "3";
    public static final String BILLLOG_4 = "4";
    public static final String BILLLOG_5 = "5";
    
    private String receiveAtStr;
    private String deliveryDateStr;
    private String sourceDesc;
    private String deliveryTypeDesc;
    private String deliveryTimeDesc;
    private String feeTypeDesc;
    private String payTypeDesc;
    private String statusDesc;
    private String tFeeStatusDesc;
    private String dAddressDetl;
    private String rAddressDetl;
    private String deliveryDuration;
    private String remark1;
    private String remark2;
    private String remark3;

    public String getdAddressDetl() {
        return dAddressDetl;
    }

    public void setdAddressDetl(String dAddressDetl) {
        this.dAddressDetl = dAddressDetl;
    }

    public String getrAddressDetl() {
        return rAddressDetl;
    }

    public void setrAddressDetl(String rAddressDetl) {
        this.rAddressDetl = rAddressDetl;
    }

    public String getDeliveryTypeDesc() {
        return deliveryTypeDesc;
    }

    public void setDeliveryTypeDesc(String deliveryTypeDesc) {
        this.deliveryTypeDesc = deliveryTypeDesc;
    }

    public void setDeliveryType(String deliveryType) {
        super.setDeliveryType(deliveryType);
        switch (deliveryType){
            case "1":
                setDeliveryTypeDesc("冷藏");
        }
    }

    public String getDeliveryTimeDesc() {
        return deliveryTimeDesc;
    }

    public void setDeliveryTimeDesc(String deliveryTimeDesc) {
        this.deliveryTimeDesc = deliveryTimeDesc;
    }

    public void setDeliveryTime(String deliveryTime) {
        super.setDeliveryTime(deliveryTime);
        switch (deliveryTime.toLowerCase()){
            case "am":
                setDeliveryTimeDesc("上午"); break;
            case "pm":
                setDeliveryTimeDesc("下午"); break;
        }
    }

    public String getFeeTypeDesc() {
        return feeTypeDesc;
    }

    public void setFeeTypeDesc(String feeTypeDesc) {
        this.feeTypeDesc = feeTypeDesc;
    }

    public void setFeeType(String feeType) {
        super.setFeeType(feeType);
        switch (feeType){
            case "1":
                setFeeTypeDesc("整件");break;
            case "2":
                setFeeTypeDesc("重量（公斤）");break;
            case "3":
                setFeeTypeDesc("体积（立方米）");break;
        }
    }

    public String getPayTypeDesc() {
        return payTypeDesc;
    }

    public void setPayTypeDesc(String payTypeDesc) {
        this.payTypeDesc = payTypeDesc;
    }

    public void setPayType(String payType) {
        super.setPayType(payType);
        switch (payType){
            case BY_THE_MONTH:
                setPayTypeDesc("月结");break;
            case PAY_CASH:
                setPayTypeDesc("现结");break;
        }
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }

    public void setSource(String source) {
        super.setSource(source);
        switch (source){
            case SOURCE_YIMI_BILL:
                setSourceDesc("一米市集");break;
            case SOURCE_FARM:
                setSourceDesc("农场");break;
            case "3":
                setSourceDesc("一米外送");break;
        }
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setStatus(String status) {
        super.setStatus(status);
        switch (status){
            case STATUS_CREATED:
                setStatusDesc("新建");break;
            case STATUS_CONFIRMED:
                setStatusDesc("确认");break;
            case STATUS_TRUCK_DELIVERING:
                setStatusDesc("大车运输");break;
            case STATUS_POWERCAR_DELIVERING:
                setStatusDesc("小车配送");break;
            case STATUS_DELIVERED:
                setStatusDesc("签收");break;
            case STATUS_CANCELED:
                setStatusDesc("作废");break;
        }
    }

    public String gettFeeStatusDesc() {
        return tFeeStatusDesc;
    }

    public void settFeeStatusDesc(String tFeeStatusDesc) {
        this.tFeeStatusDesc = tFeeStatusDesc;
    }

    public void settFeeStatus(String tFeeStatus){
        super.settFeeStatus(tFeeStatus);
        switch (tFeeStatus){
            case UNLIQUIDATED:
                settFeeStatusDesc("未结算");break;
            case LIQUIDATED:
                settFeeStatusDesc("已结算");break;
        }
    }

    public String getReceiveAtStr() {
        return receiveAtStr;
    }

    public void setReceiveAtStr(String receiveAtStr) {
        this.receiveAtStr = receiveAtStr;
    }

	public String getDeliveryDateStr() {
		return deliveryDateStr;
	}

	public void setDeliveryDateStr(String deliveryDateStr) {
		this.deliveryDateStr = deliveryDateStr;
	}
    public void setReceiveAt(Date receiveAt) {
        setReceiveAtStr(new SimpleDateFormat("yyyy-MM-dd").format(receiveAt));
        super.setReceiveAt(receiveAt);
    }
    
    public void setDeliveryDate(Date deliveryDate) {
    	setDeliveryDateStr(new SimpleDateFormat("yyyy-MM-dd").format(deliveryDate));
    	super.setDeliveryDate(deliveryDate);
    }

    public String getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(String deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public void setFromMap(Map map) throws Exception {
        setId(map.get("id") == null ? 0 : Long.parseLong(map.get("id").toString()));
        String dName = map.get("dName") == null ? "" : map.get("dName").toString();
        if (dName.indexOf("-") > 0) {
            setdId(Long.parseLong(dName.split("-")[0]));
            setdName(dName.split("-")[1]);
        }
        setrArea(map.get("rArea") == null ? "" : map.get("rArea").toString());
        setrAddress(map.get("rAddress") == null ? "" : map.get("rAddress").toString());
        setRecipient(map.get("recipient") == null ? "" : map.get("recipient").toString());
        setRemark(map.get("remark") == null ? "" : map.get("remark").toString());
        setReceiveAt(map.get("receiveAtStr") == null ? null :
                DateUtil.parseFormatIso8601Date(map.get("receiveAtStr").toString()));
    }

    public void setFromRequestParams(HttpServletRequest request) throws Exception {
        setId(Long.parseLong(StringUtil.getNotNullStr(request.getParameter("id"))));
        setrArea(StringUtil.getNotNullStr(request.getParameter("rArea")));
        setrAddress(StringUtil.getNotNullStr(request.getParameter("rAddress")));
        setRecipient(StringUtil.getNotNullStr(request.getParameter("recipient")));
        setRemark(StringUtil.getNotNullStr(request.getParameter("remark")));
        if (!StringUtil.isEmpty(request.getParameter("receiveAtStr"))) {
            setReceiveAt(DateUtil.parseFormatIso8601Date(request.getParameter("receiveAtStr")));
        }
    }

	/**
	 * @return the remark1
	 */
	public String getRemark1() {
		return remark1;
	}

	/**
	 * @param remark1 the remark1 to set
	 */
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	/**
	 * @return the remark2
	 */
	public String getRemark2() {
		return remark2;
	}

	/**
	 * @param remark2 the remark2 to set
	 */
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	/**
	 * @return the remark3
	 */
	public String getRemark3() {
		return remark3;
	}

	/**
	 * @param remark3 the remark3 to set
	 */
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
    
}
