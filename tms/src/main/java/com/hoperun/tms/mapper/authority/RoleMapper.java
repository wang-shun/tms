package com.hoperun.tms.mapper.authority; 

import java.util.List;

import com.hoperun.tms.bean.authority.Role;
import org.springframework.stereotype.Component;


/**
 *  DAO
 * @author wangchaofeng 2016-1-13 10:36:01
 */
@Component
public interface RoleMapper {

	
	/**
	 * 查询 
	 * @param RoleDto
	 * @return list
	 * @throws DAOException
	 */
	List<Role> select(Role role) throws Exception;

	/**
	 * 删除
	 * @param RoleDto
	 * @return status
	 * @throws DAOException
	 */
	long delRoleById(Role role) throws Exception;
	
	/**
	 * 修改
	 * @param RoleDto
	 * @return status
	 * @throws DAOException
	 */
	void updateRole(Role role) throws Exception;
	
	
	/**
	 *	添加
	 * @param RoleDto
	 * @return status
	 * @throws DAOException
	 */
	void insertRole(Role role) throws Exception;

}
 