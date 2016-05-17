package com.hoperun.tms.controller.authorityManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.tms.bean.authority.Menu;
import com.hoperun.tms.bean.authority.RoleMenu;
import com.hoperun.tms.bean.authority.User;
import com.hoperun.tms.bean.authority.UserRole;
import com.hoperun.tms.mapper.ReturnData;
import com.hoperun.tms.service.authority.AuthUserService;
import com.hoperun.tms.service.authority.MenuService;
import com.hoperun.tms.service.authority.RoleMenuService;
import com.hoperun.tms.service.base.ReceiverService;
import com.hoperun.tms.service.permission.AuthLoginService;
import com.hoperun.tms.util.StringUtil;


@Controller
@Scope("prototype")
public class LoginController {
	
    @Autowired
    private AuthLoginService loginAo;
    
    @Autowired
    private AuthUserService userServiceAo;
    
    @Autowired
    ReceiverService receiverService;
    
    @Autowired    
    private MenuService menuServiceAO;
    
    
    @Autowired    
    private RoleMenuService roleMenuServiceAO;
    
	
	/**
	 * 用户登录
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="login")
	public String login() throws Exception {
		return "main/login";
	}

	 /**
     * <pre>
     * 登录
     * </pre>
     *
     * @param request
     * @param name 登录名
     * @param pass 密码
     * @return
     */
    @RequestMapping(value = "/authLogin")
    @ResponseBody
    public ReturnData authLogin(final HttpServletRequest request, HttpServletResponse response) {
    	String name = request.getParameter("name");
    	String pass = request.getParameter("pass");
    	ReturnData returnData = new ReturnData(false,null);
        try {

            if (StringUtil.isNullOrEmpty(name)) {
                returnData.setData("请输入用户名");
            }
            if (StringUtil.isNullOrEmpty(pass)) {
                returnData.setData("请输入密码");
            }
            
            Subject sub = SecurityUtils.getSubject();
            
            if(sub.isAuthenticated()){
            	loginAo.forceKickUser(name,sub); //踢用户，清缓存 
            }
            
            User userl = (User)SecurityUtils.getSubject().getPrincipal();
            
            loginAo.loginIn(name, pass, false);
            
            User user = (User)SecurityUtils.getSubject().getPrincipal();
            userServiceAo.refreshUser(request, user);
            
            
            List<Menu> ms = getMenuMap(user);
            
            Map<String,Menu> menuMap = new HashMap<String,Menu>();
            
            if(ms != null){
    			for(Menu m : ms){
    				menuMap.put(m.getUrl(),m);
    			}
    		}
            
            request.getSession().setAttribute("menuMap", menuMap);
            request.getSession().setAttribute("menuList", ms);
            
            returnData = new ReturnData(true,null);
        } catch (Exception e) {
        	e.printStackTrace();
        	returnData.setData(e.getMessage());
        	returnData.setStatus(false);
        }
        return returnData;
    }
	
	private List<Menu> getMenuMap(User user) {
		UserRole ur = new UserRole();
		ur.setUser_id(user.getId());
		List<UserRole> urList = userServiceAo.getUserRoleList(ur);
		
		try{
		if (null != urList && urList.size() > 0) {
			List<String> roleIds = new ArrayList<String>();
			for(UserRole urTemp:urList){
				RoleMenu rm = new RoleMenu();
				rm.setRole_id(urTemp.getRole_id());
				roleIds.add(urTemp.getRole_id().toString());
			}
			List<RoleMenu> rmList = roleMenuServiceAO.getRoleMenuListByRIds(roleIds);
				if(rmList != null){
					List<String> menuIds = new ArrayList<String>();
		    		for(RoleMenu rmTemp:rmList){
		    			menuIds.add(rmTemp.getMenu_id().toString());
		    		}	
		    		List<Menu> ms = menuServiceAO.findBySysMenuIds(menuIds);
		    		return ms;
				}
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 登出
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "logout")
    public void logout(final HttpServletRequest request, HttpServletResponse response) throws Exception {

//        request.removeAttribute(Constant.SESSIONKEY_USER);
        Subject sub = SecurityUtils.getSubject();
        sub.logout();
        Map map = new HashMap();
        map.put("url", "/html/main/login.html");
        HtmlUtil.writerSuccessJson(response, map);
    }
 }
