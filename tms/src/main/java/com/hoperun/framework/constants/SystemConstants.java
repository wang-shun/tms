package com.hoperun.framework.constants;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationObjectSupport;

/**
 * <br>
 * <b>功能：</b>SystemConstants<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class SystemConstants {

	private final static Logger LOGGER = Logger.getLogger(SystemConstants.class);

	private static boolean inited = false;
	private static SystemConstants instance;

    // 支付订单名称
    private String PAY_ORDER_NAME;
    
    // 支付订单描述
    private String PAY_ORDER_DESC;
    
	public synchronized static SystemConstants getInstance() {
		if (!inited) {
			instance = new SystemConstants();
		}
		return instance;
	}

	private SystemConstants() {
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("/system.properties");
		try {
			prop.load(in);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		// 图片存储路径
		PIC_STORE_FOLDER = prop.getProperty("pic_store_folder");
		if (PIC_STORE_FOLDER == null || "".equals(PIC_STORE_FOLDER)) {
			LOGGER.error("System config parameter PIC_STORE_FOLDER is NULL!!!!");
		} else {
			LOGGER.info("System config parameter PIC_STORE_FOLDER is:" + PIC_STORE_FOLDER);
		}

		inited = true;

	}

    public String getPAY_ORDER_NAME()
    {
        return PAY_ORDER_NAME;
    }
    
    public String getPAY_ORDER_DESC()
    {
        return PAY_ORDER_DESC;
    }
    
	// 显示前一日或者次日的营业时段 1为前一日，2为次日
	public static final int IS_NEXT_DAY = 0;

	public static final int NUMBER_NULL = -1;
	// 是否显示前一日
	// public static final boolean IS_SHOW_DAY=true;

	// 图片存储路径，当前是本地路径
	private String PIC_STORE_FOLDER;

	// 所以文件存放根目录
	public static String INDEX_BASE_DIR;

	public static final String ORDER_BY_PREFIX = " order by ";
	public static final String IMAGE_SURFIX = ".jpg";

	/**
	 * 字段中标识“是”和“否”使用的常量 <br>
	 * <b>功能：</b>YesOrNo<br>
	 * <b>作者：</b>wang_jiahao<br>
	 * <b>日期：</b>2016年3月23日 上午10:52:32<br>
	 * <b>版权所有：<b>版权所有(C) 2016，www.hoperun.com<br>
	 */
	public enum YesOrNo {
		NO(0, "否"), YES(1, "是");

		private int code;
		private String desc;

		private YesOrNo(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}

	/**
	 * 上传图片的类型 <br>
	 * <b>功能：</b>UploadImgType<br>
	 * <b>作者：</b>wang_jiahao<br>
	 * <b>日期：</b>2016年3月23日 上午10:52:32<br>
	 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
	 */
	public enum UploadImgType {
		FOOD(1, "菜品", "FOODPIC"), AD(2, "客显屏图片", "ADPIC");

		private int code;
		private String desc;
		private String folderName;

		private UploadImgType(int code, String desc, String folderName) {
			this.code = code;
			this.desc = desc;
			this.folderName = folderName;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

		public String getFolderName() {
			return folderName;
		}

		@Override
		public String toString() {

			return String.valueOf(this.code);

		}
	}

	/**
	 * <br>
	 * <b>功能：</b>操作权限的类型，1：总部后台管理 2：门店后台管理 3：POS <br>
	 * <b>作者：</b>wang_jiahao<br>
	 * <b>日期：</b>2016年3月23日 上午10:52:32<br>
	 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
	 */
	public enum OperateType {
		HEADQUARTER(1, "总部"), RESTAURANT(2, "门店"), POS(3, "POS");

		private int code;
		private String desc;

		private OperateType(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}

	/**
	 * <br>
	 * <b>功能：</b>角色权限的类型，1：总部后台管理 2：门店后台管理<br>
	 * <b>作者：</b>wang_jiahao<br>
	 * <b>日期：</b>2016年3月24日 上午10:52:32<br>
	 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
	 */
	public enum RuleType {
		HEADQUARTER(1, "总部"), RESTAURANT(2, "门店");

		private int code;
		private String desc;

		private RuleType(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}

	public enum PayStatus {
		NOT("0", "未支付"), SUCCESSFUL("1", "已支付");

		private String statusCode;
		private String statusDesc;

		private PayStatus(String statusCode, String statusDesc) {
			this.statusCode = statusCode;
			this.statusDesc = statusDesc;
		}

		public String getCode() {
			return statusCode;
		}

		public String getDesc() {
			return statusDesc;
		}
	}

	public enum PayApp {
		ALIPAY("alipay", "支付宝"), WXPAY("wxpayjsapi", "微信"), UNIONPAY("upacp", "银联支付");

		private String appCode;
		private String appDesc;

		private PayApp(String appCode, String appDesc) {
			this.appCode = appCode;
			this.appDesc = appDesc;
		}

		public String getCode() {
			return appCode;
		}

		public String getDesc() {
			return appDesc;
		}
	}

	public String getPIC_STORE_FOLDER() {
		return PIC_STORE_FOLDER;
	}

}