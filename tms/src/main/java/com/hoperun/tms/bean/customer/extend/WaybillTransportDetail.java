package com.hoperun.tms.bean.customer.extend;

import java.util.Date;

/**
 * Created by 16030117 on 2016/4/11.
 */
public class WaybillTransportDetail {

    public static final String SOURCE_YIMI_BILL = "1";
    public static final String SOURCE_FARM = "2";

    private Long id;
    private String no;//运单号
    private String status;
    private String dNameBig;
    private String vNoBig;//大车车牌号
    private String deliveryTerm;//集散点
    private String vNoSmall;//小车车牌号
    private String dNameSmall;//配送人
    private Date signTime;//签收时间
    private String wLog;//订单跟踪日志
    private String wStatus;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the vNoBig
	 */
	public String getvNoBig() {
		return vNoBig;
	}
	/**
	 * @param vNoBig the vNoBig to set
	 */
	public void setvNoBig(String vNoBig) {
		this.vNoBig = vNoBig;
	}
	/**
	 * @return the deliveryTerm
	 */
	public String getDeliveryTerm() {
		return deliveryTerm;
	}
	/**
	 * @param deliveryTerm the deliveryTerm to set
	 */
	public void setDeliveryTerm(String deliveryTerm) {
		this.deliveryTerm = deliveryTerm;
	}
	/**
	 * @return the vNoSmall
	 */
	public String getvNoSmall() {
		return vNoSmall;
	}
	/**
	 * @param vNoSmall the vNoSmall to set
	 */
	public void setvNoSmall(String vNoSmall) {
		this.vNoSmall = vNoSmall;
	}
	/**
	 * @return the dNameSmall
	 */
	public String getdNameSmall() {
		return dNameSmall;
	}
	/**
	 * @param dNameSmall the dNameSmall to set
	 */
	public void setdNameSmall(String dNameSmall) {
		this.dNameSmall = dNameSmall;
	}
	/**
	 * @return the signTime
	 */
	public Date getSignTime() {
		return signTime;
	}
	/**
	 * @param signTime the signTime to set
	 */
	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
	/**
	 * @return the wLog
	 */
	public String getwLog() {
		return wLog;
	}
	/**
	 * @param wLog the wLog to set
	 */
	public void setwLog(String wLog) {
		this.wLog = wLog;
	}
	/**
	 * @return the sourceYimiBill
	 */
	public static String getSourceYimiBill() {
		return SOURCE_YIMI_BILL;
	}
	/**
	 * @return the sourceFarm
	 */
	public static String getSourceFarm() {
		return SOURCE_FARM;
	}
	/**
	 * @return the no
	 */
	public String getNo() {
		return no;
	}
	/**
	 * @param no the no to set
	 */
	public void setNo(String no) {
		this.no = no;
	}
	/**
	 * @return the dNameBig
	 */
	public String getdNameBig() {
		return dNameBig;
	}
	/**
	 * @param dNameBig the dNameBig to set
	 */
	public void setdNameBig(String dNameBig) {
		this.dNameBig = dNameBig;
	}
	/**
	 * @return the wStatus
	 */
	public String getwStatus() {
		return wStatus;
	}
	/**
	 * @param wStatus the wStatus to set
	 */
	public void setwStatus(String wStatus) {
		this.wStatus = wStatus;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

    

}
