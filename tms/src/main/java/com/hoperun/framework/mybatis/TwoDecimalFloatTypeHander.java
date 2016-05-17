package com.hoperun.framework.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.apache.ibatis.type.FloatTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * <br>
 * <b>功能：</b>两位精度浮点型辅助类<br>
 * <b>作者：</b>Wang Jiahao<br>
 * <b>日期：</b> Jan 6, 2016 <br>
 * <b>版权所有：<b>版权所有(C) 2016，www.hoperun.com<br>
 */
public class TwoDecimalFloatTypeHander extends FloatTypeHandler {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Float parameter, JdbcType jdbcType) throws SQLException {
		ps.setFloat(i, parameter);
	}

	@Override
	public Float getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return keepTwoDecimalFloat(rs.getFloat(columnName));
	}

	@Override
	public Float getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return keepTwoDecimalFloat(rs.getFloat(columnIndex));
	}

	@Override
	public Float getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return keepTwoDecimalFloat(cs.getFloat(columnIndex));
	}

	/**
	 * Float 保留两位小数
	 * 
	 * @return
	 */
	private static Float keepTwoDecimalFloat(Float f) {
		DecimalFormat decimalFormat = new DecimalFormat(".00");
		return Float.parseFloat(decimalFormat.format(f));
	}
}
