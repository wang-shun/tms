package com.hoperun.shinho.service;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoperun.framework.base.BaseService;
import com.hoperun.framework.exception.ServiceException;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.shinho.bean.UserEntity;
import com.hoperun.shinho.mapper.UserMapper;

/**
 * <br>
 * <b>功能：</b>提供用户数据访问服务的类。<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Dec 9, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
@Service("userService")
public class UserService extends BaseService {
	private static final Logger LOGGER = Logger.getLogger(UserService.class);

	// 表示验证码的有效时间。豪秒。
	private static final int TIME_SPAN = 600000;

	private static final int LOCK = 0;

	/**
	 * 根据指定的用户账户，查询对应的用户信息。
	 * 
	 * @param memberId
	 *            指定的用户id
	 * @return 对应的用户信息。
	 */
	public UserEntity queryById(String memberId) {
		return getMapper().queryById(memberId);
	}

	/**
	 * 根据用户账号（电话号码）查询用户对象。
	 * 
	 * @param cellPhoneNo
	 *            用户账号（电话号码）
	 */
	public UserEntity queryByCellPhone(String cellPhoneNo) {
		return getMapper().queryByCellPhone(cellPhoneNo);
	}

	/**
	 * insert a user record
	 * 
	 * @param userEntity
	 * @return
	 */
	private void createUser(UserEntity userEntity) {

		userEntity.setCustomerId(createUserId());
		getMapper().insert(userEntity);
	}

	/**
	 * 开头1位数字"6"+9位流水 共10位数字的一个编号
	 * 
	 * @return
	 */
	private String createUserId() {
		return "6" + StringUtil.fillZero(getMapper().queryCustomerIdSeq(), 9);
	}

	/**
	 * 根据OpenId创建一条新记录
	 * 
	 * @param openId
	 */
	public void createUserByOpenId(String openId) {
		UserEntity userEntity = new UserEntity();
		userEntity.setOpenId(openId);
		createUser(userEntity);
	}

	/**
	 * @param userEntity
	 * @throws ServiceException
	 */
	public UserEntity updateUser(UserEntity userEntity) throws ServiceException {
		getMapper().update(userEntity);
		return queryById(userEntity.getCustomerId());
	}

	/**
	 * @param userEntity
	 * @throws ServiceException
	 */
	public void updateWXInfo(UserEntity userEntity) throws ServiceException {
		getMapper().updateWXInfo(userEntity);
	}

	/**
	 * 最后接触时间
	 * 
	 * @param memberId
	 */
	public void updateLastDate(String memberId) {
		getMapper().updateLastDate(memberId);
	}

	/**
	 * check cell phone no is unique without current user no
	 * 
	 * @param userEntity
	 * @return
	 */
	private boolean isUnique(UserEntity userEntity) {
		UserEntity userEntityResult = getMapper().queryByCellPhoneExceptCurrentUser(userEntity);
		return userEntityResult == null ? true : false;
	}

	/**
	 * 根据OpenId创建一条新记录
	 * 
	 * @param openId
	 */
	public void createUserByWXUserInfo(String openId, String nickName, String headImage) {
		synchronized (this) {
			UserEntity userEntityInDB = this.queryByOpenId(openId);
			if (userEntityInDB == null) {
				UserEntity userEntity = new UserEntity();
				userEntity.setCustomerId(UUID.randomUUID().toString());
				userEntity.setNick_name(nickName);
				userEntity.setOpenId(openId);
				userEntity.setHead_img(headImage);
				createUser(userEntity);
			}
		}

	}

	/**
	 * 根据指定的 openId 查询会员信息。
	 * 
	 * @param openId
	 *            指定的 openId
	 * @return
	 */
	public UserEntity queryByOpenId(String openId) {
		return getMapper().queryByOpenId(openId);
	}

	/**
	 * 验证手机号码是否合法。
	 * 
	 * @param telephone
	 *            指定的手机号码。
	 * @return
	 */
	private void validateTelephone(String telephone) throws Exception {

		if (telephone == null || "".equals(telephone)) {
			throw new ServiceException("提交的电话号码不能为空。");
		}

		String regex = "\\d{11}";

		if (!telephone.matches(regex)) {
			throw new ServiceException("提交的电话号码不合法。");
		}
	}

	/**
	 * 获取用户SequenceId
	 * 
	 * @param
	 */
	public String queryCustomerIdSeq() {
		return getMapper().queryCustomerIdSeq();
	}

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserMapper getMapper() {
		return userMapper;
	}
}