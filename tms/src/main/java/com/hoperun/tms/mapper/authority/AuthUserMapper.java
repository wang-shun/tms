package com.hoperun.tms.mapper.authority; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.hoperun.framework.base.BaseMapper;
import com.hoperun.tms.bean.authority.User;
import com.hoperun.tms.bean.authority.UserRole;



 /**
 *  DAO
 * @author wangchaofeng 2016-1-13 10:36:01
 */
@Component
public interface AuthUserMapper  extends BaseMapper{

	
	/**
	 * 查询 
	 * @param User
	 * @return list
	 * @throws DAOException
	 */
	List<User> select(User user) throws Exception;

	/**
	 * 删除
	 * @param User
	 * @return status
	 * @throws DAOException
	 */
	long delUserById(User user) throws Exception;
	
	/**
	 * 用户是否存在
	 * @param User
	 * @return status
	 * @throws DAOException
	 */
	Integer userIsExists(User user) throws Exception;
	
	/**
	 * 修改
	 * @param User
	 * @return status
	 * @throws DAOException
	 */
	void updateUser(User user) throws Exception;
	
	/**
	 * 修改用户登录信息
	 * @param User
	 * @return status
	 * @throws DAOException
	 */
	void updateUserLoginMess(User user) throws Exception;
	
	
	/**
	 *	添加
	 * @param User
	 * @return status
	 * @throws DAOException
	 */
	void insertUser(User user) throws Exception;
	
	/**
	 *	添加
	 * @param User
	 * @return status
	 * @throws DAOException
	 */
	void insertUserRole(List<UserRole> urList) throws Exception;
	
	/**
	 *	查询
	 * @param User
	 * @return status
	 * @throws DAOException
	 */
	List<UserRole> getUserRoleList(UserRole ur) throws Exception;
	
	/**
	 *	删除
	 * @param User
	 * @return status
	 * @throws DAOException
	 */
	void delUserRole(UserRole ur) throws Exception;

}
 