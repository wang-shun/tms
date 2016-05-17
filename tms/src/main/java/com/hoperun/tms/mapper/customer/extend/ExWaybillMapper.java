package com.hoperun.tms.mapper.customer.extend;

import com.hoperun.framework.annotation.DataSource;
import com.hoperun.tms.bean.customer.extend.WaybillTransportDetail;
import com.hoperun.tms.mapper.customer.WaybillMapper;
import org.springframework.stereotype.Component;

/**
*  DAO
* @author 15120071
* 2016-3-22
*/
@Component
public interface ExWaybillMapper extends WaybillMapper {
	
	@DataSource("master")
	public WaybillTransportDetail selectWaybillTransportDetail(String no);
	
	@DataSource("master")
	public String selectWaybillNo();
}
 