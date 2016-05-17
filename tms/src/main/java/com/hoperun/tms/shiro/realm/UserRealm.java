package com.hoperun.tms.shiro.realm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hoperun.tms.bean.authority.Menu;
import com.hoperun.tms.bean.authority.RoleMenu;
import com.hoperun.tms.bean.authority.User;
import com.hoperun.tms.bean.authority.UserRole;
import com.hoperun.tms.service.authority.AuthUserService;
//import com.hoperun.tms.service.authority.MenuService;
//import com.hoperun.tms.service.authority.RoleMenuService;

/**
 * <p>
 * @author 15120071
 * <p>
 * Date: 2016年2月1日
 * <p>
 * Version: 1.0
 */
public class UserRealm extends AuthorizingRealm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	 @Autowired
	    private AuthUserService authUserService;

//    @Autowired    private MenuService menuServiceAO;
//    
//    @Autowired    private RoleMenuService roleMenuServiceAO;
    

    /**
     * @principals 用户凭证 ,登录成功后的用户信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        UserRole ur = new UserRole();
        ur.setUser_id(user.getId());
        List<UserRole> urList = authUserService.getUserRoleList(ur);
        

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if (null != urList && urList.size() > 0) {
        	
        	List<Menu> mlist = new ArrayList<Menu>();
//        	for(UserRole urTemp:urList){
//        		RoleMenu rm = new RoleMenu();
//        		rm.setRole_id(urTemp.getRole_id());
//        		List<RoleMenu> rmList = roleMenuServiceAO.getRoleMenuList(rm);
//        		if(null != null){
//	        		for(RoleMenu rmTemp:rmList){
//	        			Menu m = menuServiceAO.findBySysMenuId(rmTemp.getMenu_id());
//	        			mlist.add(m);
//	        		}	
//        		}
//        	}
        	
        	   Set<String> perSet = new HashSet<String>();
               for (Menu menu : mlist) {
                   perSet.add("all");
               }
               authorizationInfo.setStringPermissions(perSet);
        	
        }
        return authorizationInfo;
    }

    /**
     * @token 用户登录信息(令牌)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
        String loginName = (String) token.getPrincipal();
        logger.info("[BEGIN]用户登录:" + loginName);

        User user = null;
        try {
            user = authUserService.findByLoginName(loginName);
            logger.debug("[SUCCESS]登录时获取用户信息:" + user.toString());
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("[ERROR]登录时获取用户信息错误:" + e.getMessage(), e.getMessage());
        }
        if (null == user) {
            throw new UnknownAccountException();// 没找到帐号
        }
        logger.debug("[BEGIN]获取用户信息:" + user.toString());
        
        
        fullData(user);

//        IF (BOOLEAN.FALSE.EQUALS(USER.GETSTATE())) {
//            THROW NEW LOCKEDACCOUNTEXCEPTION(); // 帐号锁定
//        }

        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
        		user, //用户
        		user.getPassword(), //密码
        		getName() //realm name
        		);
        return authenticationInfo;
    }
    

    /**
     * 清空授权缓存
     *
     * @param principals
     * @param userName
     */
    public void clearUserCachedInfo(final PrincipalCollection principals, final String userName) {
//        if (null != userName) {
//            super.getAuthenticationCache().remove(userName);
//            return;
//        }
//        UserDO userDO = (UserDO) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
//        if (null != userDO) {
//            super.getAuthenticationCache().remove(userDO.getLoginName());
//        }
    }

    
    public void fullData(User user){
        UserRole ur = new UserRole();
        ur.setUser_id(user.getId());
        List<UserRole> urList = authUserService.getUserRoleList(ur);
        
//
//        if (null != urList && urList.size() > 0) {
//        	List<Menu> mlist = new ArrayList<Menu>();
//        	for(UserRole urTemp:urList){
//        		RoleMenu rm = new RoleMenu();
//        		rm.setRole_id(urTemp.getRole_id());
//        		List<RoleMenu> rmList = roleMenuServiceAO.getRoleMenuList(rm);
//        		if(null != null){
//	        		for(RoleMenu rmTemp:rmList){
//	        			Menu m = menuServiceAO.findBySysMenuId(rmTemp.getMenu_id());
//	        			mlist.add(m);
//	        		}	
//        		}
//        	}
//        	
//        	user.setMenuList(mlist);
//        }
    }

}
