package com.hoperun.tms.service.authority;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoperun.tms.bean.authority.RoleMenu;
import com.hoperun.tms.mapper.authority.RoleMenuMapper;;

@Service("roleMenuService")
public class RoleMenuService {

	@Autowired
	private RoleMenuMapper rolemenuDAO;
	
	public long insertRolemenuList(List<RoleMenu> rolemenuList) {
		try {
			long status = rolemenuDAO.insertRolemenuList(rolemenuList);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void delRoleMenuById(RoleMenu roleMenu) {
		try {
			rolemenuDAO.delRoleMenuById(roleMenu);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<RoleMenu> getRoleMenuList(RoleMenu roleMenu) {
		try {
			List<RoleMenu> rmList = rolemenuDAO.getRoleMenuList(roleMenu);
			return rmList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<RoleMenu> getRoleMenuListByRIds(List<String> rmlist) {
		try {
			List<RoleMenu> rmList = rolemenuDAO.getRoleMenuByIds(rmlist);
			return rmList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
