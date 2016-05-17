package com.hoperun.framework.base;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * <br>
 * <b>功能：</b>BaseService<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public abstract class BaseService {

	public abstract BaseMapper getMapper();

	public void add(BaseBean bean) throws Exception {
		getMapper().insert(bean);
	}

	public <E> void update(BaseBean bean, E e) throws Exception {
		getMapper().updateByExample(bean, e);
	}

	public <E> void updateBySelective(BaseBean bean, E e) {
		getMapper().updateByExampleSelective(bean, e);
	}

	public void delete(Object... ids) throws Exception {
		if (ids == null || ids.length < 1) {
			return;
		}
		for (Object id : ids) {
			getMapper().deleteByPrimaryKey(id);
		}
	}

	public <E> int queryByCount(E e) throws Exception {
		return getMapper().countByExample(e);
	}

	public <E> List<BaseBean> queryList(E e) throws Exception {
		return getMapper().selectByExample(e);
	}

	public <E> PageInfo<BaseBean> queryListByPage(E e, int pageNum,
			int pageSize) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		List<BaseBean> list = getMapper().selectByExample(e);
		PageInfo<BaseBean> page = new PageInfo<BaseBean>(list);

		return page;

	}

	public BaseBean queryById(Object id) throws Exception {
		return getMapper().selectByPrimaryKey(id);
	}

}
