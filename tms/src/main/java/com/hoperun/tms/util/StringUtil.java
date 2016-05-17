package com.hoperun.tms.util;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理的封装简便类。.
 * @author 15120071
 */
public class StringUtil implements Serializable{

    /**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -1803093049089580530L;

	/** 一个空字符串的具名常量。表示为：&quot;&quot;. */
    public static final String EMPTY = "";

    /** The Constant HEX_CHARS. */
    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /** The Constant HIGHEST_SPECIAL. */
    private static final int HIGHEST_SPECIAL = '>';

    /** The special characters representation. */
    private static char[][] specialCharactersRepresentation = new char[HIGHEST_SPECIAL + 1][];
    static {
        specialCharactersRepresentation['&'] = "&amp;".toCharArray();
        specialCharactersRepresentation['<'] = "&lt;".toCharArray();
        specialCharactersRepresentation['>'] = "&gt;".toCharArray();
        specialCharactersRepresentation['"'] = "&#034;".toCharArray();
        specialCharactersRepresentation['\''] = "&#039;".toCharArray();
    }

    /**
     * 默认的构造方法。.
     */
    StringUtil() {
    }

    /**
     * 返回给定字符串的UTF8格式下的长度。.
     * 
     * @param str 给定的字符串。
     * @return 给定字符串的UTF8格式下的长度。
     */
    public static Integer utf8Length(String str) {
        if (isNullOrEmpty(str))
            return 0;
        String temp = new String(str);
        return temp.trim().replaceAll("[^\\x00-\\xff]", "***").length();
    }

    /**
     * <p>
     * 如果给定的对象为<code>null</code>或者字符串形式为"null"，则返回<code>true</code>。
     * </p> .
     * 
     * @param o 要验证的对象。
     * @return 返回true，如果给定的对象为<code>null</code>或者字符串形式为"<code>null</code>"
     */
    public static boolean isNull(Object o) {
        return o == null;
    }

    /**
     * <p>
     * 如果给定的对象为<code>null</code>或者字符串形式为空字符串，则返回<code>true</code>。
     * </p> .
     * 
     * @param o 要验证的对象。
     * @return 返回true，如果给定的对象为<code>null</code>或者字符串形式为空字符串
     */
    public static boolean isNullOrEmpty(Object o) {
        if (o == null) {
            return true;
        } else if (EMPTY.equals(o.toString().trim())
                || "null".equals(o.toString().trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * 如果给定字符串可以表示为一个<code>Number</code>，则返回<code>true</code>，否则返回
     * <code>false</code>。
     * </p> .
     * 
     * @param input 表示Number的给定字符串。
     * @return 返回<code>true</code>，如果给定的字符串可以表示为一个Number。
     */
    public static boolean isNumber(String input) {
        if (isNullOrEmpty(input))
            return false;
        Object result = null;
        if (input.indexOf('.') > 0) {
            try {
                result = Float.parseFloat(input);
            } catch (NumberFormatException e) {
                return false;
            }
            return (result != null);
        } else {
            try {
                result = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                return false;
            } catch (Exception e) {
                try {
                    result = Long.parseLong(input);
                } catch (Exception ee) {
                    return false;
                }
            }
            return (result != null);
        }
    }

    /**
     * <p>
     * 如果给定字符串是正确格式的Email地址，则返回<code>true</code>，否则返回<code>false</code>。
     * </p> .
     * 
     * @param input 表示Email地址的给定字符串。
     * @return 返回<code>true</code>，如果给定的字符是正确格式的Email。
     */
    public static boolean isEmail(String input) {
        if (isNullOrEmpty(input))
            return false;
        input = input.trim();
        return Pattern.matches(
                "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.?)*[a-zA-Z]{2,3}$", input);
    }

    /**
     * 移除给定的字符串中的<code>(X)HTML</code>标签。.
     * 
     * @param input 给定的字符串。
     * @return 返回移除<code>(X)HTML</code>标签的字符串。
     */
    public static String stripTags(String input) {
        return input.replaceAll("</?[^>]+>", EMPTY);
    }

    /**
     * 将字符串中的XML特殊字符转换成可显示的字符。 下列字符经过转换后的字符:
     * 
     * <pre>
     *  &amp; --&gt; &amp;amp;
     *  &lt; --&gt; &amp;lt;
     *  &gt; --&gt; &amp;gt;
     *  &quot; --&gt; &amp;#034;
     *  ' --&gt; &amp;#039;
     * </pre> .
     * 
     * @param buffer 要处理的字符串。
     * @return 处理后的字符串。
     */
    public static String escapeXml(String buffer) {
        int start = 0;
        int length = buffer.length();
        char[] arrayBuffer = buffer.toCharArray();
        StringBuffer escapedBuffer = null;

        for (int i = 0; i < length; i++) {
            char c = arrayBuffer[i];
            if (c <= HIGHEST_SPECIAL) {
                char[] escaped = specialCharactersRepresentation[c];
                if (escaped != null) {
                    // create StringBuffer to hold escaped xml string
                    if (start == 0) {
                        escapedBuffer = new StringBuffer(length + 5);
                    }
                    // add unescaped portion
                    if (start < i) {
                        escapedBuffer.append(arrayBuffer, start, i - start);
                    }
                    start = i + 1;
                    // add escaped xml
                    escapedBuffer.append(escaped);
                }
            }
        }
        // no xml escaping was necessary
        if (start == 0) {
            return buffer;
        }
        // add rest of unescaped portion
        if (start < length) {
            escapedBuffer.append(arrayBuffer, start, length - start);
        }
        return escapedBuffer.toString();
    }

    /**
     * 将给定的对象转换为字符串对象。.
     * 
     * @param input 给定要转换字符串的对象。
     * @return 转换的字符串
     */
    public static String parse(Object input) {
        if (isNullOrEmpty(input))
            return EMPTY;
        return String.valueOf(input);
    }

    /**
     * 将给定的输入流转换为字符串对象。.
     * 
     * @param is 给定的输入流
     * @return 转换的字符串
     */
    public static String parse(java.io.InputStream is) {
        StringBuffer buffer = new StringBuffer();
        try {
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = is.read(b)) != -1) {
                buffer.append(new String(b, 0, i));
            }
            is.close();
        } catch (java.io.IOException ioe) {

        }
        return buffer.toString();
    }

    /**
     * 将给定的对象转换成<code>Integer</code>型。.
     * 
     * @param input 要转换为Integer型的对象。
     * @param defaultValue 转换出错时返回的默认值。
     * @return 转换后的Integer值。
     */
    public static int parseInt(Object input, int defaultValue) {
        if (isNull(input))
            return defaultValue;
        try {
            return Integer.parseInt(input.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <p>
     * 使用指定的字面值替换序列替换给定字符串匹配字面值目标序列的每个子字符串。 该替换从此字符串的开始一直到结束，例如，用 "b" 替换字符串
     * "aaa" 中的 "aa" 将生成 "ba" 而不是 "ab"。
     * </p> .
     * 
     * @param input 给定的源字符串
     * @param substringBefore 要替换的目标字符串
     * @param substringAfter 用于替换目标字符串的新字符串
     * @return 替换后的新字符串
     */
    public static String replace(String input, String substringBefore,
            String substringAfter) {
        if (input == null)
            input = "";
        if (input.length() == 0)
            return "";
        if (substringBefore == null)
            substringBefore = "";
        if (substringBefore.length() == 0)
            return input;

        StringBuffer buf = new StringBuffer(input.length());
        int startIndex = 0;
        int index;
        while ((index = input.indexOf(substringBefore, startIndex)) != -1) {
            buf.append(input.substring(startIndex, index)).append(
                    substringAfter);
            startIndex = index + substringBefore.length();
        }
        return buf.append(input.substring(startIndex)).toString();
    }

    /**
     * 计算字符串中某个字符出现的次数
     * 
     * @param str 字符串源
     * @param ch 匹配的字符
     * @return 出现的次数
     */
    public static int getNum(String str, String ch) {
        int len = ch.length();
        int num = 0;
        for (int i = len; i <= str.length(); i++) {
            String c = str.substring(i - len, i);
            if (c.equals(ch)) {
                num++;
            }
        }
        return num;
    }

    /**
     * 截取Html片段，补取或删除不全的Html代码。
     * 
     * @param shtml 要截取的Html片段。
     * @param iLength 要截取的长度。
     * @param strEnd 结束的字符串。
     * @return 截取的Html片段。
     */
    public static String interceptHtml(String shtml, int iLength, String strEnd) {
        StringBuffer result = new StringBuffer();
        // 字符串长度变量
        int n = 0;
        char charHtml;
        // 是否是Html代码
        boolean isCode = false;
        // 是否是Html特殊字符
        boolean isHtml = false;
        for (int i = 0; i < shtml.length(); i++) {
            // 字符串索引值
            charHtml = shtml.charAt(i);
            if (charHtml == '<') {
                isCode = true;
            } else if (charHtml == '&') {
                isHtml = true;
            } else if (charHtml == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (charHtml == ';' && isHtml) {
                isHtml = false;
            }
            if (!isCode && !isHtml) {
                n = n + 1;
                if ((charHtml + "").getBytes().length > 1) {
                    n = n + 1;
                }
            }
            // 追加到序列中(先用String.valueOf(char[])方法将参数转换为字符串，再追加到序列)
            result.append(charHtml);
            if (n >= iLength) {
                break;
            }
        }
        // 添加结束字符串(标识)
        if (iLength < shtml.length()) {
            result.append(strEnd);
        } else {
            result.append("");
        }
        // 取出截取字符串中的Html标签
        String splitStr = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
        // 去除不需要结束标签的html标签
        splitStr = splitStr
                .replaceAll(
                        "</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR"
                                + "|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
                        "");
        // 去除成对的Html标签
        for (int i = 0; i < (StringUtil.getNum(splitStr, "<") - StringUtil
                .getNum(splitStr, "</")); i++) {
            splitStr = splitStr.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>",
                    "$2");
        }
        // 用正则表达式取出标签
        Pattern pat = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
        Matcher mat = pat.matcher(splitStr);

        List<String> endHtml = new ArrayList<String>();
        // find():尝试查找与该模式匹配的输入序列的下一个子序列
        while (mat.find()) {
            // group(int):返回在以前匹配操作期间由给定组捕获的输入子序列
            endHtml.add(mat.group(1));
        }
        // 补全不成对的Html标签
        for (int i = endHtml.size() - 1; i >= 0; i--) {
            result.append("</");
            result.append(endHtml.get(i));
            result.append(">");
        }
        return result.toString();
    }




    /**
     * 以HTML或者普通字符串格式化给定的字符串。当<tt>flag</tt>为<code>true</code>时，
     * 输出HTML格式化的字符串，反之则输出普通字符串。.
     * 
     * @param html 要格式化的字符串。
     * @param flag 是否转换为HTML格式。
     * @return 格式化的字符串。
     * @since 1.1
     */
    public static String formatPara(String html, boolean flag) {
        if (isNullOrEmpty(html))
            return EMPTY;
        String shtml = html;
        if (flag) {
            // shtml = shtml.replaceAll("&", "&amp;");
            shtml = shtml.replaceAll("<br[^>]*>", "\r\n");
            shtml = shtml.replaceAll(" ", "&nbsp;");
            shtml = shtml.replaceAll("\r\n", "\n");
            shtml = shtml.replaceAll("\n", "<br />");
        } else {
            shtml = shtml.replaceAll("<br[^>]*>", "\r\n");
            shtml = shtml.replaceAll("&nbsp;", " ");
            shtml = shtml.replaceAll("&amp;", "&");
        }
        return shtml;
    }

    /**
     * 用MD5算法对给定的摘要字符串进行加密处理。若给定的摘要字符串为<code>null</code>， 则返回{@link #EMPTY}字符串。.
     * 
     * @param text 要加密的摘要字符串。
     * @return 经过MD5算法加密过的摘要字符串。
     * @since 1.1
     */
    public static String md5(String text) {
        if (isNull(text))
            return EMPTY;
        byte[] b = text.getBytes();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            // 用给定的字符串字节更新MD5算法的摘要信息
            digest.update(b);
            // 完成MD5的哈希计算
            byte[] hashBytes = digest.digest();
            char[] chars = new char[hashBytes.length * 2];
            for (int i = 0, k = 0; i < hashBytes.length; i++) {
                chars[k++] = HEX_CHARS[hashBytes[i] >>> 4 & 0xf];
                chars[k++] = HEX_CHARS[hashBytes[i] & 0xf];
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException nsae) {
        	System.out.println("MD5加密失败，在调用环境中不存在MD5算法！详细信息：" + nsae.getMessage());
            return text;
        }
    }

    /**
     * 使用特定的加密算法加密给定的字符串，该加密为不右逆。加密后的字符串永远是长度为20位的字符串。.
     * 
     * @param text 要加密的摘要字符串。
     * @return 经过特定算法加密过的摘要字符串。
     * @since 1.1
     */
    public static String digest(String text) {
        String dedigest = md5(text);
        if (EMPTY.equals(dedigest))
            return EMPTY;
        StringBuffer sb = new StringBuffer();
        sb.append(dedigest.substring(2, 6));
        sb.append(dedigest.substring(8, 12));
        sb.append(dedigest.substring(14, 18));
        sb.append(dedigest.substring(20, 24));
        sb.append(dedigest.substring(26, 30));
        return sb.toString();
    }

    /**
     * 判断给定的字符串是否是url格式
     * 
     * @param urlString 给定的字符串
     * @return true or false
     */
    public static boolean isURL(String urlString) {
        String strRegexp = "^((https?|ftp|rtsp|mms|HTTPS?|FTP|RTSP|MMS)?://)?"
                + "(([0-9a-zA-Z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}|([0-9a-zA-Z_!~*'()-]+\\.)*([0-9a-zA-Z][0-9a-zA-Z]{0,490})?[0-9a-zA-Z]\\.[a-zA-Z]{2,6})"
                + "(:[0-9]{1,4})?((/?)|(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)|^/.*$";
        Pattern regex = Pattern.compile(strRegexp);
        Matcher matcher = regex.matcher(urlString);
        boolean isUrl = matcher.matches();
        if (isUrl) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 过滤HTML代码中的所有标签，仅保留文字信息
     * 
     * */
    public static String filterSpacialCharacter(String str) {
        if (null == str || "".equals(str.trim())) {
            return "";
        }
        return Pattern.compile("<(?:\"[^\"]*\"|'[^']*'|[^><])*>", Pattern.DOTALL).matcher(str).replaceAll("");
    }


    /**
     * 将Json串构建为Jquery跨域回调函数方式
     * 
     * @param jsoncallback 回调函数名。
     * @param json JSON串。
     */
    public static String toJsonCallback(String jsoncallback, String json){
        if(jsoncallback!=null){
            return new StringBuffer().append(jsoncallback).append("(").append(json).append(")").toString();
        }else{
            return json;
        }
    }
    
    



    /**
     * split方法的反操作实现。.
     * 
     * @param strs 要组合的字符串数组
     * @param separator 每个字符串之间的隔离符
     * @return 返回组合之后的<code>String</code>字符串。
     */
    public static String reSplit(String[] strs, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i]).append(separator);
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    
    /**
     * 字符串截取
     *
     * @param text 文本串
     * @param length 截取的文本长度，以字节为单位
     * 
     * @return String 截取后的文本串
     * 
     * @author wenjian
     */
    public static String cutString(String text, int length){
        return cutString(text, length, null, null);
    }
    /**
     * 字符串截取
     *
     * @param text 文本串
     * @param length 截取的文本长度，以字节为单位
     * @param mode (可选参数) 指明是否在省略部份加后缀，值有：auto(默认),true(不管怎样都加上后缀),false(不管怎样都不加后缀)
     * @param ellipsis (可选参数) 指明在省略部份附加什么，默认为"..."省略号
     * 
     * @return String 截取后的文本串
     * 
     * @author wenjian
     */
    public static String cutString(String text, int length, String mode, String ellipsis){
        //mode = mode==null?CUTSTR_MODE_AUTO:mode;
        ellipsis = ellipsis==null?CUTSTR_DEFAULT_ELLIPSIS:ellipsis;
        int len = text.length();
        int i=0;
        for(int j=0;i<len && j<length;i++){
            char c = text.charAt(i);
            if(c>255){
                j+=2;
            }else{
                j++;
            }
        }
        text = text.substring(0, i);
        /* 处理省略模式 */
        if(mode==null || mode.equalsIgnoreCase(CUTSTR_MODE_AUTO)){
            if(i<len){
                text += ellipsis;
            }
        }else if(mode.equalsIgnoreCase(CUTSTR_MODE_TRUE)){
            text += ellipsis;
        }
        return text;
    }
    /** 省略策略: 自动，如果文本内容超过长度，截取并加"..."号，否则，不加"..." */
    public static final String CUTSTR_MODE_AUTO="auto";
    /** 省略策略: 不管怎么样，都加省略"..." */
    public static final String CUTSTR_MODE_TRUE="true";
    /** 省略策略: 不管怎么样，都不加省略"..." */
    public static final String CUTSTR_MODE_FALSE="false";
    /** 默认省略部份追加的内容 */
    public static final String CUTSTR_DEFAULT_ELLIPSIS="...";
    
    /**
     * 判断 手机号是否合法
     * @param phoneNum
     * @return
     */
    public static boolean isPhone(String phoneNum){
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
    
    /**
     * 判断是否UTF8
     * @param b
     * @param aMaxCount
     * @return
     */
    public static boolean ifUtf8(byte[] b,int aMaxCount){
        int lLen=b.length,lCharCount=0;
        for(int i=0;i<lLen && lCharCount<aMaxCount;++lCharCount){
               byte lByte=b[i++];//to fast operation, ++ now, ready for the following for(;;)
               if(lByte>=0) continue;//>=0 is normal ascii
               if(lByte<(byte)0xc0 || lByte>(byte)0xfd) return false;
               int lCount=lByte>(byte)0xfc?5:lByte>(byte)0xf8?4:lByte>(byte)0xf0?3:lByte>(byte)0xe0?2:1;
               if(i+lCount>lLen) return false;
               for(int j=0;j<lCount;++j,++i) if(b[i]>=(byte)0xc0) return false;
        }
        return true;

    } 
    
    public static String utf8(String fileName) {
		try {
			fileName = new String(fileName.getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fileName;
	}
    
    
    public static boolean checkListIsNull(List<File> list){
    	if(null==list||list.isEmpty()) return false;
    	int num = list.size();
    	if(null == list.get(0)&&1==num) return false;
    	return true;
    }
    
    public static boolean isChinese(String str){
    	  
        char[] chars=str.toCharArray(); 
        boolean isGB2312=false; 
        for(int i=0;i<chars.length;i++){
                    byte[] bytes=(""+chars[i]).getBytes(); 
                    if(bytes.length==2){ 
                                int[] ints=new int[2]; 
                                ints[0]=bytes[0]& 0xff; 
                                ints[1]=bytes[1]& 0xff; 
                                if(ints[0]>=0x81 && ints[0]<=0xFE && ints[1]>=0x40 && ints[1]<=0xFE){ 
                                            isGB2312=true; 
                                            break; 
                                } 
                    } 
        } 
        return isGB2312; 
    }
    
    public static String getPercent(double x,double y)
    {
        String baifenbi="";//接受百分比的值
        double baix=x*1.0;
        double baiy=y*1.0;
        double fen=baix/baiy;
        DecimalFormat df1 = new DecimalFormat("##%");
        baifenbi= df1.format(fen);
        return baifenbi;
    }

    public static String formart(long x,String rex){
    	if(isNullOrEmpty(rex)) rex = "0.00";
    	 double baix=x*1.0;
    	 double fen=baix/1024/1024;
    	DecimalFormat format = new DecimalFormat(rex);
    	return format.format(fen);
    }
    
    
    private static final String REX = "\"";
	
	public static String l(String rex){
		return REX+rex+REX;
	}
	

	/**
	 * 验证文件夹名称
	 * @param dirName
	 * @return
	 */
    public static boolean isValidateDir(String dirName){
    	if(StringUtil.isNullOrEmpty(dirName)) return false;
        Pattern pattern = Pattern.compile("[^\\/:*?\"<>|]+");
        Matcher matcher = pattern.matcher(dirName);
        if (matcher.matches())return true;
        return false;
    }
    
    public static boolean checkPassWord(String password){
    	String regex = "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*?[-!\\(\\)\\~@#$%\\^\\&\\*_\\+\\-\\=])[a-zA-Z0-9-!\\(\\)\\~@#$%\\^\\&\\*_\\+\\-\\=]{10,20}";
    	return password.matches(regex);
    }
    
	
    
    
}
