package com.hoperun.tms.mapper.authority; 

import java.util.List;

import com.hoperun.tms.bean.authority.RoleMenu;
import org.springframework.stereotype.Component;


/**
 *  DAO
 * @author wangchaofeng 2016-1-13 10:36:01
 */
@Component
public interface RoleMenuMapper {

	
	/**
	 * 插入角色
	 * @param Role
	 * @return status
	 * @throws DAOException
	 */
	long insertRolemenuList(List<RoleMenu> rolemenuList) throws Exception;
	
	/**
	 * 插入角色
	 * @param Role
	 * @return status
	 * @throws DAOException
	 */
	void delRoleMenuById(RoleMenu rolemenu) throws Exception;

	/**
	 * 根据角色拿菜单权限
	 * @param Role
	 * @return status
	 * @throws DAOException
	 */
	List<RoleMenu> getRoleMenuList(RoleMenu roleMenu) throws Exception;
	
	/**
	 * 根据角色拿菜单权限 按照 id
	 * @param Role
	 * @return status
	 * @throws DAOException
	 */
	List<RoleMenu> getRoleMenuByIds(List<String> rmlist) throws Exception;
}
 