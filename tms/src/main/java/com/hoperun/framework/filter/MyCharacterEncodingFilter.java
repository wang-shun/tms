package com.hoperun.framework.filter;

import java.io.IOException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * <br>
 * <b>功能：</b>MyCharacterEncodingFilter<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public class MyCharacterEncodingFilter extends CharacterEncodingFilter {

	private String encoding;
	private boolean forceEncoding = false;

	@Override
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @return the forceEncoding
	 */
	public boolean isForceEncoding() {
		return forceEncoding;
	}

	@Override
	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String charsetEncoding = this.encoding;
		String accept = request.getHeader("Accept");
		// application/json;charset=UTF-8
		boolean isSet = false;
		if (accept != null && accept.indexOf("charset=") > 1) {
			String temp = accept.substring(accept.indexOf("charset=") + 8);
			try {
				charsetEncoding = temp;
				isSet = true;
			} catch (UnsupportedCharsetException ex) {
				// 不支持
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING,
						"不支持的字符编码：" + ex.getCharsetName(), ex);
			} catch (IllegalCharsetNameException ex) {
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING,
						"非法的字符编码名称：" + ex.getCharsetName(), ex);
			}
		}
		if (!isSet) {
			String acceptCharset = request.getHeader("Accept-Charset");
			if (acceptCharset != null) {
				String[] charsets = acceptCharset.split(",");
				SET_CHARSET: for (String cha : charsets) {
					try {
						String temp = cha;
						if (temp.indexOf(";") > 0) {
							temp = temp.substring(0, temp.indexOf(";"));
						}
						charsetEncoding = temp;
						isSet = true;
						break SET_CHARSET;
					} catch (UnsupportedCharsetException ex) {
						Logger.getLogger(this.getClass().getName()).log(
								Level.WARNING,
								"不支持的字符编码：" + ex.getCharsetName(), ex);
					} catch (IllegalCharsetNameException ex) {
						Logger.getLogger(this.getClass().getName()).log(
								Level.WARNING,
								"非法的字符编码名称：" + ex.getCharsetName(), ex);
					}
				}
			}
		}
		// 把找到的字符编码名称存储到当前线程的上下文变量中
		ThreadLocalVariables.set(ThreadLocalVariables.VAR_NAME_CHARSET_NAME,
				charsetEncoding);
		// 强制编码，或者是请求参数无指定编码时，使用程序指定编码进行解码。
		if (this.forceEncoding) {
			request.setCharacterEncoding(charsetEncoding);
			// 如果是强制编码，同时还把响应的编码也强行设置为程序指定编码
			response.setCharacterEncoding(charsetEncoding);
		} else if (request.getCharacterEncoding() == null) {
			// 如果请求中未带有编码，设置为程序指定的编码
			request.setCharacterEncoding(charsetEncoding);
			response.setCharacterEncoding(charsetEncoding);
		} else {
			response.setCharacterEncoding(charsetEncoding);
		}

		filterChain.doFilter(request, response);

		// 删除前面申明的当前线程变量
		ThreadLocalVariables.remove(ThreadLocalVariables.VAR_NAME_CHARSET_NAME);
	}
}
