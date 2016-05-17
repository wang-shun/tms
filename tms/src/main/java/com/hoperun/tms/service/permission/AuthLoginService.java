package com.hoperun.tms.service.permission;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoperun.tms.bean.authority.User;
import com.hoperun.tms.shiro.filter.ShiroUPToken;
import com.hoperun.tms.shiro.filter.SpringCacheManagerWrapper;



@Service("authLoginService")
public class AuthLoginService {
	
	@Autowired
	private SpringCacheManagerWrapper springCacheManagerWrapper;

    public boolean loginIn(final String loginName, final String password, final boolean rememberMe) throws Exception {
        // subject理解成权限对象。类似user
        Subject subject = SecurityUtils.getSubject();
        // 创建用户名和密码的令牌
        ShiroUPToken token = new ShiroUPToken(loginName, password);
        token.setRememberMe(rememberMe);
        
        // 记录该令牌，
        try {
            // 调用subject.login方法进行登录，其会自动委托给SecurityManager.login方法进行登录；
            /*
             * 1、收集用户身份/凭证，即如用户名/密码； 2、调用Subject.login进行登录，如果失败将得到相应的AuthenticationException异常，根据异常提示用户错误信息；否则登录成功；
             * 3、最后调用Subject.logout进行退出操作。
             */
            subject.login(token);
        } catch (ExcessiveAttemptsException e) {
            throw new Exception(e.getMessage());
        } catch (LockedAccountException ex) {
            throw new Exception("账号已被锁定");
        } catch (DisabledAccountException ex) {
            throw new Exception("账号已被禁用");
        } catch (UnknownAccountException ex) {
            throw new Exception("用户名或密码错误");
        } catch (IncorrectCredentialsException ex) {
            throw new Exception("用户名或密码错误");
        } catch (AuthenticationException e) {
            throw new Exception(e.getMessage());
        }
        return true;
    }
    
    /*
     * 踢用户，清缓存
     */
	public void forceKickUser(String loginName, Subject subject){
    	Cache<String, Deque<Serializable>> cache2 = springCacheManagerWrapper.getCache("authenticationCache");
        cache2.remove(loginName);
//        subject.logout();
    }
    

}
