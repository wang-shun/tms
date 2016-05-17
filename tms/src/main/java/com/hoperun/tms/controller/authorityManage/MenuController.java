package com.hoperun.tms.controller.authorityManage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoperun.tms.bean.authority.Menu;
import com.hoperun.tms.mapper.ReturnData;
import com.hoperun.tms.service.authority.MenuService;


@Controller
@Scope("prototype")
@RequestMapping(value="/menu")
public class MenuController {
	
	@Autowired
	MenuService  menuServiceAO;
	
	/**
	 * 首页展示
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getAll")
	@ResponseBody
	public ReturnData menu(HttpServletRequest request) throws Exception {
		List<Menu> menulist = menuServiceAO.getMenuList(null);
		ObjectMapper mapper = new ObjectMapper();  
        String mlJson = "";
		try {
			mlJson = mapper.writeValueAsString(menulist);
		}catch (Exception e) {
			e.printStackTrace();
		} 
		ReturnData returnData = new ReturnData(true,mlJson);
		return returnData;
	}
	
	
	/**
	 * 修改菜单信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/menuUpdate")
	@ResponseBody
	public ReturnData menuUpdate(HttpServletRequest request) throws Exception {
		String name = request.getParameter("name");
		String desc = request.getParameter("desc");
		String link = request.getParameter("link");
		String id = request.getParameter("id");
		String pclass = request.getParameter("pclass");
		Menu menu = new Menu();
		menu.setName(name.trim());
		menu.setDescription(desc.trim());
		menu.setUrl(link.trim());
		menu.setId(Integer.parseInt(id));
		menu.setpClass(pclass);
		
		Boolean status = menuServiceAO.updateMenu(menu);
		ReturnData returnData = new ReturnData(false,null);
		if(status){
			returnData = new ReturnData(true,null);
		}
		
		return returnData;
	}
	
	/**
	 * 增加菜单
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/menuAdd")
	@ResponseBody
	public ReturnData menuAdd(HttpServletRequest request) throws Exception {
		String name = request.getParameter("name");
		String pId = request.getParameter("pId");
		String isParent = request.getParameter("isParent");
		
		Menu menu = new Menu();
		menu.setName(name);
		menu.setpId(Integer.parseInt(pId));
		menu.setIsParent(Boolean.valueOf(isParent));
		
		ReturnData returnData = new ReturnData(false,null);
		long id = menuServiceAO.addMenu(menu);
		if(id > 0){
			returnData = new ReturnData(true,id);
		}
		return  returnData;
	}
	
	
	/**
	 * 删除整个目录或者菜单
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/menuDelAll")
	@ResponseBody
	public ReturnData menuDelAll(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		
		Menu menu = new Menu();
		menu.setId(Integer.parseInt(id));
		menu.setpId(Integer.parseInt(id));
		
		ReturnData returnData = new ReturnData(false,null);
		
		menuServiceAO.delAllMenuInfoByPid(menu);
		long counts = menuServiceAO.delAllMenuById(menu);
		if(counts > 0){
			returnData = new ReturnData(true,null);
		}
		
		return returnData;
	}
	/**
	 * 删除整个目录的子菜单
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/menuDelchildren")
	@ResponseBody
	public ReturnData menuDelchildren(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		
		Menu menu = new Menu();
		menu.setId(Integer.parseInt(id));
		menu.setpId(Integer.parseInt(id));
		
		ReturnData returnData = new ReturnData(false,null);
		
		long counts = menuServiceAO.delAllMenuInfoByPid(menu);
		if(counts > 0){
			returnData = new ReturnData(true,null);
		}
		
		return  returnData;
	}
	

 }
