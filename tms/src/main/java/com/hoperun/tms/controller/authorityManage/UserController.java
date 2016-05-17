package com.hoperun.tms.controller.authorityManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.authority.Role;
import com.hoperun.tms.bean.authority.User;
import com.hoperun.tms.bean.authority.UserRole;
import com.hoperun.tms.bean.authorityDto.RoleDto;
import com.hoperun.tms.mapper.ReturnData;
import com.hoperun.tms.service.authority.AuthUserService;
import com.hoperun.tms.service.authority.RoleService;


@Controller
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	AuthUserService  userServiceAO;
	
	
	@Autowired
	RoleService  roleServiceAO;
	
	/**
	 * 用户展示
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/query")
	public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<User> plist = userServiceAO.getUserList(null);
		request.setAttribute("plist", plist);
		User user = new User();
		  int pageNumber = 1;
	        int pageSize = 10;
	        if (request.getParameter("page") != null) {
	            pageNumber = Integer.parseInt(request.getParameter("page").toString());
	            pageSize = Integer.parseInt(request.getParameter("rows").toString());
	        } else {
	            pageSize = 1000;
	        }
	        Map map = userServiceAO.getUserListPaginate(user, pageNumber, pageSize);
	        HtmlUtil.writerSuccessJson(response, map);
	}
	
	
    /**
     * 用户操作
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/userOper")
    @ResponseBody
    public void userOper(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String oper = request.getParameter("oper");
            if("userAdd".equals(oper)){
                
                User user = new User();
                
                user.setLogin_name(StringUtil.getNotNullStr(request.getParameter("login_name")));
                Boolean isExists = userServiceAO.userIsExists(user);
                if(isExists){
                	HtmlUtil.writerFailJson(response, "用户名已经存在");
                }
                String pass = StringUtil.getNotNullStr(request.getParameter("password"));
                if(!"".equals(pass)){
                	String md5password = new Md5Hash(pass,"",2).toHex();
                	user.setPassword(md5password);
                }
        		user.setVsername(StringUtil.getNotNullStr(request.getParameter("vsername")));
        		user.setMobile(StringUtil.getNotNullStr(request.getParameter("mobile")));
        		user.setEmail(StringUtil.getNotNullStr(request.getParameter("email")));
                
                userServiceAO.insertUser(user);
            }else if("userDel".equals(oper)){
                String[] ids = request.getParameter("id").split(",");
                for (String id : ids) {
                    User user = new User();
            		user.setId(Integer.parseInt(id));
                    userServiceAO.delUserById(user);
                }
            }else if("userUpdate".equals(oper)){
            	
            	User user = new User();
            	String pass = StringUtil.getNotNullStr(request.getParameter("password"));
            	if(!"".equals(pass)){
            		String md5password = new Md5Hash(pass,"",2).toHex();
            		user.setPassword(md5password);
            	}
         		user.setVsername(StringUtil.getNotNullStr(request.getParameter("vsername")));
         		user.setMobile(StringUtil.getNotNullStr(request.getParameter("mobile")));
         		user.setEmail(StringUtil.getNotNullStr(request.getParameter("email")));
         		user.setId(Integer.parseInt(request.getParameter("id")));
         		userServiceAO.updateUser(user);
            }
            HtmlUtil.writerSuccessJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            HtmlUtil.writerFailJson(response, e.getMessage());
        }
    }
	
	/**
	 * 删除用户
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/userDel")
	@ResponseBody
	public JSONPObject userDel(HttpServletRequest request, @RequestParam String callback) throws Exception {
		String id = request.getParameter("id");
		
		User user = new User();
		user.setId(Integer.parseInt(id));
		
		ReturnData returnData = new ReturnData(false,null);
		
		long counts = userServiceAO.delUserById(user);
		if(counts > 0){
			returnData = new ReturnData(true,null);
		}
		
		return new JSONPObject(callback, returnData);
	}
	
	/**
	 * 判断用户是否存在
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/userIsExists")
	@ResponseBody
	public JSONPObject userIsExists(HttpServletRequest request, @RequestParam String callback) throws Exception {
		String loginName = request.getParameter("loginName");
		
		User user = new User();
		user.setLogin_name(loginName);
		
		ReturnData returnData = new ReturnData(false,null);
		
		Boolean isExists = userServiceAO.userIsExists(user);
		if(isExists){
			returnData = new ReturnData(true,null);
		}
		
		return new JSONPObject(callback, returnData);
	}
	
	
	/**
	 * 增加用户
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/userAddOrUpdate")
	@ResponseBody
	public JSONPObject userAdd(HttpServletRequest request, @RequestParam String callback) throws Exception {
		String loginName = request.getParameter("loginname");
		String password = request.getParameter("Password");
		String name = request.getParameter("Name");
		String mobile = request.getParameter("Mobile");
		String email = request.getParameter("Email");
		String action = request.getParameter("action");
		
		
		User user = new User();
		user.setLogin_name(loginName);
		
		String md5password = new Md5Hash(password,"",2).toHex();
		user.setPassword(md5password);
		
		if(name != null && !"".equals(name)){
			user.setVsername(name);
		}
		if(mobile != null && !"".equals(mobile)){
			user.setMobile(mobile);
		}
		if(email != null && !"".equals(email)){
			user.setEmail(email);
		}
		
		
		
		ReturnData returnData = new ReturnData(true,null);
		
		if(null != action && "add".equals(action)){
			
			userServiceAO.insertUser(user);
		}else{
			String  userId = request.getParameter("id");
			user.setId(Integer.parseInt(userId));
			userServiceAO.updateUser(user);
		}
		
		return new JSONPObject(callback, returnData);
	}
	
	
	/**
	 * 用户操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/userOperOld")
	public String userOperold(HttpServletRequest request) throws Exception {
		String action = request.getParameter("action");
		
		request.setAttribute("action", action);
		if(null != action && "add".equals(action)){
			
			return "/authority/useradd";
		}else{
			String userId = request.getParameter("id");
			
			User user = new User();
			user.setId(Integer.parseInt(userId));
			
			List<User> uList = userServiceAO.getUserList(user);
			User u = uList.get(0);
			
			request.setAttribute("user", u);
			
			return "/authority/useradd";
		}
	}
	
	
	/**
	 * 用户角色操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/userRoleOper")
	@ResponseBody
	public ReturnData userRoleOper(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		
		
		List<Role> rolelist = roleServiceAO.getRoleList(null);
		
		UserRole ur = new UserRole();
		ur.setUser_id(Integer.parseInt(id));
		
		List<UserRole> urList = userServiceAO.getUserRoleList(ur);
		Map<Integer,Integer> urm = new HashMap<Integer,Integer> ();
		if(null != urList && urList.size()>0){
			for(UserRole uur : urList){
				urm.put(uur.getRole_id(), uur.getUser_id());
			}
		}
		List<RoleDto> roledlist = new ArrayList<RoleDto>();
		for(Role r:rolelist){
			RoleDto d =  new RoleDto();
			BeanUtils.copyProperties(r,d);
			if(null != urList && urList.size()>0){
				if(urm.containsKey(d.getId())){
					d.setChecked("Checked");
				}else{
					d.setChecked("");
				}
			}
			roledlist.add(d);
		}
		ReturnData returnData = new ReturnData(true,roledlist);
		return  returnData;
	}
	
	
	
	/**
	 * 判断用户是否存在
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/userroleupdate")
	@ResponseBody
	public JSONPObject userroleupdate(HttpServletRequest request, @RequestParam String callback) throws Exception {
		String userId = request.getParameter("userId");
		String role = request.getParameter("role");
		
		
		List<UserRole> urList = new ArrayList<UserRole>();
		if(role != null){
			String[] ro = role.split(",");
			for(String r:ro){
				UserRole uro = new UserRole();
				uro.setRole_id(Integer.parseInt(r));
				uro.setUser_id(Integer.parseInt(userId));
				urList.add(uro);
			}
		}
		UserRole ur = new UserRole();
		ur.setUser_id(Integer.parseInt(userId));
		ReturnData returnData = new ReturnData(true,null);
		userServiceAO.delUserRole(ur);
		userServiceAO.addUserRole(urList);
		return new JSONPObject(callback, returnData);
	}

 }
