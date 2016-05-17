package com.hoperun.tms.mapper.transportationPlan; 

import java.util.List;

import com.hoperun.framework.base.BaseMapper;
import com.hoperun.tms.bean.base.Driver;
import org.springframework.stereotype.Component;


/**
 *  DAO
 * @author 15120071
 * 2016-3-22
 */
@Component
public interface VehicleConfirmationMapper extends BaseMapper{

	
	/**
	 * 查询 
	 * @param Driver
	 * @return list
	 * @throws DAOException
	 */
	List<Driver> select(Driver driver) throws Exception;
	/**
	 * 查询 
	 * @param Driver
	 * @return list
	 * @throws DAOException
	 */
	Long selectMaxId() throws Exception;

	/**
	 * 删除
	 * @param Driver
	 * @return status
	 * @throws DAOException
	 */
	long delDriverById(long id) throws Exception;
	
	
	/**
	 * 修改
	 * @param Driver
	 * @return status
	 * @throws DAOException
	 */
	void updateDriver(Driver driver) throws Exception;
	
	
	/**
	 *	添加
	 * @param Driver
	 * @return status
	 * @throws DAOException
	 */
	void insertDriver(Driver driver) throws Exception;
	


}
 