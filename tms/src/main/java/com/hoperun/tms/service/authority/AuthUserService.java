package com.hoperun.tms.service.authority;
 
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.authority.User;
import com.hoperun.tms.bean.authority.UserRole;
import com.hoperun.tms.mapper.authority.AuthUserMapper;
import com.hoperun.tms.util.IpUtil;

@Service("authUserService")
public class AuthUserService{

	@Autowired
	private AuthUserMapper authUserMapper;
	
	public Map getUserListPaginate(User user, int pageNumber, int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		Page startPage = PageHelper.startPage(pageNumber, pageSize);
		List<User> userList =getUserList(user);
		Long totalrecords = startPage.getTotal();
		map.put("userList", userList);
		map.put("currpage", pageNumber);
		map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
		map.put("totalrecords", totalrecords);
		return map;
	}
	
	
	public List<User> getUserList(User r){
         List<User> rlist = null;
		try {
			rlist = authUserMapper.select(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
         return rlist;
	}


	public long delUserById(User user) {
		try {
			long status = authUserMapper.delUserById(user);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Boolean userIsExists(User user) {
		try {
			Integer isExists = authUserMapper.userIsExists(user);
			if(isExists == 1){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	


	public void updateUser(User user) {
		try {
			authUserMapper.updateUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	public void insertUser(User user) {
		try {
			authUserMapper.insertUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void addUserRole(List<UserRole> urList) {
		try {
			authUserMapper.insertUserRole(urList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	public List<UserRole> getUserRoleList(UserRole ur) {
		  List<UserRole> rlist = null;
		try {
			rlist = authUserMapper.getUserRoleList(ur);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rlist;
	}


	public void delUserRole(UserRole ur) {
		try {
			authUserMapper.delUserRole(ur);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public User findByLoginName(String loginName) {
		User user = null;
		try {
			User r = new User();
			r.setLogin_name(loginName);
			List<User> rlist = authUserMapper.select(r);
			if(rlist != null && rlist.size() > 0){
				user = rlist.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}


    public void refreshUser(HttpServletRequest request,User user){
    	try {
	    	User u = new User();
	    	u.setId(user.getId());
	    	u.setLastLoginTime(new Date());
	    	u.setLastLoginIp(IpUtil.getIpAddr(request));
	    	authUserMapper.updateUserLoginMess(u);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }


	

}
