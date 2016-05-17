package com.hoperun.shinho.mapper;

import java.util.Map;

import com.hoperun.framework.annotation.DataSource;
import com.hoperun.framework.base.BaseMapper;
import com.hoperun.shinho.bean.UserEntity;

/**
 * 用户管理 Mapper。
 * 
 * @author ye_wenchen
 */
public interface UserMapper extends BaseMapper {
	/**
	 * 根据唯一标识查询用户。
	 * 
	 * @param memberId
	 *            指定的用户唯一标识。
	 */
	@DataSource("master")
	public UserEntity queryById(String memberId);

	/**
	 * 根据用户电话号码查询用户对象。
	 * 
	 * @param cellPhoneNo
	 *            用户电话号码
	 */
	@DataSource("master")
	public UserEntity queryByCellPhone(String cellPhoneNo);

	/**
	 * 更新用户账号。
	 * 
	 * @param userEntity
	 *            用户信息
	 */
	@DataSource("master")
	public void update(UserEntity userEntity);

	/**
	 * 更新用户微信账号数据。
	 * 
	 * @param userEntity
	 *            用户信息
	 */
	@DataSource("master")
	public void updateWXInfo(UserEntity userEntity);

	/**
	 * 最后接触时间(下订单、评价商品、修改个人信息)
	 * 
	 * @param memberId
	 */
	@DataSource("master")
	public void updateLastDate(String memberId);

	/**
	 * 根据用户OpenId查询用户。
	 * 
	 * @param rowWid
	 *            指定的用户编码唯一标识。
	 */
	@DataSource("master")
	public UserEntity queryByOpenId(String openId);

	/**
	 * 根据手机号查询用户信息，同时排除当前用户Id，用于判断用户手机号是否唯一
	 * 
	 * @param userEntity
	 * @return
	 */
	@DataSource("master")
	public UserEntity queryByCellPhoneExceptCurrentUser(UserEntity userEntity);

	/**
	 * 激活电话存储过程。
	 * 
	 * @param map
	 *            指定的参数。 用户名，电话。
	 */
	public void doActivation(Map<String, String> p_map);

	/**
	 * 获取用户SequenceId
	 * 
	 * 
	 */
	public String queryCustomerIdSeq();

}