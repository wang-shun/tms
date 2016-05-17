package com.hoperun.framework.base;

import java.util.List;

import com.hoperun.framework.annotation.DataSource;
import org.apache.ibatis.annotations.Param;

/**
 * <br>
 * <b>功能：</b>BaseMapper<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Nov 9, 2014 <br>
 * <b>版权所有：<b>版权所有(C) 2014，www.hoperun.com<br>
 */
public interface BaseMapper {

	@DataSource("master")
	public <E> int countByExample(E e);

	@DataSource("master")
	public <E> int deleteByExample(E e);

	@DataSource("master")
	public <K> int deleteByPrimaryKey(K k);

	@DataSource("master")
	public <T extends BaseBean> int insert(T t);

	@DataSource("master")
	public <T extends BaseBean> int insertSelective(T t);

	@DataSource("master")
	public <T extends BaseBean,E> List<T> selectByExample(E e);

	@DataSource("master")
	public <T extends BaseBean,K> T selectByPrimaryKey(K k);

	@DataSource("master")
	public <T extends BaseBean, E> int updateByExampleSelective(@Param("record") T t, @Param("example") E e);

	@DataSource("master")
	public <T extends BaseBean, E> int updateByExample(@Param("record") T t, @Param("example") E e);

	@DataSource("master")
	public <T extends BaseBean> int updateByPrimaryKeySelective(T t);

	@DataSource("master")
	public <T extends BaseBean> int updateByPrimaryKey(T t);

	@DataSource("master")
	public <T extends BaseBean,E> List<T> queryForList(E e, int start, int end);

	/*@DataSource("master")
	public <T extends BaseBean,E> List<T> selectOnPage(
			@Param("example")E example, @Param("skipResults")int skipResults,
			@Param("maxResults")int maxResults);*/
}
