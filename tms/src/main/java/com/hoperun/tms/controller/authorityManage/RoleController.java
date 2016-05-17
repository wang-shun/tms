package com.hoperun.tms.controller.authorityManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.tms.bean.authority.Menu;
import com.hoperun.tms.bean.authority.Role;
import com.hoperun.tms.bean.authority.RoleMenu;
import com.hoperun.tms.bean.authority.User;
import com.hoperun.tms.bean.authorityDto.MenuDto;
import com.hoperun.tms.mapper.ReturnData;
import com.hoperun.tms.service.authority.MenuService;
import com.hoperun.tms.service.authority.RoleMenuService;
import com.hoperun.tms.service.authority.RoleService;


@Controller
@Scope("prototype")
@RequestMapping(value="/role")
public class RoleController {
	
	@Autowired
	RoleService  roleServiceAO;
	
	@Autowired
	MenuService  menuServiceAO;
	
	@Autowired
	RoleMenuService  rolemenuServiceAO;
	
	/**
	 * 首页展示
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/query")
	@ResponseBody
	public void role(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int pageNumber = 1;
        int pageSize = 10;
        if(request.getParameter("page")!= null){
            pageNumber =  Integer.parseInt(request.getParameter("page").toString());
            pageSize = Integer.parseInt(request.getParameter("rows").toString());
        }else{
            pageSize=1000;
        }
	        
        Map map = roleServiceAO.getRoleListPaginate(null, pageNumber, pageSize);
		HtmlUtil.writerSuccessJson(response,map);
	}
	
	
	/**
	 * 删除权限
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/roleDel")
	@ResponseBody
	public ReturnData roleDel(HttpServletRequest request) throws Exception {
		String ids = request.getParameter("ids");
		
		ReturnData returnData = new ReturnData(false,null);
		
		if(ids != null && !"".equals(ids)){
			
			String[] idArray = ids.split(",");
			for(String id:idArray){
				Role role = new Role();
				role.setId(Integer.parseInt(id));
				long counts = roleServiceAO.delRoleById(role);
				if(counts > 0){
					returnData = new ReturnData(true,null);
				}
			}
		}
		return returnData;
	}
	
	/**
	 * 给对应的角色配置菜单权限页面
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/roleMenu")
	@ResponseBody
	public ReturnData roleMenu(HttpServletRequest request) throws Exception {
		String   roleId  = request.getParameter("roleId");
		String action = request.getParameter("action");
		
		List<Menu> menulist = menuServiceAO.getMenuList(null);
		String mlJson = "";
		
		if("update".equals(action)){
		
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setRole_id(Integer.parseInt(roleId));
			List<RoleMenu> rolemenuList = rolemenuServiceAO.getRoleMenuList(roleMenu);
			Map<Integer,Integer> rolemenuMap = new HashMap<Integer,Integer>();
			for(RoleMenu rm : rolemenuList){
				rolemenuMap.put(rm.getMenu_id(), rm.getRole_id());
			}
			
			List<MenuDto> mdList = new ArrayList<MenuDto>();
			for(Menu men : menulist){
				MenuDto d = new MenuDto();
				BeanUtils.copyProperties(men,d);
				if(rolemenuMap.containsKey(men.getId())){
					d.setChecked(true);
				}
				mdList.add(d);
			}
	        
			try {
				ObjectMapper mapper = new ObjectMapper();  
				mlJson = mapper.writeValueAsString(mdList);
			}catch (Exception e) {
				e.printStackTrace();
			} 
			
			
		}else if("add".equals(action)){
			try {
				ObjectMapper mapper = new ObjectMapper();  
				mlJson = mapper.writeValueAsString(menulist);
			}catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
		
		
		ReturnData returnData = new ReturnData(true,mlJson);
		return returnData;
		
	}
	
	
	/**
	 * 修改角色的菜单权限
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/roleupdate")
	@ResponseBody
	public ReturnData roleupdate(HttpServletRequest request) throws Exception {
		String ns = request.getParameter("ns");
		String action = request.getParameter("action");
		String roleId = request.getParameter("roleId");
		String roleName = request.getParameter("rolename");
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		
		List<RoleMenu> rolemenuList = new ArrayList<RoleMenu>();
		
		ReturnData returnData = new ReturnData(false,null);
		
		Role role = new Role();
		
		if("update".equals(action)){
			
			role.setId(Integer.parseInt(roleId));
			role.setName(roleName);
			roleServiceAO.updateRole(role);
			
			
		}else{
			role.setName(roleName);
			role.setCreateId(user.getId());
			roleServiceAO.insertRole(role);
			roleId = role.getId().toString();
		}
		
		
		if(null != ns && !"".equals(ns)){
			RoleMenu rolemen = null;
			String[] notes = ns.split(",");
			for(String note:notes){
				rolemen = new RoleMenu();
				rolemen.setRole_id(Integer.parseInt(roleId));
				rolemen.setMenu_id(Integer.parseInt(note));
				rolemenuList.add(rolemen);
			}
		}
		
		rolemenuServiceAO.delRoleMenuById(rolemenuList.get(0));
		
		long counts = rolemenuServiceAO.insertRolemenuList(rolemenuList);
		
		if(counts > 0){
			returnData = new ReturnData(true,null);
		}
		
		return returnData;
	}
	
	

 }
