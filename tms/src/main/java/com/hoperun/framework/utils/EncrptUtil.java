package com.hoperun.framework.utils;

import java.security.MessageDigest;

/**
 * 用于加密、解密的工具类。
 * 
 * @author ye_wenchen
 */
public class EncrptUtil
{
    /**
     * 根据指定的内容生成 MD5（16位）密文。
     * 
     * @param p_source
     *            指定的加密内容。
     * @return
     */
    public static String encryptMD5(String p_source)
    {
        StringBuffer sb = new StringBuffer(32);
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(p_source.getBytes("utf-8"));

            for (int i = 0; i < array.length; i++)
            {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toLowerCase().substring(1, 3));
            }
        }
        catch (Exception e)
        {
            return null;
        }

        return sb.toString().substring(8, 24);
    }
}
