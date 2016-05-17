package com.hoperun.framework.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hoperun.system.bean.SystemOperateEntity;
import com.hoperun.system.bean.SystemRestaurantEntity;
import com.hoperun.system.bean.SystemUserEntity;

/**
 * <br>
 * <b>功能：</b>SessionUtils<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public final class SessionUtils
{

    protected static final Logger logger = Logger.getLogger(SessionUtils.class);

    private static final String SESSION_USER = "session_user";
    //
    private static final String SESSION_HEADQUARTER_OPERATE = "session_headquarter_operate";
    // 用户选中操作的门店编号
    private static final String SESSION_CURRENT_RESTAURANT = "session_current_restaurant";
    // 用户选中操作的门店权限key字符串，使用逗号间隔，用于判断当前用户是否有操作权限
    private static final String SESSION_CURRENT_RESTAURANT_OPERATE = "session_current_restaurant_operate";
    private static final String SESSION_RESTAURANT_LIST = "session_restaurant_list";

    private static final String SESSION_VALIDATECODE = "session_validatecode";// 验证码

    /**
     * set a private constructor, to hide the implicit public one
     */
    public SessionUtils()
    {

    }

    /**
     * 设置session的值
     * 
     * @param request
     * @param key
     * @param value
     */
    public static void setAttr(HttpServletRequest request, String key, Object value)
    {
        request.getSession(true).setAttribute(key, value);
    }

    /**
     * 获取session的值
     * 
     * @param request
     * @param key
     */
    public static Object getAttr(HttpServletRequest request, String key)
    {
        return request.getSession(true).getAttribute(key);
    }

    /**
     * 删除Session值
     * 
     * @param request
     * @param key
     */
    public static void removeAttr(HttpServletRequest request, String key)
    {
        request.getSession(true).removeAttribute(key);
    }

    /**
     * 设置用户信息 到session
     * 
     * @param request
     * @param user
     */
    public static void setUser(HttpServletRequest request, SystemUserEntity user)
    {
        request.getSession(true).setAttribute(SESSION_USER, user);
    }

    /**
     * 从session中获取用户信息
     * 
     * @param request
     * @return SysUser
     */
    public static SystemUserEntity getUser(HttpServletRequest request)
    {
        SystemUserEntity userInSession = (SystemUserEntity) request.getSession(true).getAttribute(SESSION_USER);
        if (userInSession != null)
            return (SystemUserEntity) userInSession.clone();
        else
            return null;
    }

    /**
     * 从session中获取用户信息
     * 
     * @param request
     * @return SysUser
     */
    public static void removeUser(HttpServletRequest request)
    {
        removeAttr(request, SESSION_USER);
    }

    /**
     * 设置验证码 到session
     * 
     * @param request
     */
    public static void setValidateCode(HttpServletRequest request, String validateCode)
    {
        request.getSession(true).setAttribute(SESSION_VALIDATECODE, validateCode);
    }

    /**
     * 从session中获取验证码
     * 
     * @param request
     * @return SysUser
     */
    public static String getValidateCode(HttpServletRequest request)
    {
        return (String) request.getSession(true).getAttribute(SESSION_VALIDATECODE);
    }

    /**
     * 从session中获删除验证码
     * 
     * @param request
     * @return SysUser
     */
    public static void removeValidateCode(HttpServletRequest request)
    {
        removeAttr(request, SESSION_VALIDATECODE);
    }

    /**
     * 设置用户对总部的操作权限到session
     * 
     * @param request
     * @param operates
     */
    public static void setHeadquarterOperate(HttpServletRequest request, List<SystemOperateEntity> operates)
    {
        request.getSession(true).setAttribute(SESSION_HEADQUARTER_OPERATE, operates);
    }

    /**
     * 从session中获取用户对总部的操作权限
     * 
     * @param request
     * @return List<SystemOperateEntity>
     */
    public static List<SystemOperateEntity> getHeadquarterOperate(HttpServletRequest request)
    {
        List<SystemOperateEntity> operates = (List<SystemOperateEntity>) request.getSession(true).getAttribute(SESSION_HEADQUARTER_OPERATE);
        return operates;
    }

    /**
     * 从session中移除用户对总部的操作权限
     * 
     * @param request
     */
    public static void removeHeadquarterOperate(HttpServletRequest request)
    {
        removeAttr(request, SESSION_HEADQUARTER_OPERATE);
    }

    /**
     * 设置用户所属门店列表到session
     * 
     * @param request
     * @param restaurantList
     */
    public static void setRestaurantList(HttpServletRequest request, List<SystemRestaurantEntity> restaurantList)
    {
        request.getSession(true).setAttribute(SESSION_RESTAURANT_LIST, restaurantList);
    }

    /**
     * 从session中获取用户所属门店列表
     * 
     * @param request
     * @return List<SystemRestaurantEntity>
     */
    public static List<SystemRestaurantEntity> getRestaurantList(HttpServletRequest request)
    {
        List<SystemRestaurantEntity> operates = (List<SystemRestaurantEntity>) request.getSession(true).getAttribute(SESSION_RESTAURANT_LIST);
        return operates;
    }

    /**
     * 从session中移除用户所属门店列表
     * 
     * @param request
     */
    public static void removeRestaurantList(HttpServletRequest request)
    {
        removeAttr(request, SESSION_RESTAURANT_LIST);
    }

    /**
     * 设置当前用户操作的门店编号 到session
     * 
     * @param request
     * @param restaurantId
     */
    public static void setCurrentRestaurant(HttpServletRequest request, Integer restaurantId)
    {
        request.getSession(true).setAttribute(SESSION_CURRENT_RESTAURANT, restaurantId);
    }

    /**
     * 从session中获取当前用户操作的门店编号
     * 
     * @param request
     * @return restaurantId
     */
    public static Integer getCurrentRestaurant(HttpServletRequest request)
    {
        Integer restaurantId = (Integer) request.getSession(true).getAttribute(SESSION_CURRENT_RESTAURANT);
        return restaurantId;
    }

    /**
     * 从session中移除当前用户操作的门店编号
     * 
     * @param request
     */
    public static void removeCurrentRestaurant(HttpServletRequest request)
    {
        removeAttr(request, SESSION_CURRENT_RESTAURANT);
    }

    /**
     * 设置用户在当前门店的操作权限字符串到session
     * 
     * @param request
     * @param operateString
     */
    public static void setCurrentRestaurantOperate(HttpServletRequest request, String operateString)
    {
        request.getSession(true).setAttribute(SESSION_CURRENT_RESTAURANT_OPERATE, operateString);
    }

    /**
     * 从session中获取用户在当前门店的操作权限字符串
     * 
     * @param request
     * @return String
     */
    public static String getCurrentRestaurantOperate(HttpServletRequest request)
    {
        return (String) request.getSession(true).getAttribute(SESSION_CURRENT_RESTAURANT_OPERATE);
    }

    /**
     * 从session中获用户在当前门店的操作权限字符串
     * 
     * @param request
     */
    public static void removeCurrentRestaurantOperate(HttpServletRequest request)
    {
        removeAttr(request, SESSION_CURRENT_RESTAURANT_OPERATE);
    }

    public static String getUserInfo(){
        return "ceshi";
    }
}