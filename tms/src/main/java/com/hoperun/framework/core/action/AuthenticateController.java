package com.hoperun.framework.core.action;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hoperun.framework.base.BaseController;
import com.hoperun.framework.constants.ErrorCodeConstants;
import com.hoperun.framework.core.bean.PublicKeyInfo;
import com.hoperun.framework.exception.LoginException;
import com.hoperun.framework.utils.RSAUtils;

/**
 * 
 * <br>
 * <b>功能：</b>AuthenticateController<br>
 * <b>作者：</b>wang_jiahao<br>
 * <b>日期：</b>2015年6月8日 下午4:44:55<br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
@Controller
@Scope("prototype")
@RequestMapping("/auth")
public class AuthenticateController extends BaseController {
	public final static String SPLIT_SIGN = ",";
	private final static Logger LOGGER = Logger
			.getLogger(AuthenticateController.class);

	/**
	 * 获取私钥，用于登录页面加密用户密码
	 * 
	 * @param id
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/privatekey", method = RequestMethod.GET)
	public void generatePrivateKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		if (session.getAttribute(RSAUtils.RSA_PRIVATE_KEY) != null
				&& session.getAttribute(RSAUtils.RSA_PUBLIC_KEY) != null) {
			sendSuccessMessage(response,
					session.getAttribute(RSAUtils.RSA_PUBLIC_KEY));
			return;
		}

		Map<String, Object> map = RSAUtils.getKeys();
		// 生成公钥和私钥
		RSAPublicKey publicKey = (RSAPublicKey) map
				.get(RSAUtils.RSA_PUBLIC_KEY);
		RSAPrivateKey privateKey = (RSAPrivateKey) map
				.get(RSAUtils.RSA_PRIVATE_KEY);

		// 公钥信息保存在页面，用于加密
		String publicKeyExponent = publicKey.getPublicExponent().toString(16);
		String publicKeyModulus = publicKey.getModulus().toString(16);

		PublicKeyInfo publicKeyInfo = new PublicKeyInfo(publicKeyExponent,
				publicKeyModulus);
		// 私钥保存在session中，用于解密
		session.setAttribute(RSAUtils.RSA_PRIVATE_KEY, privateKey);
		// public key保存在session中，用于解密
		session.setAttribute(RSAUtils.RSA_PUBLIC_KEY, publicKeyInfo);

		sendSuccessMessage(response, publicKeyInfo);
	}

	@RequestMapping(value = "/login/{loginInfo}", method = RequestMethod.GET)
	public void login(@PathVariable String loginInfo,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 判断当前用户Session中是否已经生成私钥
		HttpSession session = request.getSession(false);
		LOGGER.debug("接收到的密文:" + loginInfo);
		if (session == null) {
			// 用户之前没有获取过publicKey
			// session为空,return error message
			throw new LoginException(ErrorCodeConstants.SYS_MISS_SECURITY_KEY);
		} else {
			RSAPrivateKey privateKey = (RSAPrivateKey) request.getSession()
					.getAttribute(RSAUtils.RSA_PRIVATE_KEY);
			// 使用私钥对loginInfo进行解密

			String plainText = "";
			try {
				plainText = RSAUtils.decryptByPrivateKey(loginInfo, privateKey);
			} catch (Exception e) {
				LOGGER.error("RSA转换错误。", e);
				throw new LoginException(ErrorCodeConstants.SYS_LOGIN_CODE_FORMAT_ERROR);
			}

			if (!"".equals(plainText)) {
				plainText = new StringBuffer(plainText).reverse().toString();
			} else {
				throw new LoginException(ErrorCodeConstants.SYS_LOGIN_INFO_CANNOT_BLANK);
			}
			LOGGER.debug("解密后的明文是:" + plainText);
			// 分解解密后的loginInfo明文，区分出用户名和密码
			String[] authInfoArray = plainText.split(SPLIT_SIGN);
			if (authInfoArray.length != 2) {
				throw new LoginException(ErrorCodeConstants.SYS_LOGIN_INFO_ERROR);
			}
			// TODO 前往进行校验
			LOGGER.debug("用户名是:" + authInfoArray[0]);
			LOGGER.debug("密码是:" + authInfoArray[1]);
			// TODO 返回校验结果
			sendSuccessMessage(response, authInfoArray[0]);
		}

	}

}
