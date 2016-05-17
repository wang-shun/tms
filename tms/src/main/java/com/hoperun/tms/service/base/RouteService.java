package com.hoperun.tms.service.base;
 
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hoperun.tms.bean.base.RouteExample;
import com.hoperun.tms.bean.base.extend.ExRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.base.Route;
import com.hoperun.tms.mapper.base.RouteMapper;

@Service
public class RouteService {

	@Autowired
	private RouteMapper routeMapper;

	public List<Route> getRouteList(RouteExample e){
         return routeMapper.selectByExample(e);
	}
	
	public Map getRouteListPaginate(RouteExample e,int pageNumber,int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		Page startPage = PageHelper.startPage(pageNumber, pageSize);
		List<Route> routeList =getRouteList(e);
		Long totalrecords = startPage.getTotal();
		map.put("routeList", routeList);
		map.put("currpage", pageNumber);
		map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
		map.put("totalrecords", totalrecords);
		return map;
	}

	public void updateMultiRoutes(Route route, RouteExample e) throws Exception {
		route.setUpdatedBy("ceshi");
		route.setUpdatedAt(new Date());
		routeMapper.updateByExampleSelective(route, e);
	}

	/**
	 * route必须包含主键ID
	 * @param route
	 * @throws Exception
     */
	public void updateById(Route route) throws Exception{
		if (null == route.getId()) {
			throw new Exception("路线信息更新失败，未正确获取路线ID");
		}
		uniqueValidate(route);
		route.setUpdatedBy("ceshi");
		route.setUpdatedAt(new Date());
		routeMapper.updateByPrimaryKeySelective(route);
	}

	/**
	 * 路线作废
	 * route必须包含主键ID
	 * @throws Exception
	 */
	public void disable(String[] ids) throws Exception{
		for (String id : ids) {
			if (null == id) {
				throw new Exception("路线信息删除失败，未正确获取路线ID");
			}
			Route route = new Route();
			route.setId(Long.parseLong(id));
			route.setStatus(ExRoute.STATUS_DISABLED);
			route.setUpdatedBy("ceshi");
			route.setUpdatedAt(new Date());
			routeMapper.updateByPrimaryKeySelective(route);
		}
	}

	public void insertRoute(Route route) throws Exception{
		uniqueValidate(route);
		route.setCreatedBy("ceshi");
		route.setCreatedAt(new Date());
		route.setUpdatedBy("ceshi");
		route.setUpdatedAt(new Date());
		routeMapper.insertSelective(route);
	}

	private void uniqueValidate(Route route) throws Exception{
		RouteExample e = new RouteExample();
		RouteExample.Criteria c = e.createCriteria();
		c.andNameEqualTo(route.getName()).andStatusEqualTo(ExRoute.STATUS_ENABLED);
		if(null != route.getId()){
			c.andIdNotEqualTo(route.getId());
		}
		List<Route> routes = routeMapper.selectByExample(e);
		if (!routes.isEmpty()) {
			throw new Exception("线路名:"+route.getName()+"在路线列表中已存在。");
		}
	}
}
