package com.hoperun.tms.mapper.base.extend;

import com.hoperun.framework.annotation.DataSource;
import com.hoperun.tms.mapper.base.DriverMapper;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Created by 16030117 on 2016/4/13.
 */
@Component
public interface ExDriverMapper extends DriverMapper{
	
	@DataSource("master")
	public Map selectDriverAndVno(String phoneNum);
}
