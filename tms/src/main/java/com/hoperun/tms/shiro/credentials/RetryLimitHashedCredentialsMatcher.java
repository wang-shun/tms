package com.hoperun.tms.shiro.credentials;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import com.hoperun.tms.shiro.filter.ShiroUPToken;


/**
 * @author 15120071
 * 增加重试次数，重试锁定时间
 *
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Cache<String, AtomicLong> passwordRetryCache;
    private static final String PREFIX_RETRYCOUNT = "shiro_shinho_retryCount:";
    private static final String PREFIX_TIME_STAMP = "shiro_shinho_timestamp:";
    private static final int retry_times = 5;
    private static final int retry_minutes = 1;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }
    
    public static void main(String[] args) {
    	AtomicInteger retryCount = new AtomicInteger(0);
    	System.out.println(retryCount.getAndIncrement());
    	System.out.println(retryCount.get());
    	System.out.println(retryCount.get());
	}

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    	ShiroUPToken t = (ShiroUPToken) token;
        String username = (String)t.getUsername();
        AtomicLong retryCount = passwordRetryCache.get(PREFIX_RETRYCOUNT+username);
        if(retryCount == null) {
            retryCount = new AtomicLong(0);
            passwordRetryCache.put(PREFIX_RETRYCOUNT+username, retryCount);
        }
        
        if(retryCount.incrementAndGet() > retry_times )  {
        	if(!marginTime(Calendar.getInstance().getTimeInMillis(), passwordRetryCache.get(PREFIX_TIME_STAMP+username).get()))
        	throw new ExcessiveAttemptsException("登录次数失败超过"+retry_times+"次,请在"+retry_minutes+"分钟后重试");	
        	else passwordRetryCache.put(PREFIX_RETRYCOUNT+username, new AtomicLong(1));
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if(matches){
        	passwordRetryCache.remove(PREFIX_RETRYCOUNT+username);	
        	passwordRetryCache.remove(PREFIX_TIME_STAMP+username);
        }
        else {
        	passwordRetryCache.put(PREFIX_RETRYCOUNT+username, retryCount);
        	passwordRetryCache.put(PREFIX_TIME_STAMP+username,new AtomicLong(Calendar.getInstance().getTimeInMillis()));
        }
        return matches;
    }
    
    public boolean marginTime(long time1,long time2){
    	return (time1-time2)/1000 >= 60 * retry_minutes;
    }
}
