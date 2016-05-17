/**
 * @author 16020025
 *
 */
package com.hoperun.tms.constants;
public class ExWaybillLogConstants{
    public static final String W_STATUS_CONFIRMED= "0";//运单确认
    public static final String W_STATUS_CART_RECEIVE= "1";//大车接单
    public static final String W_STATUS_GENERATE_DELIVERY= "2";//生成配送单
    public static final String W_STATUS_GET_OFF_CONFIRMED= "3";//大车下车确认
    public static final String W_STATUS_ELECTROMBILE_RECEIVE= "4";//小车接单
    public static final String W_STATUS_SIGN= "5";//运单签收
    public static final String W_STATUS_CANCEL= "9";//作废
    
    public static final String W_LOG_CONFIRMED= "运单确认完成，";
    public static final String W_LOG_CART_RECEIVE= "运单大车扫码";
    public static final String W_LOG_GENERATE_DELIVERY= "生成配送单";
    public static final String W_LOG_GET_OFF_CONFIRMED= "大车下车确认";
    public static final String W_LOG_ELECTROMBILE_RECEIVE= "运单小车扫码";
    public static final String W_LOG_SIGN= "运单签收确认";
    public static final String W_LOG_CANCEL= "运单已经作废";
}