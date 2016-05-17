package com.hoperun.framework.utils;

import java.util.UUID;

/**
 * 主键生成器
 * @author gao_qi
 *
 */
public class PrimaryKeyGenerator
{
    /**
     * 生成UUID
     * @return
     */
    public static String generateUUID()
    {
        return UUID.randomUUID().toString();
    }
}