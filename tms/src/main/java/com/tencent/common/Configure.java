package com.tencent.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.hoperun.framework.utils.StringUtil;

/**
 * User: rizenguo Date: 2014/10/29 Time: 14:40 这里放置各种配置数据
 */
public class Configure {

	private final static Logger LOGGER = Logger.getLogger(Configure.class);

	private static final String FILE_NAME = "wxpay.properties";
	/** 操作对象. */
	private static Configure config;

	// 这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	private String key = "";

	// 微信分配的公众号ID（开通公众号之后可以获取到）
	private String appID = "";

	// 微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	private String mchID = "";

	// 受理模式下给子商户分配的子商户号
	private String subMchID = "";

	// HTTPS证书的本地路径
	private String certLocalPath = "";

	// HTTPS证书密码，默认密码等于商户号MCHID
	private String certPassword = "";

	// 是否使用异步线程的方式来上报API测速，默认为异步模式
	private static boolean useThreadToDoReport = true;

	// 机器IP
	private String ip = "";

	//支付二维码支付成功后通知的后台服务地址
	private String qrNotifyUrl = "";

	// 以下是几个API的路径：
	// 1）被扫支付API
	public static String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";

	// 2）被扫支付查询API
	public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

	// 3）退款API
	public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	// 4）退款查询API
	public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

	// 5）撤销API
	public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";

	// 6）下载对账单API
	public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";

	// 7) 统计上报API
	public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";

	// 8) 统一下单API
	public static String UNIFIED_ORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	public static boolean isUseThreadToDoReport() {
		return useThreadToDoReport;
	}

	public static void setUseThreadToDoReport(boolean useThreadToDoReport) {
		Configure.useThreadToDoReport = useThreadToDoReport;
	}

	public static String HttpsRequestClassName = "com.tencent.common.HttpsRequest";

	public static void setHttpsRequestClassName(String name) {
		HttpsRequestClassName = name;
	}

	public String getKey() {
		return key;
	}

	public String getAppID() {
		return appID;
	}

	public String getMchID() {
		return mchID;
	}

	public String getSubMchID() {
		return subMchID;
	}

	public String getCertLocalPath() {
		return certLocalPath;
	}

	public String getCertPassword() {
		return certPassword;
	}

	public String getIp() {
		return ip;
	}

	/**
	 * 获取config对象.
	 * 
	 * @return
	 */
	public synchronized static Configure getInstance() {
		if (null == config) {
			LOGGER.debug("create Configure instance!");
			config = new Configure();
		}
		return config;
	}
	
	private Configure(){
		loadPropertiesFromSrc();
	}

	/**
	 * 从classpath路径下加载配置参数
	 */
	private void loadPropertiesFromSrc() {
		Properties properties = new Properties();
		InputStream in = null;
		try {
			// Properties pro = null;
			in = Configure.class.getClassLoader()
					.getResourceAsStream(FILE_NAME);
			if (null != in) {
				BufferedReader bf = new BufferedReader(new InputStreamReader(
						in, "utf-8"));
				LOGGER.debug("load unipay properties!");
				try {
					properties.load(bf);
				} catch (IOException e) {
					throw e;
				}
			} else {
				LOGGER.error(FILE_NAME + "文件不存在!");
				return;
			}
			loadProperties(properties);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据传入的 {@link #load(java.util.Properties)}对象设置配置参数
	 * 
	 * @param pro
	 */
	private void loadProperties(final Properties pro) {
		String value = null;

		value = pro.getProperty("appID");
		if (!StringUtil.isEmpty(value)) {
			LOGGER.debug("appID=" + value);
			this.appID = value.trim();
		}
		value = pro.getProperty("mchID");
		if (!StringUtil.isEmpty(value)) {
			LOGGER.debug("mchID=" + value);
			this.mchID = value.trim();
		}
		value = pro.getProperty("certLocalPath");
		if (!StringUtil.isEmpty(value)) {
			LOGGER.debug("certLocalPath=" + value);
			this.certLocalPath = value.trim();
		}
		value = pro.getProperty("certPassword");
		if (!StringUtil.isEmpty(value)) {
			LOGGER.debug("certPassword=" + value);
			this.certPassword = value.trim();
		}
		value = pro.getProperty("key");
		if (!StringUtil.isEmpty(value)) {
			LOGGER.debug("key=" + value);
			this.key = value.trim();
		}
		value = pro.getProperty("qrNotifyUrl");
		if (!StringUtil.isEmpty(value)) {
			LOGGER.debug("qrNotifyUrl=" + value);
			this.qrNotifyUrl = value.trim();
		}
		
		ip = localIp();
	}

	/**
	 * 获取本机Ip
	 * 
	 * 通过 获取系统所有的networkInterface网络接口 然后遍历 每个网络下的InterfaceAddress组。 获得符合
	 * <code>InetAddress instanceof Inet4Address</code> 条件的一个IpV4地址
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String localIp() {
		String ip = null;
		Enumeration allNetInterfaces;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				List<InterfaceAddress> InterfaceAddress = netInterface
						.getInterfaceAddresses();
				for (InterfaceAddress add : InterfaceAddress) {
					InetAddress Ip = add.getAddress();
					if (Ip != null && Ip instanceof Inet4Address) {
						ip = Ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			// logger.warn("获取本机Ip失败:异常信息:" + e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.debug("Get local IP address：" + ip);
		return ip;
	}

	public String getQrNotifyUrl() {
		return qrNotifyUrl;
	}
}
