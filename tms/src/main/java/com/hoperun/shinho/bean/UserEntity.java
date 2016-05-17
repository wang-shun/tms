package com.hoperun.shinho.bean;

import java.util.Date;

import org.apache.cxf.common.util.StringUtils;

import com.hoperun.framework.base.BaseBean;

/**
 * <br>
 * <b>功能：</b>表示用户实体的类。<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Dec 9, 2015 <br>
 * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
 */
public class UserEntity extends BaseBean implements Cloneable {

	// PK member_id VARCHAR2(50) 会员编码
	private String customerId;
	// open_id VARCHAR2(100) 微信OpenID
	private String openId = "";
	// phone VARCHAR2(50) 联系电话
	private String tel = "";
	// name VARCHAR2(50),会员名称
	private String name = "";
	// nick_name VARCHAR2(50),昵称
	private String nick_name = "";
	// real_name VARCHAR2(50),真实姓名
	private String real_name = "";
	// head img from wx
	private String head_img = "";
	// type_id VARCHAR2(50),类型(01 对外消费者 02 内部员工)一期都是01，对外消费则
	private String type_id = "01";
	// level_id VARCHAR2(100),会员等级
	private String level_id = "";
	// 级别名称
	private String level_name = "";
	// emp_id VARCHAR2(50),员工工号
	private String emp_id = "";
	// password VARCHAR2(50),密码MD5
	private String password = "";
	// email VARCHAR2(50),邮件
	private String email = "";
	// group_id VARCHAR2(50),分组用户
	private String group_id = "";
	// remark VARCHAR2(500),备注
	private String remark = "";
	// mobile_phone VARCHAR2(50),移动电话
	private String mobile_phone = "";
	// question VARCHAR2(50),找回密码问题
	private String question = "";
	// answer VARCHAR2(50),找回密码答案
	private String answer = "";
	// sex VARCHAR2(50),性别
	private String sex = "";
	// birthday VARCHAR2(50),生日（yyyy-MM-dd）
	private String birthday = "";
	// 省。province VARCHAR2(50)省
	private String province = "";
	// 城市。city VARCHAR2(50)城市
	private String city = "";
	// 区县。county VARCHAR2(50)区县
	private String district = "";
	// geo_id NUMBER, 区县ID，地区编码，关联价格主数据
	private Integer geo_id;
	// status VARCHAR2(50) default '1', 状态
	private String status = "1";
	// update_dt DATE default sysdate,更新時間
	private Date update_dt;
	// last_date DATE default sysdate,最后接触时间 下订单、评价商品、修改个人信息
	private Date last_date;
	// insdt DATE default sysdate 记录创建日期
	private Date insdt;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getLevel_id() {
		return level_id;
	}

	public void setLevel_id(String level_id) {
		this.level_id = level_id;
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMobile_phone() {
		return mobile_phone;
	}

	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Integer getGeo_id() {
		return geo_id;
	}

	public void setGeo_id(Integer geo_id) {
		this.geo_id = geo_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdate_dt() {
		return update_dt;
	}

	public void setUpdate_dt(Date update_dt) {
		this.update_dt = update_dt;
	}

	public Date getLast_date() {
		return last_date;
	}

	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}

	public Date getInsdt() {
		return insdt;
	}

	public void setInsdt(Date insdt) {
		this.insdt = insdt;
	}

	public String getLevel_name() {
		return level_name;
	}

	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}

	/**
	 * 根据手机号是否为空，判断是否绑定
	 * 
	 * @return
	 */
	public boolean isBinding() {
		return StringUtils.isEmpty(mobile_phone) ? false : true;
	}

	public Object clone() {
		UserEntity o = null;
		try {
			o = (UserEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}


}