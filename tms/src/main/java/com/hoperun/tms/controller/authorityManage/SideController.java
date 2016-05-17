package com.hoperun.tms.controller.authorityManage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoperun.tms.bean.authority.Menu;
import com.hoperun.tms.bean.authorityDto.MenuPage;
import com.hoperun.tms.mapper.ReturnData;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SideController {

	/**
	 * 首页侧边栏
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/controlSide")
	@ResponseBody
	public ReturnData rushed(HttpServletRequest request) throws Exception {
		ReturnData returnData = new ReturnData(false, null);
		try{
			List<Menu> ms  = (List<Menu>)request.getSession().getAttribute("menuList");
			List<MenuPage> mplist = new ArrayList<MenuPage>();
			
			if (ms != null) {
				for (Menu m : ms) {
					if(m == null){
						continue;
					}
					MenuPage mp = new MenuPage();
					PropertyUtils.copyProperties(mp,m);
					if(mp.getIsParent() && mp.getpId()!= null && mp.getpId() > 0 ){
						mplist.add(mp);
					}else if(!mp.getIsParent() && mp.getpId()!= null && mp.getpId() > 0){
						 for(MenuPage mptemp : mplist){
							 if(mptemp.getId().intValue() == mp.getpId().intValue()){
								 mptemp.getMenuPageList().add(mp);
							 }
						 }
					}
				}
				
				if(mplist.size() > 0){
					returnData = new ReturnData(true, mplist);
				}
			}
//			
//		User user = (User) SecurityUtils.getSubject().getPrincipal();
//		UserRole ur = new UserRole();
//		ur.setUser_id(user.getId());
//		List<UserRole> urList = userServiceAO.getUserRoleList(ur);
//
//		if (null != urList && urList.size() > 0) {
//			List<MenuPage> mplist = new ArrayList<MenuPage>();
//			Map<String,String> meunId = new HashMap<String,String>();
//			for (UserRole urTemp : urList) {
//				RoleMenu rm = new RoleMenu();
//				rm.setRole_id(urTemp.getRole_id());
//				List<RoleMenu> rmList = roleMenuServiceAO.getRoleMenuList(rm);
//				if (rmList != null) {
//					for (RoleMenu rmTemp : rmList) {
//						if(meunId.containsKey(rmTemp.getMenu_id().toString())){
//							continue;
//						}
//						meunId.put(rmTemp.getMenu_id().toString(), "hava");
//						Menu m = menuServiceAO.findBySysMenuId(rmTemp.getMenu_id());
//						if(m == null){
//							continue;
//						}
//						MenuPage mp = new MenuPage();
//						PropertyUtils.copyProperties(mp,m);
//						if(mp.getIsParent() && mp.getPId()!= null && mp.getPId() > 0 ){
//							mplist.add(mp);
//						}else if(!mp.getIsParent() && mp.getPId()!= null && mp.getPId() > 0){
//							 for(MenuPage mptemp : mplist){
//								 if(mptemp.getId().intValue() == mp.getPId().intValue()){
//									 mptemp.getMenuPageList().add(mp);
//								 }
//							 }
//						}
//						
//					}
//				}
//				
//			}
//			if(mplist.size() > 0){
//				returnData = new ReturnData(true, mplist);
//			}
//		}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return  returnData;
	}

}
