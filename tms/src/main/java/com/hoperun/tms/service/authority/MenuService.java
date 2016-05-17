package com.hoperun.tms.service.authority;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoperun.tms.bean.authority.Menu;
import com.hoperun.tms.mapper.authority.MenuMapper;;

@Service("menuService")
public class MenuService {

	@Autowired
	private MenuMapper menuDAO;
	
	
	public List<Menu> getMenuList(Menu m){
         List<Menu> menulist = null;
		try {
			menulist = menuDAO.select(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
         return menulist;
	}


	public Boolean updateMenu(Menu menu) {
		try {
			Boolean status = menuDAO.update(menu);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}


	public long addMenu(Menu menu) {
		try {
			menuDAO.add(menu);
			return menu.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	public long delAllMenuById(Menu menu) {
		try {
			long couns = menuDAO.delAllMenuById(menu);
			return couns;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public long delAllMenuInfoByPid(Menu menu) {
		try {
			long couns = menuDAO.delAllMenuInfoByPid(menu);
			return couns;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	public Menu findBySysMenuId(Integer menu_id) {
			try {
				Menu m = new Menu();
				m.setId(menu_id);
				 List<Menu>	menulist = menuDAO.select(m);
				 if(null != menulist && menulist.size() > 0){
					return  menulist.get(0);
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public List<Menu> findBySysMenuIds(List<String> ids) {
		try {
			 List<Menu>	menulist = menuDAO.findBySysMenuIds(ids);
			 if(null != menulist && menulist.size() > 0){
				return  menulist;
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	return null;
}
	

}
