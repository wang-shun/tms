package com.hoperun.tms.mapper.authority; 

import java.util.List;

import com.hoperun.framework.base.BaseMapper;
import com.hoperun.tms.bean.authority.Menu;
import org.springframework.stereotype.Component;


/**
 *  DAO
 * @author wangchaofeng 2016-1-13 10:36:01
 */

@Component
public interface MenuMapper extends BaseMapper{

	
	/**
	 * 查询 
	 * @param Role
	 * @return 主键
	 * @throws DAOException
	 */
	List<Menu> select(Menu menu) throws Exception;
	
	List<Menu> findBySysMenuIds(List<String> ids) throws Exception;

	Boolean update(Menu menu) throws Exception;

	long add(Menu menu) throws Exception;

	long delAllMenuById(Menu menu) throws Exception;
	
	long delAllMenuInfoByPid(Menu menu) throws Exception;
}
 