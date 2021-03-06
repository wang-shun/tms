package com.hoperun.tms.service.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.hoperun.tms.bean.base.WxConfig;
import com.hoperun.tms.util.TmsConstants;

@Service
public class WxConfigService {

	public static String post(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		HttpPost post = postForm(url, params);
		body = invoke(httpclient, post);
		httpclient.getConnectionManager().shutdown();
		return body;
	}

	public static String get(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);
		httpclient.getConnectionManager().shutdown();
		return body;
	}

	private static String invoke(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		return body;
	}

	private static String paseResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		String charset = EntityUtils.getContentCharSet(entity);
		String body = null;
		try {
			body = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static HttpPost postForm(String url, Map<String, String> params) {
		
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return httpost;
	}

	 public WxConfig getWxConfig(String source){
		//获取access_token
		Map<String, String> params = new HashMap<String, String>();
		String appid=TmsConstants.appId;
		String secret=TmsConstants.secret;
		params.put("grant_type","client_credential");
		params.put("appid",appid);
		params.put("secret",secret);
		String xml = WxConfigService.post("https://api.weixin.qq.com/cgi-bin/token",params);
		JSONObject jsonMap  = JSONObject.fromObject(xml);
		Map<String, String> map = new HashMap<String, String>();
	    Iterator<String> it = jsonMap.keys();  
	    while(it.hasNext()) {  
	        String key = (String) it.next();  
	        String u = jsonMap.get(key).toString();
	        map.put(key, u);  
	    }
	    String access_token = map.get("access_token");
	    System.out.println("access_token=" + access_token);
	    
	    //获取ticket
	    params.put("type","jsapi");
	    params.put("access_token",access_token);
	    xml = WxConfigService.post("https://api.weixin.qq.com/cgi-bin/ticket/getticket",params); 
	    jsonMap  = JSONObject.fromObject(xml);
		map = new HashMap<String, String>();
	    it = jsonMap.keys();  
	    while(it.hasNext()) {  
	        String key = (String) it.next();  
	        String u = jsonMap.get(key).toString();
	        map.put(key, u);  
	    }
	    String jsapi_ticket = map.get("ticket");
	    System.out.println("jsapi_ticket=" + jsapi_ticket);
	    
	    //获取签名signature
	    String noncestr = UUID.randomUUID().toString();
	    String timestamp = Long.toString(System.currentTimeMillis() / 1000);
	    String url="";
	    if(source.trim().equals("1")){
	    	url=TmsConstants.url_1;
	    }
	    if(source.trim().equals("2")){
	    	url=TmsConstants.url_2;
	    }
	    String str = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + noncestr +
                "&timestamp=" + timestamp +
                "&url=" + url;
	    WxConfig bean=new WxConfig();
	    bean.setAppId(appid);
	    bean.setNoncestr(noncestr);
	    bean.setTimestamp(timestamp);
	    bean.setSignature(SHA1(str));
	    return bean;
	}
	
	   /** 
     * @author：徐寅
     * @date： 2016年04月20日 上午9:24:43 
     * @description： SHA、SHA1加密
     * @parameter：   str：待加密字符串
     * @return：  加密串
    **/
    public static String SHA1(String str) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
