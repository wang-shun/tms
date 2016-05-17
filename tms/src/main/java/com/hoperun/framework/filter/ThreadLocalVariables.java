package com.hoperun.framework.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用线程对象保存当前线程上下文的一些参数，用来在多个模块间传递一下关键的请求参数
 * 
 * @author Luo Wenqiang
 */
public class ThreadLocalVariables {

	private static final ThreadLocal<Map<String, String>> maps = new ThreadLocal<Map<String, String>>();
	public static final String VAR_NAME_CHARSET_NAME = "charsetEncoding";

	public static String get(String key) {
		synchronized (maps) {
			if (maps.get() == null) {
				maps.set(new HashMap<String, String>());
			}
			return maps.get().get(key);
		}
	}

	public static void set(String key, String value) {
		synchronized (maps) {
			if (maps.get() == null) {
				maps.set(new HashMap<String, String>());
			}
			maps.get().put(key, value);
		}
	}

	public static void remove() {
		synchronized (maps) {
			if (maps.get() != null) {
				maps.remove();
			}
		}
	}

	public static void remove(String key) {
		synchronized (maps) {
			if (maps.get() == null) {
				maps.set(new HashMap<String, String>());
			}
			maps.get().remove(key);
		}
	}
}
