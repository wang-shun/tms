package com.hoperun.tms.service.authority;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.authority.Role;
import com.hoperun.tms.mapper.authority.RoleMapper;;

@Service("roleService")
public class RoleService {

	@Autowired
	private RoleMapper roleDAO;
	
	
	public List<Role> getRoleList(Role r){
         List<Role> rlist = null;
		try {
			rlist = roleDAO.select(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
         return rlist;
	}
	
	public Map getRoleListPaginate(Role r,int pageNumber,int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		Page startPage = PageHelper.startPage(pageNumber, pageSize);
		List<Role> roleList = getRoleList(r);
		Long totalrecords = startPage.getTotal();
		map.put("roleList", roleList);
		map.put("currpage", pageNumber);
		map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
		map.put("totalrecords", totalrecords);
		return map;
	}


	public long delRoleById(Role role) {
		try {
			long status = roleDAO.delRoleById(role);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	public void updateRole(Role role) {
		try {
			roleDAO.updateRole(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	public void insertRole(Role role) {
		try {
			roleDAO.insertRole(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	

}
