package com.hoperun.framework.utils;

import java.util.Random;

/**
 * 用于生成验证码的工具类。
 * 
 * @author ye_wenchen
 */
public class AuthorizeCodeUtil
{
    /**
     * 获取指定字符长度位的随机数字验证码。
     * 
     * @param p_charCount
     *            指定的数字长度。
     * @return
     */
    public String getRandNum(int p_charCount)
    {
        String charValue = "";
        for (int i = 0; i < p_charCount; i++)
        {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;

    }

    /**
     * 在指定区间内生成随机数。
     * 
     * @param p_from
     *            最小值。
     * @param p_to
     *            最大值。
     */
    public int randomInt(int p_from, int p_to)
    {
        Random r = new Random();
        return p_from + r.nextInt(p_to - p_from);
    }
}