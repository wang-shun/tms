package com.hoperun.tms.mapper.customer; 

import java.util.List;

import com.hoperun.framework.base.BaseMapper;
import com.hoperun.tms.bean.customer.DeliveryBillTmp;
import org.springframework.stereotype.Component;


/**
 *  DAO
 * @author 15120071
 * 2016-3-22
 */
@Component
public interface DeliveryBillTmpMapper extends BaseMapper{
	/**
	 * 查询 
	 * @param DeliveryBillTmp
	 * @return list
	 * @throws DAOException
	 */
	List<DeliveryBillTmp> select(DeliveryBillTmp deliveryBillTmp) throws Exception;
	/**
	 * 查询 
	 * @param DeliveryBillTmp
	 * @return list
	 * @throws DAOException
	 */
	Long selectMaxId() throws Exception;

	/**
	 * 删除
	 * @param DeliveryBillTmp
	 * @return status
	 * @throws DAOException
	 */
	long delDeliveryBillTmpById(Long id) throws Exception;
	
	
	/**
	 * 修改
	 * @param DeliveryBillTmp
	 * @return status
	 * @throws DAOException
	 */
	void updateDeliveryBillTmp(DeliveryBillTmp deliveryBillTmp) throws Exception;
	
	
	/**
	 *	添加
	 * @param DeliveryBillTmp
	 * @return status
	 * @throws DAOException
	 */
	void insertDeliveryBillTmp(DeliveryBillTmp deliveryBillTmp) throws Exception;
	

}
 