package com.hoperun.tms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

    /** 
     * 电话号码验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean validatePhone(String phoneNum) {   
        Pattern p1 = null,p2 = null;  
        Matcher m = null;  
        boolean b = false;    
		String _d = "^1[3578][01379]\\d{8}$";//电信手机号码
		String _l = "^1[34578][01256]\\d{8}$";//联通手机号码
		String _y = "^(134[012345678]\\d{7}|1[34578][012356789]\\d{8})$";//移动手机号码
        if(Pattern.compile(_d).matcher(phoneNum).matches() 
        		|| Pattern.compile(_l).matcher(phoneNum).matches()
        		|| Pattern.compile(_y).matcher(phoneNum).matches()){
        	b=true;
        }   
        return b;  
    }  
}
