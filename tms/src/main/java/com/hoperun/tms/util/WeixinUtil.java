package com.hoperun.tms.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONObject;
/**
 * User: xuyin
 * Date: 2016/04/06
 * Time: 17:11
 */
public class WeixinUtil {
	
	public static String getOpenId(String appid, String secret, String code ) {
        String o_auth_openid_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code;";
        String requestUrl = o_auth_openid_url.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
        StringBuffer buffer=new StringBuffer();
        JSONObject jsonObject = null;
		try {
			buffer = httpRequest(requestUrl, "GET", null);
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (Exception e) {
			// 获取token失败
			e.printStackTrace();
		}
        return jsonObject.getString("openid");
    }
	
	private static StringBuffer httpRequest(String requestUrl, String requestMethod, String output) throws Exception {
		URL url = new URL(requestUrl);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod(requestMethod);
		if (null != output) {
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(output.getBytes("UTF-8"));
			outputStream.close();
		}

		// 从输入流读取返回内容
		InputStream inputStream = connection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		StringBuffer buffer = new StringBuffer();
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}

		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		connection.disconnect();
		return buffer;
	}
}
